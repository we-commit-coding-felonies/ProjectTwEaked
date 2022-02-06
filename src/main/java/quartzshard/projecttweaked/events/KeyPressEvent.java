package main.java.quartzshard.projecttweaked.events;

import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.network.PacketHandler;
import main.java.quartzshard.projecttweaked.network.packets.KeyPressPKT;
import main.java.quartzshard.projecttweaked.utils.ClientKeyHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PECore.MODID)
public class KeyPressEvent
{
	@SubscribeEvent
	public static void keyPress(KeyInputEvent event)
	{
		for (KeyBinding k : ClientKeyHelper.mcToPe.keySet())
		{
			if (k.isPressed())
			{
				PacketHandler.sendToServer(new KeyPressPKT(ClientKeyHelper.mcToPe.get(k)));
			}
		}
	}
}
