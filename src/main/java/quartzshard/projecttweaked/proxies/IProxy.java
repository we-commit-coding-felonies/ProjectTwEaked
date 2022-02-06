package main.java.quartzshard.projecttweaked.proxies;

import main.java.quartzshard.projecttweaked.api.capabilities.IAlchBagProvider;
import main.java.quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayer;

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
