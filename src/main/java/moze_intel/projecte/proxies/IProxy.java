package moze_intel.projecte.proxies;

import net.minecraft.entity.player.EntityPlayer;
import moze_intel.projecte.api.capabilities.IAlchBagProvider;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;

public interface IProxy
{
    void registerKeyBinds();
    void registerRenderers();
    void registerLayerRenderers();
    void initializeManual();
    void clearClientKnowledge();
    IKnowledgeProvider getClientTransmutationProps();
    IAlchBagProvider getClientBagProps();
    EntityPlayer getClientPlayer();
    boolean isJumpPressed();
}
