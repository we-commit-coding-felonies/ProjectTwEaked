package main.java.quartzshard.projecttweaked.network.packets;

import io.netty.buffer.ByteBuf;
import main.java.quartzshard.projecttweaked.PECore;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KnowledgeClearPKT implements IMessage
{
	public KnowledgeClearPKT() {}

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}

	public static class Handler implements IMessageHandler<KnowledgeClearPKT, IMessage>
	{
		@Override
		public IMessage onMessage(KnowledgeClearPKT pkt, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					PECore.proxy.clearClientKnowledge();
				}
			});
			return null;
		}
	}
}
