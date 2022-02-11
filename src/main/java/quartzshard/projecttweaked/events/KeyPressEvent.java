package quartzshard.projecttweaked.events;

import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.network.PacketHandler;
import quartzshard.projecttweaked.network.packets.KeyPressPKT;
import quartzshard.projecttweaked.utils.ClientKeyHelper;
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
