package main.java.quartzshard.projecttweaked.net;

import main.java.quartzshard.projecttweaked.PECore;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class ProjectEXNetHandler
{
	public static SimpleNetworkWrapper NET;

	public static void init()
	{
		NET = new SimpleNetworkWrapper(PECore.MODID);
		NET.registerMessage(new MessageSyncEMC.Handler(), MessageSyncEMC.class, 0, Side.CLIENT);
		NET.registerMessage(new MessageSendLinkStack.Handler(), MessageSendLinkStack.class, 1, Side.SERVER);
		NET.registerMessage(new MessageCreateItemButton.Handler(), MessageCreateItemButton.class, 2, Side.SERVER);
		NET.registerMessage(new MessageArcaneTableRecipeTransfer.Handler(), MessageArcaneTableRecipeTransfer.class, 3, Side.SERVER);
	}
}