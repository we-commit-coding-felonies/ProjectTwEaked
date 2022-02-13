package quartzshard.projecttweaked.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import quartzshard.projecttweaked.api.item.IExtraFunction;
import quartzshard.projecttweaked.api.item.IItemCharge;
import quartzshard.projecttweaked.api.item.IModeChanger;
import quartzshard.projecttweaked.api.item.IProjectileShooter;
import quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.items.armor.GemArmorBase;
import quartzshard.projecttweaked.gameObjs.items.armor.GemChest;
import quartzshard.projecttweaked.gameObjs.items.armor.GemFeet;
import quartzshard.projecttweaked.gameObjs.items.armor.GemHelmet;
import quartzshard.projecttweaked.handlers.InternalAbilities;
import quartzshard.projecttweaked.utils.PEKeybind;
import quartzshard.projecttweaked.utils.PlayerHelper;

public class KeyPressPKT implements IMessage
{
	private PEKeybind key;

	public KeyPressPKT() {}

	public KeyPressPKT(PEKeybind key)
	{
		this.key = key;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		key = PEKeybind.values()[buf.readInt()];
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(key.ordinal());
	}

	public static class Handler implements IMessageHandler<KeyPressPKT, IMessage>
	{
		@Override
		public IMessage onMessage(final KeyPressPKT message, final MessageContext ctx)
		{
			ctx.getServerHandler().player.server.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    InternalAbilities internalAbilities = player.getCapability(InternalAbilities.CAPABILITY, null);

                    if (message.key == PEKeybind.ARMOR_TOGGLE)
                    {
                        if (player.isSneaking())
                        {
                            ItemStack helm = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

                            if (!helm.isEmpty() && helm.getItem() == ObjHandler.gemHelmet)
                            {
                                GemHelmet.toggleNightVision(helm, player);
                            }
                        }
                        else
                        {
                            ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

                            if (!boots.isEmpty() && boots.getItem() == ObjHandler.gemFeet)
                            {
                                ((GemFeet) ObjHandler.gemFeet).toggleStepAssist(boots, player);
                            }
                        }
                        return;
                    }

                    for (EnumHand hand : EnumHand.values())
                    {
                        ItemStack stack = player.getHeldItem(hand);
                        switch (message.key)
                        {
                            case CHARGE:
                                if (!stack.isEmpty()
                                        && stack.getItem() instanceof IItemCharge
                                        && ((IItemCharge) stack.getItem()).changeCharge(player, stack, hand))
                                {
                                    return;
                                } else if (hand == EnumHand.MAIN_HAND && (ProjectTwEakedConfig.misc.unsafeKeyBinds || stack.isEmpty()))
                                {
                                    if (GemArmorBase.hasAnyPiece(player))
                                    {
                                        internalAbilities.setGemState(!internalAbilities.getGemState());
                                        player.sendMessage(new TextComponentTranslation(internalAbilities.getGemState() ? "pe.gem.activate" : "pe.gem.deactivate"));
                                        return;
                                    }
                                }
                                break;
                            case EXTRA_FUNCTION:
                                if (!stack.isEmpty()
                                        && stack.getItem() instanceof IExtraFunction
                                        && ((IExtraFunction) stack.getItem()).doExtraFunction(stack, player, hand))
                                {
                                    return;
                                } else if (hand == EnumHand.MAIN_HAND && (ProjectTwEakedConfig.misc.unsafeKeyBinds || stack.isEmpty()))
                                {
                                    if (internalAbilities.getGemState()
                                            && !player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty()
                                            && player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == ObjHandler.gemChest)
                                    {
                                        if (internalAbilities.getGemCooldown() <= 0)
                                        {
                                            ((GemChest) ObjHandler.gemChest).doExplode(player);
                                            internalAbilities.resetGemCooldown();
                                            return;
                                        }
                                    }
                                }
                                break;
                            case FIRE_PROJECTILE:
                                if (!stack.isEmpty()
                                        && stack.getItem() instanceof IProjectileShooter
                                        && internalAbilities.getProjectileCooldown() <= 0
                                        && ((IProjectileShooter) stack.getItem()).shootProjectile(player, stack, hand))
                                {
                                    PlayerHelper.swingItem(player, hand);
                                    internalAbilities.resetProjectileCooldown();
                                    return;
                                } else if (hand == EnumHand.MAIN_HAND && (ProjectTwEakedConfig.misc.unsafeKeyBinds || stack.isEmpty()))
                                {
                                    if (internalAbilities.getGemState()
                                            && !player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()
                                            && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ObjHandler.gemHelmet)
                                    {
                                        ((GemHelmet) ObjHandler.gemHelmet).doZap(player);
                                        return;
                                    }
                                }
                                break;
                            case MODE:
                                if (!stack.isEmpty()
                                        && stack.getItem() instanceof IModeChanger
                                        && ((IModeChanger) stack.getItem()).changeMode(player, stack, hand))
                                {
                                    return;
                                }
                                break;
                            case ARMOR_TOGGLE:
                        }

                    }

                }
            });
			return null;
		}
	}
}
