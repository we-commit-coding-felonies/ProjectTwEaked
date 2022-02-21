package moze_intel.projecte.events;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.item.IAlchShield;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.AlchemicalBag;
import moze_intel.projecte.gameObjs.items.armor.GemArmorBase;
import moze_intel.projecte.handlers.InternalAbilities;
import moze_intel.projecte.handlers.InternalTimers;
import moze_intel.projecte.impl.AlchBagImpl;
import moze_intel.projecte.impl.KnowledgeImpl;
import moze_intel.projecte.impl.TransmutationOffline;
import moze_intel.projecte.network.PacketHandler;
import moze_intel.projecte.network.packets.CheckUpdatePKT;
import moze_intel.projecte.network.packets.SyncCovalencePKT;
import moze_intel.projecte.utils.ItemHelper;
import moze_intel.projecte.utils.PlayerHelper;
import moze_intel.projecte.utils.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

@Mod.EventBusSubscriber(modid = PECore.MODID)
public class PlayerEvents
{
	// On death or return from end, copy the capability data
	@SubscribeEvent
	public static void cloneEvent(PlayerEvent.Clone evt)
	{
		NBTTagCompound bags = evt.getOriginal().getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).serializeNBT();
		evt.getEntityPlayer().getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).deserializeNBT(bags);

		NBTTagCompound knowledge = evt.getOriginal().getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null).serializeNBT();
		evt.getEntityPlayer().getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null).deserializeNBT(knowledge);
	}

	// On death or return from end, sync to the client
	@SubscribeEvent
	public static void respawnEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent evt)
	{
		evt.player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null).sync((EntityPlayerMP) evt.player);
		evt.player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).sync(null, (EntityPlayerMP) evt.player);
	}

	@SubscribeEvent
	public static void playerChangeDimension(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event)
	{
		// Sync to the client for "normal" interdimensional teleports (nether portal, etc.)
		event.player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null).sync((EntityPlayerMP) event.player);
		event.player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).sync(null, (EntityPlayerMP) event.player);

		event.player.getCapability(InternalAbilities.CAPABILITY, null).onDimensionChange();
	}

	@SubscribeEvent
	public static void attachCaps(AttachCapabilitiesEvent<Entity> evt)
	{
		if (evt.getObject() instanceof EntityPlayer)
		{
			evt.addCapability(AlchBagImpl.Provider.NAME, new AlchBagImpl.Provider());
			evt.addCapability(KnowledgeImpl.Provider.NAME, new KnowledgeImpl.Provider((EntityPlayer) evt.getObject()));

			if (evt.getObject() instanceof EntityPlayerMP)
			{
				evt.addCapability(InternalTimers.NAME, new InternalTimers.Provider());
				evt.addCapability(InternalAbilities.NAME, new InternalAbilities.Provider((EntityPlayerMP) evt.getObject()));
			}
		}
	}

	@SubscribeEvent
	public static void playerConnect(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
	{
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		PacketHandler.sendFragmentedEmcPacket(player);

		PacketHandler.sendTo(new CheckUpdatePKT(), player);

		IKnowledgeProvider knowledge = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
		knowledge.sync(player);
		PlayerHelper.updateScore(player, PlayerHelper.SCOREBOARD_EMC, MathHelper.floor(knowledge.getEmc()));

		player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null).sync(null, player);

		PacketHandler.sendTo(new SyncCovalencePKT(ProjectEConfig.difficulty.covalenceLoss, ProjectEConfig.difficulty.covalenceLossRounding), player);

		PECore.debugLog("Sent knowledge and bag data to {}", player.getName());
	}

	@SubscribeEvent
	public static void onConstruct(EntityEvent.EntityConstructing evt)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER // No world to check yet
			&& evt.getEntity() instanceof EntityPlayer && !(evt.getEntity() instanceof FakePlayer))
		{
			TransmutationOffline.clear(evt.getEntity().getUniqueID());
			PECore.debugLog("Clearing offline data cache in preparation to load online data");
		}
	}

	@SubscribeEvent
	public static void onHighAlchemistJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent evt)
	{
		if (PECore.uuids.contains((evt.player.getUniqueID().toString())))
		{
			ITextComponent prior = new TextComponentTranslation("pe.server.high_alchemist").setStyle(new Style().setColor(TextFormatting.BLUE));
			ITextComponent playername = new TextComponentString(" " + evt.player.getName() + " ").setStyle(new Style().setColor(TextFormatting.GOLD));
			ITextComponent latter = new TextComponentTranslation("pe.server.has_joined").setStyle(new Style().setColor(TextFormatting.BLUE));
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(prior.appendSibling(playername).appendSibling(latter));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void pickupItem(EntityItemPickupEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();
		
		if (world.isRemote)
		{
			return;
		}

		ItemStack bag = AlchemicalBag.getFirstBagWithSuctionItem(player, player.inventory.mainInventory);

		if (bag.isEmpty())
		{
			return;
		}

		IItemHandler handler = player.getCapability(ProjectEAPI.ALCH_BAG_CAPABILITY, null)
				.getBag(EnumDyeColor.byMetadata(bag.getItemDamage()));
		ItemStack remainder = ItemHandlerHelper.insertItemStacked(handler, event.getItem().getItem(), false);

		if (remainder.isEmpty())
		{
			event.getItem().setDead();
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			((EntityPlayerMP) player).connection.sendPacket(new SPacketCollectItem(event.getItem().getEntityId(), player.getEntityId(), 1));
		}
		else
		{
			event.getItem().setItem(remainder);
		}

		event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onPlayerAttacked(LivingAttackEvent event)
	{
		if (!(event.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer)event.getEntity();
		ItemStack offhand = player.getHeldItemOffhand();
		IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		Iterable<ItemStack> armour = player.getArmorInventoryList();
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (ItemStack stack : armour){
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof IAlchShield)
			{
				((IAlchShield) stack.getItem()).onPlayerAttacked(event, 0, stack);
				if (event.isCanceled()) return;
			}
		}
		for (int i = 0; i < baubles.getSlots(); i++)
		{
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof IAlchShield)
			{
				((IAlchShield) stack.getItem()).onPlayerAttacked(event, 0, stack);
				if (event.isCanceled()) return;
			}
		}
		if (!offhand.isEmpty() && offhand.getItem() instanceof IAlchShield)
		{
			((IAlchShield) offhand.getItem()).onPlayerAttacked(event, 0, offhand);
			if (event.isCanceled()) return;
		}
		
		for (int i = 0; i < inv.getSlots(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) continue;
			if (stack.getItem() instanceof IAlchShield)
			{
				((IAlchShield) stack.getItem()).onPlayerAttacked(event, i, stack);
				if (event.isCanceled()) return;
			}
		}
		return;
	}

	@SubscribeEvent
	public static void onPlayerEquipmentUpdate(LivingEquipmentChangeEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer ent = (EntityPlayer) event.getEntity();
			NBTTagCompound entData = event.getEntity().getEntityData();
			if (event.getFrom().getItem() instanceof GemArmorBase && (event.getTo().isEmpty() || event.getFrom().getMaxDamage() <= event.getFrom().getItemDamage()) && entData.getByte("pe_gem_num_replacements") > 0) {
				ItemStack newStack;
				int slot;
				switch (event.getSlot()) {
					case HEAD:
						newStack = new ItemStack(ObjHandler.rmHelmet);
						slot = 39;
						break;
					case CHEST:
						newStack = new ItemStack(ObjHandler.rmChest);
						slot = 38;
						break;
					case LEGS:
						newStack = new ItemStack(ObjHandler.rmLegs);
						slot = 37;
						break;
					case FEET:
						newStack = new ItemStack(ObjHandler.rmFeet);
						slot = 36;
						break;
					default:
						return;
				}
				int rmDura = ProjectEConfig.matterArmors.rmArmorDurability;
				if (ProjectEConfig.matterArmors.rmBurnout) {ItemHelper.getOrCreateCompound(newStack).setInteger("pe_wear", Math.max(rmDura / 10, event.getEntity().world.rand.nextInt(((rmDura / 10) * 3) + 1)));} else {newStack.setItemDamage(Math.max(rmDura / 10, event.getEntity().world.rand.nextInt(((rmDura / 10) * 3) + 1)));}
				InvWrapper playerInv = new InvWrapper(ent.inventory);
				ent.renderBrokenItemStack(event.getFrom());
				if (ProjectEConfig.matterArmors.gemBreakExplosion) {WorldHelper.createNovaExplosion(ent.world, ent, ent.posX, ent.posY, ent.posZ, (float) (ProjectEConfig.matterArmors.gemBreakExplosionPower));}
				playerInv.setStackInSlot(slot, newStack);
				entData.setByte("pe_gem_num_replacements", (byte) (entData.getByte("pe_gem_num_replacements") - 1));
			}
		}
	}
}
