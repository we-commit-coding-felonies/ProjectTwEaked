package main.java.quartzshard.projecttweaked.events;

import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PECore.MODID)
public class PlayerRender
{
	@SubscribeEvent
	public static void onFOVUpdateEvent(FOVUpdateEvent evt)
	{
		if (!evt.getEntity().getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty() && evt.getEntity().getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == ObjHandler.gemFeet)
		{
			evt.setNewfov(evt.getFov() - 0.4F);
		}
	}
}
