package main.java.quartzshard.projecttweaked.impl;

import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import main.java.quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import main.java.quartzshard.projecttweaked.api.event.PlayerKnowledgeChangeEvent;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import main.java.quartzshard.projecttweaked.network.PacketHandler;
import main.java.quartzshard.projecttweaked.network.packets.KnowledgeSyncPKT;
import main.java.quartzshard.projecttweaked.playerData.Transmutation;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import main.java.quartzshard.projecttweaked.utils.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class KnowledgeImpl {

    public static void init()
    {
        CapabilityManager.INSTANCE.register(IKnowledgeProvider.class, new Capability.IStorage<IKnowledgeProvider>() {
            @Override
            public NBTTagCompound writeNBT(Capability<IKnowledgeProvider> capability, IKnowledgeProvider instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IKnowledgeProvider> capability, IKnowledgeProvider instance, EnumFacing side, NBTBase nbt) {
                if (nbt instanceof NBTTagCompound) {
                    instance.deserializeNBT((NBTTagCompound) nbt);
                }
            }
        }, () -> new DefaultImpl(null));
    }

    private static class DefaultImpl implements IKnowledgeProvider
    {
        @Nullable
        private final EntityPlayer player;
        private final List<ItemStack> knowledge = new ArrayList<>();
        private final IItemHandlerModifiable inputLocks = new ItemStackHandler(9);
        private long emc = 0;
        private boolean fullKnowledge = false;

        private DefaultImpl(EntityPlayer player) {
            this.player = player;
        }

        private void fireChangedEvent()
        {
            if (player != null && !player.world.isRemote)
            {
                MinecraftForge.EVENT_BUS.post(new PlayerKnowledgeChangeEvent(player));
            }
        }

        @Override
        public boolean hasFullKnowledge()
        {
            return fullKnowledge;
        }

        @Override
        public void setFullKnowledge(boolean fullKnowledge)
        {
            boolean changed = this.fullKnowledge != fullKnowledge;
            this.fullKnowledge = fullKnowledge;
            if (changed)
            {
                fireChangedEvent();
            }
        }

        @Override
        public void clearKnowledge()
        {
            knowledge.clear();
            fullKnowledge = false;
            fireChangedEvent();
        }

        @Override
        public boolean hasKnowledge(@Nonnull ItemStack stack) {
            if (stack.isEmpty())
            {
                return false;
            }

            if (fullKnowledge)
            {
                return true;
            }

            for (ItemStack s : knowledge)
            {
                if (ItemHelper.basicAreStacksEqual(s, stack))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean addKnowledge(@Nonnull ItemStack stack) {
            if (fullKnowledge)
            {
                return false;
            }

            if (stack.getItem() == ObjHandler.tome)
            {
                if (!hasKnowledge(stack))
                {
                    knowledge.add(stack);
                }
                fullKnowledge = true;
                fireChangedEvent();
                return true;
            }

            if (!hasKnowledge(stack))
            {
                knowledge.add(stack);
                fireChangedEvent();
                return true;
            }

            return false;
        }

        @Override
        public boolean removeKnowledge(@Nonnull ItemStack stack) {
            boolean removed = false;

            if (stack.getItem() == ObjHandler.tome)
            {
                fullKnowledge = false;
                removed = true;
            }

            if (fullKnowledge)
            {
                return false;
            }

            Iterator<ItemStack> iter = knowledge.iterator();

            while (iter.hasNext())
            {
                if (ItemHelper.basicAreStacksEqual(stack, iter.next()))
                {
                    iter.remove();
                    removed = true;
                }
            }

            if (removed)
            {
                fireChangedEvent();
            }
            return removed;
        }

        @Override
        public @Nonnull List<ItemStack> getKnowledge() {
            return fullKnowledge ? Transmutation.getCachedTomeKnowledge() : Collections.unmodifiableList(knowledge);
        }

        @Override
        public @Nonnull IItemHandlerModifiable getInputAndLocks() {
            return inputLocks;
        }

        @Override
        public long getEmc() {
            return emc;
        }

        @Override
        public void setEmc(long emc) {
            this.emc = emc;
        }

        @Override
        public void sync(@Nonnull EntityPlayerMP player)
        {
            PacketHandler.sendTo(new KnowledgeSyncPKT(serializeNBT()), player);
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setLong("transmutationEmc", emc);

            NBTTagList knowledgeWrite = new NBTTagList();
            for (ItemStack i : knowledge)
            {
                NBTTagCompound tag = i.writeToNBT(new NBTTagCompound());
                knowledgeWrite.appendTag(tag);
            }

            properties.setTag("knowledge", knowledgeWrite);
            properties.setTag("inputlock", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inputLocks, null));
            properties.setBoolean("fullknowledge", fullKnowledge);
            return properties;
        }

        @Override
        public void deserializeNBT(NBTTagCompound properties)
        {
            emc = properties.getLong("transmutationEmc");

            NBTTagList list = properties.getTagList("knowledge", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++)
            {
                ItemStack item = new ItemStack(list.getCompoundTagAt(i));
                if (!item.isEmpty())
                {
                    knowledge.add(item);
                }
            }

            pruneStaleKnowledge();
            pruneDuplicateKnowledge();

            for (int i = 0; i < inputLocks.getSlots(); i++)
            {
                inputLocks.setStackInSlot(i, ItemStack.EMPTY);
            }

            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inputLocks, null, properties.getTagList("inputlock", Constants.NBT.TAG_COMPOUND));
            fullKnowledge = properties.getBoolean("fullknowledge");
        }

        private void pruneDuplicateKnowledge()
        {
            ItemHelper.removeEmptyTags(knowledge);
            ItemHelper.compactItemListNoStacksize(knowledge);
            for (ItemStack s : knowledge)
            {
                if (s.getCount() > 1)
                {
                    s.setCount(1);
                }
            }
        }

        private void pruneStaleKnowledge()
        {
            knowledge.removeIf(stack -> !EMCHelper.doesItemHaveEmc(stack));
        }

    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound>
    {
        public static final ResourceLocation NAME = new ResourceLocation(PECore.MODID, "knowledge");

        private final DefaultImpl knowledge;

        public Provider(EntityPlayer player)
        {
            knowledge = new DefaultImpl(player);
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
            return capability == ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
            if (capability == ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY)
            {
                return ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY.cast(knowledge);
            }
            return null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            return knowledge.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            knowledge.deserializeNBT(nbt);
        }

    }

    private KnowledgeImpl() {}

}
