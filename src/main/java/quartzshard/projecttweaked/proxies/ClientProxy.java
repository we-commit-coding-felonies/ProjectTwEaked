package quartzshard.projecttweaked.proxies;

import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import quartzshard.projecttweaked.api.capabilities.IAlchBagProvider;
import quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import quartzshard.projecttweaked.api.state.PEStateProps;
import quartzshard.projecttweaked.api.state.enums.EnumFuelType;
import quartzshard.projecttweaked.api.state.enums.EnumMatterType;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.items.ItemPE;
import quartzshard.projecttweaked.gameObjs.items.KleinStar;
import quartzshard.projecttweaked.gameObjs.blocks.NovaCataclysm;
import quartzshard.projecttweaked.gameObjs.blocks.NovaCatalyst;
import quartzshard.projecttweaked.gameObjs.entity.EntityFireProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityHomingArrow;
import quartzshard.projecttweaked.gameObjs.entity.EntityLavaProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityLensProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityMobRandomizer;
import quartzshard.projecttweaked.gameObjs.entity.EntityNovaCataclysmPrimed;
import quartzshard.projecttweaked.gameObjs.entity.EntityNovaCatalystPrimed;
import quartzshard.projecttweaked.gameObjs.entity.EntitySWRGProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityWaterProjectile;
import quartzshard.projecttweaked.gameObjs.sound.MovingSoundSWRG;
import quartzshard.projecttweaked.gameObjs.tiles.AlchChestTile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserTile;
import quartzshard.projecttweaked.gameObjs.tiles.DMPedestalTile;
import quartzshard.projecttweaked.manual.ManualPageHandler;
import quartzshard.projecttweaked.rendering.ChestRenderer;
import quartzshard.projecttweaked.rendering.CondenserMK2Renderer;
import quartzshard.projecttweaked.rendering.CondenserRenderer;
import quartzshard.projecttweaked.rendering.LayerYue;
import quartzshard.projecttweaked.rendering.NovaCataclysmRenderer;
import quartzshard.projecttweaked.rendering.NovaCatalystRenderer;
import quartzshard.projecttweaked.rendering.PedestalRenderer;
import quartzshard.projecttweaked.utils.ClientKeyHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = PECore.MODID)
public class ClientProxy implements IProxy
{
	// These three following methods are here to prevent a strange crash in the dedicated server whenever packets are received
	// and the wrapped methods are called directly.

	@Override
	public void clearClientKnowledge()
	{
		FMLClientHandler.instance().getClientPlayerEntity().getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null).clearKnowledge();
	}

	@Override
	public IKnowledgeProvider getClientTransmutationProps()
	{
		return FMLClientHandler.instance().getClientPlayerEntity().getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null);
	}

	@Override
	public IAlchBagProvider getClientBagProps()
	{
		return FMLClientHandler.instance().getClientPlayerEntity().getCapability(ProjectTwEakedAPI.ALCH_BAG_CAPABILITY, null);
	}

	@Override
	public void registerKeyBinds()
	{
		ClientKeyHelper.registerMCBindings();
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt)
	{
		// Blocks with special needs
		ModelLoader.setCustomStateMapper(
				ObjHandler.novaCatalyst,
				(new StateMap.Builder()).ignore(NovaCatalyst.EXPLODE).build()
		);

		ModelLoader.setCustomStateMapper(
				ObjHandler.novaCataclysm,
				(new StateMap.Builder()).ignore(NovaCataclysm.EXPLODE).build()
		);

		ModelLoader.setCustomStateMapper(
				ObjHandler.alchChest,
				(new StateMap.Builder()).ignore(PEStateProps.FACING).build()
		);

		ModelLoader.setCustomStateMapper(
				ObjHandler.condenser,
				(new StateMap.Builder()).ignore(PEStateProps.FACING).build()
		);

		ModelLoader.setCustomStateMapper(
				ObjHandler.condenserMk2,
				(new StateMap.Builder()).ignore(PEStateProps.FACING).build()
		);

		// Items that have different properties or textures per meta value.
		registerCovalenceDust();
		registerBags();
		registerFuels();
		registerMatter();
		registerKlein();
		registerPowerItems();

		// Normal items that have no variants / meta values. The json models are named "item.pe_<name>" because we register items with unlocal name.
		// Which was a dumb decision made by somebody way back when. Oh well.
		registerItem(ObjHandler.bodyStone);
		registerItem(ObjHandler.soulStone);
		registerItem(ObjHandler.mindStone);
		registerItem(ObjHandler.lifeStone);
		registerItem(ObjHandler.blackHole);
		registerItem(ObjHandler.harvestGod);
		registerItem(ObjHandler.eternalDensity);
		registerItem(ObjHandler.timeWatch);
		registerItem(ObjHandler.ignition);
		registerItem(ObjHandler.zero);
		registerItem(ObjHandler.voidRing);
		registerItem(ObjHandler.waterOrb);
		registerItem(ObjHandler.lavaOrb);
		registerItem(ObjHandler.mobRandomizer);
		registerItem(ObjHandler.lensExplosive);
		registerItem(ObjHandler.windProjectile);
		registerItem(ObjHandler.fireProjectile);

		registerItem(ObjHandler.philosStone);
		registerItem(ObjHandler.repairTalisman);
		registerItem(ObjHandler.ironBand);
		registerItem(ObjHandler.dCatalyst);
		registerItem(ObjHandler.hyperLens);
		registerItem(ObjHandler.cataliticLens);
		registerItem(ObjHandler.tome);
		registerItem(ObjHandler.transmutationTablet);
		registerItem(ObjHandler.everTide);
		registerItem(ObjHandler.volcanite);
		registerItem(ObjHandler.dRod1);
		registerItem(ObjHandler.dRod2);
		registerItem(ObjHandler.dRod3);
		registerItem(ObjHandler.angelSmite);
		ModelLoader.setCustomModelResourceLocation(ObjHandler.angelSmite, 1, new ModelResourceLocation(ObjHandler.angelSmite.getRegistryName(), "inventory"));
		registerItem(ObjHandler.mercEye);

		registerItem(ObjHandler.dmPick);
		registerItem(ObjHandler.dmAxe);
		registerItem(ObjHandler.dmShovel);
		registerItem(ObjHandler.dmSword);
		registerItem(ObjHandler.dmHoe);
		registerItem(ObjHandler.dmShears);
		registerItem(ObjHandler.dmHammer);

		registerItem(ObjHandler.dmHelmet);
		registerItem(ObjHandler.dmChest);
		registerItem(ObjHandler.dmLegs);
		registerItem(ObjHandler.dmFeet);

		registerItem(ObjHandler.rmPick);
		registerItem(ObjHandler.rmAxe);
		registerItem(ObjHandler.rmShovel);
		registerItem(ObjHandler.rmSword);
		registerItem(ObjHandler.rmHoe);
		registerItem(ObjHandler.rmShears);
		registerItem(ObjHandler.rmHammer);
		registerItem(ObjHandler.rmKatar);
		registerItem(ObjHandler.rmStar);

		registerItem(ObjHandler.rmHelmet);
		registerItem(ObjHandler.rmChest);
		registerItem(ObjHandler.rmLegs);
		registerItem(ObjHandler.rmFeet);

		registerItem(ObjHandler.gemHelmet);
		registerItem(ObjHandler.gemChest);
		registerItem(ObjHandler.gemLegs);
		registerItem(ObjHandler.gemFeet);

		registerItem(ObjHandler.manual);

		// Item models for blocks
		registerBlock(ObjHandler.alchChest);
		registerBlock(ObjHandler.collectorMK2);
		registerBlock(ObjHandler.collectorMK3);
		registerBlock(ObjHandler.condenser);
		registerBlock(ObjHandler.condenserMk2);
		registerBlock(ObjHandler.interdictionTorch);
		registerBlock(ObjHandler.dmFurnaceOff);
		registerBlock(ObjHandler.dmPedestal);
		registerBlock(ObjHandler.collectorMK1);
		registerBlock(ObjHandler.novaCatalyst);
		registerBlock(ObjHandler.novaCataclysm);
		registerBlock(ObjHandler.relay);
		registerBlock(ObjHandler.relayMK2);
		registerBlock(ObjHandler.relayMK3);
		registerBlock(ObjHandler.rmFurnaceOff);
		registerBlock(ObjHandler.transmuteStone);
		registerBlock(ObjHandler.collectorMK0);
		registerBlock(ObjHandler.collectorMK4);
		registerBlock(ObjHandler.relayMK0);
		registerBlock(ObjHandler.relayMK4);
	}

	private static void registerBlock(Block b)
	{
		String name = ForgeRegistries.BLOCKS.getKey(b).toString();
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(name, "inventory"));
	}

	private static void registerItem(Item i)
	{
		registerItem(i, 0);
	}

	private static void registerItem(Item i, int meta)
	{
		String name = ForgeRegistries.ITEMS.getKey(i).toString();
		ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(name, "inventory"));
	}

	private static void registerCovalenceDust()
	{
		ModelLoader.setCustomModelResourceLocation(ObjHandler.covalence, 0, new ModelResourceLocation(PECore.MODID + ":" + "covalence_low", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.covalence, 1, new ModelResourceLocation(PECore.MODID + ":" + "covalence_medium", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ObjHandler.covalence, 2, new ModelResourceLocation(PECore.MODID + ":" + "covalence_high", "inventory"));
	}

	private static void registerBags()
	{
		for (EnumDyeColor e : EnumDyeColor.values())
		{
			ModelLoader.setCustomModelResourceLocation(ObjHandler.alchBag, e.getMetadata(), new ModelResourceLocation(PECore.MODID + ":" + "bags/alchbag_" + e.getName(), "inventory"));
		}
	}

	private static void registerFuels()
	{
		for (EnumFuelType e : EnumFuelType.values())
		{
			ModelLoader.setCustomModelResourceLocation(ObjHandler.fuels, e.ordinal(), new ModelResourceLocation(PECore.MODID + ":" + e.getName(), "inventory"));

			String name = ForgeRegistries.BLOCKS.getKey(ObjHandler.fuelBlock).toString();
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ObjHandler.fuelBlock), e.ordinal(), new ModelResourceLocation(name, "fueltype=" + e.getName()));
		}
	}

	private static void registerMatter()
	{
		for (EnumMatterType m : EnumMatterType.values())
		{
			ModelLoader.setCustomModelResourceLocation(ObjHandler.matter, m.ordinal(), new ModelResourceLocation(PECore.MODID + ":" + m.getName(), "inventory"));

			String name = ForgeRegistries.BLOCKS.getKey(ObjHandler.matterBlock).toString();
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ObjHandler.matterBlock), m.ordinal(), new ModelResourceLocation(name, "tier=" + m.getName()));
		}
	}

	private static void registerKlein()
	{
		for (KleinStar.EnumKleinTier e : KleinStar.EnumKleinTier.values())
		{
			ModelLoader.setCustomModelResourceLocation(ObjHandler.kleinStars, e.ordinal(), new ModelResourceLocation(PECore.MODID + ":" + "stars/klein_star_" + e.name, "inventory"));
		}
	}

	private static void registerPowerItems()
	{
		ModelResourceLocation off = new ModelResourceLocation(PECore.MODID + ":swrg_off", "inventory");
		ModelResourceLocation fly = new ModelResourceLocation(PECore.MODID + ":swrg_fly", "inventory");
		ModelResourceLocation repel = new ModelResourceLocation(PECore.MODID + ":swrg_repel", "inventory");
		ModelResourceLocation both = new ModelResourceLocation(PECore.MODID + ":swrg_both", "inventory");
		ModelLoader.registerItemVariants(ObjHandler.swrg, off, fly, repel, both);
		ModelLoader.setCustomMeshDefinition(ObjHandler.swrg, stack -> {
			if (stack.hasTagCompound()) {
				switch (stack.getTagCompound().getInteger(ItemPE.TAG_MODE)) {
					default:
					case 0: return off;
					case 1: return fly;
					case 2: return repel;
					case 3: return both;
				}
			}
			return off;
		});

		ModelResourceLocation zero = new ModelResourceLocation(PECore.MODID + ":" + "arcana_zero_off", "inventory");
		ModelResourceLocation ignition = new ModelResourceLocation(PECore.MODID + ":" + "arcana_ignition_off", "inventory");
		ModelResourceLocation harv = new ModelResourceLocation(PECore.MODID + ":" + "arcana_harv_off", "inventory");
		ModelResourceLocation swrg = new ModelResourceLocation(PECore.MODID + ":" + "arcana_swrg_off", "inventory");
		ModelLoader.registerItemVariants(ObjHandler.arcana, zero, ignition, harv, swrg);
		ModelLoader.setCustomMeshDefinition(ObjHandler.arcana, stack -> {
			if (stack.hasTagCompound()) {
				switch (stack.getTagCompound().getByte(ItemPE.TAG_MODE)) {
					default:
					case 0: return zero;
					case 1: return ignition;
					case 2: return harv;
					case 3: return swrg;
				}
			}
			return zero;
		});
	}

	@Override
	public void registerRenderers()
	{
		// Tile Entity
		ClientRegistry.bindTileEntitySpecialRenderer(AlchChestTile.class, new ChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CondenserTile.class, new CondenserRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(CondenserMK2Tile.class, new CondenserMK2Renderer());
		ClientRegistry.bindTileEntitySpecialRenderer(DMPedestalTile.class, new PedestalRenderer());

		//Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterProjectile.class, createRenderFactoryForSnowball(ObjHandler.waterOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityLavaProjectile.class, createRenderFactoryForSnowball(ObjHandler.lavaOrb));
		RenderingRegistry.registerEntityRenderingHandler(EntityMobRandomizer.class, createRenderFactoryForSnowball(ObjHandler.mobRandomizer));
		RenderingRegistry.registerEntityRenderingHandler(EntityLensProjectile.class, createRenderFactoryForSnowball(ObjHandler.lensExplosive));
		RenderingRegistry.registerEntityRenderingHandler(EntityFireProjectile.class, createRenderFactoryForSnowball(ObjHandler.fireProjectile));
		RenderingRegistry.registerEntityRenderingHandler(EntitySWRGProjectile.class, createRenderFactoryForSnowball(ObjHandler.windProjectile));
		RenderingRegistry.registerEntityRenderingHandler(EntityNovaCatalystPrimed.class, NovaCatalystRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNovaCataclysmPrimed.class, NovaCataclysmRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHomingArrow.class, RenderTippedArrow::new);
	}

	@Override
	public void registerLayerRenderers()
	{
		Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
		RenderPlayer render = skinMap.get("default");
		render.addLayer(new LayerYue(render));
		render = skinMap.get("slim");
		render.addLayer(new LayerYue(render));
	}

	private static <T extends Entity> IRenderFactory<T> createRenderFactoryForSnowball(final Item itemToRender)
	{
		return manager -> new RenderSnowball<>(manager, itemToRender, Minecraft.getMinecraft().getRenderItem());
	}

	@Override
	public void initializeManual()
	{
		ManualPageHandler.init();
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	@Override
	public boolean isJumpPressed()
	{
		return FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown();
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof EntitySWRGProjectile && FMLClientHandler.instance().getClient().inGameHasFocus)
		{
			FMLClientHandler.instance().getClient().getSoundHandler().playSound(new MovingSoundSWRG((EntitySWRGProjectile) event.getEntity()));
		}
	}
}

