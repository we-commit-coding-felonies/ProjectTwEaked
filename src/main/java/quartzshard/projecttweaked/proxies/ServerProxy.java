package main.java.quartzshard.projecttweaked.proxies;

import main.java.quartzshard.projecttweaked.api.capabilities.IAlchBagProvider;
import main.java.quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayer;

public class ServerProxy implements IProxy
{
	@Override public void registerKeyBinds() {}
	@Override public void registerRenderers() {}
	@Override public void registerLayerRenderers() {}
	@Override public void initializeManual() {}
	@Override public void clearClientKnowledge() {}
	@Override public IKnowledgeProvider getClientTransmutationProps() { return null; }
	@Override public IAlchBagProvider getClientBagProps() { return null; }
	@Override public EntityPlayer getClientPlayer() { return null; }
	@Override public boolean isJumpPressed() { return false; }
}
