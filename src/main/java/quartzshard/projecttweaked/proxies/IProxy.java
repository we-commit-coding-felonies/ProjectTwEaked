package quartzshard.projecttweaked.proxies;

import net.minecraft.entity.player.EntityPlayer;
import quartzshard.projecttweaked.api.capabilities.IAlchBagProvider;
import quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;

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
