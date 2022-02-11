package quartzshard.projecttweaked.gameObjs.items.rings;

import quartzshard.projecttweaked.api.item.IAlchBagItem;
import quartzshard.projecttweaked.api.item.IAlchChestItem;
import quartzshard.projecttweaked.api.item.IExtraFunction;
import quartzshard.projecttweaked.api.item.IPedestalItem;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.items.GemEternalDensity;
import quartzshard.projecttweaked.utils.ItemHelper;
import quartzshard.projecttweaked.utils.PlayerHelper;
import quartzshard.projecttweaked.PECore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class VoidRing extends GemEternalDensity implements IPedestalItem, IExtraFunction
{
	public VoidRing()
	{
		this.setTranslationKey("void_ring");
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isHeld)
	{
		super.onUpdate(stack, world, entity, slot, isHeld);
		ObjHandler.blackHole.onUpdate(stack, world, entity, slot, isHeld);
		if (!ItemHelper.getOrCreateCompound(stack).hasKey("teleportCooldown"))
		{
			stack.getTagCompound().setByte("teleportCooldown", ((byte) 10));
		}
		if(stack.getTagCompound().getByte("teleportCooldown") > 0) {
			stack.getTagCompound().setByte("teleportCooldown", ((byte) (stack.getTagCompound().getByte("teleportCooldown") - 1)));
		}
	}


	@Override
	public void updateInPedestal(@Nonnull World world, @Nonnull BlockPos pos)
	{
		((IPedestalItem) ObjHandler.blackHole).updateInPedestal(world, pos);
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getPedestalDescription()
	{
		return ((IPedestalItem) ObjHandler.blackHole).getPedestalDescription();
	}

	@Override
	public boolean doExtraFunction(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, EnumHand hand)
	{
		if (ItemHelper.getOrCreateCompound(stack).getByte("teleportCooldown") > 0 )
		{
			return false;
		}

		BlockPos c = PlayerHelper.getBlockLookingAt(player, 64);
		if (c == null)
		{
			c = new BlockPos(PlayerHelper.getLookVec(player, 32).getRight());
		}

		EnderTeleportEvent event = new EnderTeleportEvent(player, c.getX(), c.getY(), c.getZ(), 0);
		if (!MinecraftForge.EVENT_BUS.post(event))
		{
			if (player.isRiding())
			{
				player.dismountRidingEntity();
			}

			player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
			player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
			player.fallDistance = 0.0F;
			stack.getTagCompound().setByte("teleportCooldown", ((byte) 10));
			return true;
		}

		return false;
	}

	@Override
	public boolean updateInAlchBag(@Nonnull IItemHandler inv, @Nonnull EntityPlayer player, @Nonnull ItemStack stack)
	{
		((IAlchBagItem) ObjHandler.blackHole).updateInAlchBag(inv, player, stack);
		return super.updateInAlchBag(inv, player, stack); // Gem of Eternal Density
	}

	@Override
	public void updateInAlchChest(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack stack)
	{
		super.updateInAlchChest(world, pos, stack); // Gem of Eternal Density
		((IAlchChestItem) ObjHandler.blackHole).updateInAlchChest(world, pos, stack);
	}

	@Override
	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		byte oldMode = getMode(stack);

		if (oldMode >= 4)
		{
			ItemHelper.getOrCreateCompound(stack).setByte("Target", (byte) 0);
		}
		else
		{
			ItemHelper.getOrCreateCompound(stack).setByte("Target", (byte) (oldMode + 1));
		}

		player.sendMessage(new TextComponentTranslation("pe.gemdensity.mode_switch").appendText(" ").appendSibling(new TextComponentTranslation(getTargetName(stack))));
		return true;
	}

	@Override
	public String getTargetName(ItemStack stack)
	{
		switch(ItemHelper.getOrCreateCompound(stack).getByte("Target"))
		{
			case 0:
				return "item.ingotIron.name";
			case 1:
				return "item.ingotGold.name";
			case 2:
				return "item.diamond.name";
			case 3:
				return "item.pe_matter_dark.name";
			case 4:
				return "item.pe_matter_red.name";
			default:
				return "INVALID";
		}
	}
	
	@Override
	public ItemStack getTarget(ItemStack stack)
	{
		byte target = ItemHelper.getOrCreateCompound(stack).getByte("Target");
		switch (target)
		{
			case 0:
				return new ItemStack(Items.IRON_INGOT);
			case 1:
				return new ItemStack(Items.GOLD_INGOT);
			case 2:
				return new ItemStack(Items.DIAMOND);
			case 3:
				return new ItemStack(ObjHandler.matter, 1, 0);
			case 4:
				return new ItemStack(ObjHandler.matter, 1, 1);
			default:
				PECore.LOGGER.fatal("Invalid target for gem of eternal density: {}", target);
				return ItemStack.EMPTY;
		}
	}
}
