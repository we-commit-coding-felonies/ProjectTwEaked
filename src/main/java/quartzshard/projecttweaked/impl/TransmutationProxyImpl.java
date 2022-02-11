package quartzshard.projecttweaked.impl;

import com.google.common.base.Preconditions;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import quartzshard.projecttweaked.api.proxy.ITransmutationProxy;
import quartzshard.projecttweaked.utils.WorldTransmutations;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.UUID;

public class TransmutationProxyImpl implements ITransmutationProxy
{
    public static final ITransmutationProxy instance = new TransmutationProxyImpl();

    private TransmutationProxyImpl() {}

    @Override
    public boolean registerWorldTransmutation(@Nonnull IBlockState origin, @Nonnull IBlockState result1, IBlockState result2)
    {
        Preconditions.checkNotNull(origin);
        Preconditions.checkNotNull(result1);
        Preconditions.checkState(Loader.instance().isInState(LoaderState.POSTINITIALIZATION), String.format("Mod %s tried to register world transmutation at an invalid time!", Loader.instance().activeModContainer().getModId()));
        if (WorldTransmutations.getWorldTransmutation(origin, false) != null)
        {
            return false;
        }
        else
        {
            WorldTransmutations.register(origin, result1, result2);
            return true;
        }
    }

    @Nonnull
    @Override
    public IKnowledgeProvider getKnowledgeProviderFor(@Nonnull UUID playerUUID)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            Preconditions.checkState(PECore.proxy.getClientPlayer() != null, "Client player doesn't exist!");
            return PECore.proxy.getClientTransmutationProps();
        }
        else
        {
            Preconditions.checkNotNull(playerUUID);
            Preconditions.checkState(Loader.instance().hasReachedState(LoaderState.SERVER_STARTED), "Server must be running to query knowledge!");
            EntityPlayer player = findOnlinePlayer(playerUUID);
            if (player != null)
            {
                return player.getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null);
            }
            else
            {
                return TransmutationOffline.forPlayer(playerUUID);
            }
        }
    }

    private EntityPlayer findOnlinePlayer(UUID playerUUID)
    {
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            if (player.getUniqueID().equals(playerUUID))
            {
                return player;
            }
        }
        return null;
    }
}
