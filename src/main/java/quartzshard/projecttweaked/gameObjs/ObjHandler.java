package quartzshard.projecttweaked.gameObjs;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.gameObjs.blocks.AlchemicalChest;
import quartzshard.projecttweaked.gameObjs.blocks.Collector;
import quartzshard.projecttweaked.gameObjs.blocks.Condenser;
import quartzshard.projecttweaked.gameObjs.blocks.CondenserMK2;
import quartzshard.projecttweaked.gameObjs.blocks.FuelBlock;
import quartzshard.projecttweaked.gameObjs.blocks.InterdictionTorch;
import quartzshard.projecttweaked.gameObjs.blocks.MatterBlock;
import quartzshard.projecttweaked.gameObjs.blocks.MatterFurnace;
import quartzshard.projecttweaked.gameObjs.blocks.NovaCataclysm;
import quartzshard.projecttweaked.gameObjs.blocks.NovaCatalyst;
import quartzshard.projecttweaked.gameObjs.blocks.Pedestal;
import quartzshard.projecttweaked.gameObjs.blocks.Relay;
import quartzshard.projecttweaked.gameObjs.blocks.TransmutationStone;
import quartzshard.projecttweaked.gameObjs.customRecipes.RecipeShapelessHidden;
import quartzshard.projecttweaked.gameObjs.customRecipes.RecipeShapelessKleinStar;
import quartzshard.projecttweaked.gameObjs.customRecipes.RecipesCovalenceRepair;
import quartzshard.projecttweaked.gameObjs.entity.EntityFireProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityHomingArrow;
import quartzshard.projecttweaked.gameObjs.entity.EntityLavaProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityLensProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityMobRandomizer;
import quartzshard.projecttweaked.gameObjs.entity.EntityNovaCataclysmPrimed;
import quartzshard.projecttweaked.gameObjs.entity.EntityNovaCatalystPrimed;
import quartzshard.projecttweaked.gameObjs.entity.EntitySWRGProjectile;
import quartzshard.projecttweaked.gameObjs.entity.EntityWaterProjectile;
import quartzshard.projecttweaked.gameObjs.items.AlchemicalBag;
import quartzshard.projecttweaked.gameObjs.items.AlchemicalFuel;
import quartzshard.projecttweaked.gameObjs.items.CataliticLens;
import quartzshard.projecttweaked.gameObjs.items.CovalenceDust;
import quartzshard.projecttweaked.gameObjs.items.DestructionCatalyst;
import quartzshard.projecttweaked.gameObjs.items.DiviningRod;
import quartzshard.projecttweaked.gameObjs.items.EvertideAmulet;
import quartzshard.projecttweaked.gameObjs.items.GemEternalDensity;
import quartzshard.projecttweaked.gameObjs.items.HyperkineticLens;
import quartzshard.projecttweaked.gameObjs.items.ItemPE;
import quartzshard.projecttweaked.gameObjs.items.KleinStar;
import quartzshard.projecttweaked.gameObjs.items.Matter;
import quartzshard.projecttweaked.gameObjs.items.MercurialEye;
import quartzshard.projecttweaked.gameObjs.items.PEManual;
import quartzshard.projecttweaked.gameObjs.items.PhilosophersStone;
import quartzshard.projecttweaked.gameObjs.items.RepairTalisman;
import quartzshard.projecttweaked.gameObjs.items.TimeWatch;
import quartzshard.projecttweaked.gameObjs.items.Tome;
import quartzshard.projecttweaked.gameObjs.items.TransmutationTablet;
import quartzshard.projecttweaked.gameObjs.items.VolcaniteAmulet;
import quartzshard.projecttweaked.gameObjs.items.armor.DMArmor;
import quartzshard.projecttweaked.gameObjs.items.armor.GemChest;
import quartzshard.projecttweaked.gameObjs.items.armor.GemFeet;
import quartzshard.projecttweaked.gameObjs.items.armor.GemHelmet;
import quartzshard.projecttweaked.gameObjs.items.armor.GemLegs;
import quartzshard.projecttweaked.gameObjs.items.armor.RMArmor;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemAlchemyChestBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemCollectorBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemCondenserBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemDMFurnaceBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemFuelBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemMatterBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemRMFurnaceBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemRelayBlock;
import quartzshard.projecttweaked.gameObjs.items.itemBlocks.ItemTransmutationBlock;
import quartzshard.projecttweaked.gameObjs.items.rings.Arcana;
import quartzshard.projecttweaked.gameObjs.items.rings.ArchangelSmite;
import quartzshard.projecttweaked.gameObjs.items.rings.BlackHoleBand;
import quartzshard.projecttweaked.gameObjs.items.rings.BodyStone;
import quartzshard.projecttweaked.gameObjs.items.rings.HarvestGoddess;
import quartzshard.projecttweaked.gameObjs.items.rings.Ignition;
import quartzshard.projecttweaked.gameObjs.items.rings.LifeStone;
import quartzshard.projecttweaked.gameObjs.items.rings.MindStone;
import quartzshard.projecttweaked.gameObjs.items.rings.SWRG;
import quartzshard.projecttweaked.gameObjs.items.rings.SoulStone;
import quartzshard.projecttweaked.gameObjs.items.rings.VoidRing;
import quartzshard.projecttweaked.gameObjs.items.rings.Zero;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkAxe;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkHammer;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkHoe;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkPick;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkShears;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkShovel;
import quartzshard.projecttweaked.gameObjs.items.tools.DarkSword;
import quartzshard.projecttweaked.gameObjs.items.tools.RedAxe;
import quartzshard.projecttweaked.gameObjs.items.tools.RedHammer;
import quartzshard.projecttweaked.gameObjs.items.tools.RedHoe;
import quartzshard.projecttweaked.gameObjs.items.tools.RedKatar;
import quartzshard.projecttweaked.gameObjs.items.tools.RedPick;
import quartzshard.projecttweaked.gameObjs.items.tools.RedShears;
import quartzshard.projecttweaked.gameObjs.items.tools.RedShovel;
import quartzshard.projecttweaked.gameObjs.items.tools.RedStar;
import quartzshard.projecttweaked.gameObjs.items.tools.RedSword;
import quartzshard.projecttweaked.gameObjs.tiles.AlchChestTile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK0Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK1Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK3Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK4Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserTile;
import quartzshard.projecttweaked.gameObjs.tiles.DMFurnaceTile;
import quartzshard.projecttweaked.gameObjs.tiles.DMPedestalTile;
import quartzshard.projecttweaked.gameObjs.tiles.InterdictionTile;
import quartzshard.projecttweaked.gameObjs.tiles.RMFurnaceTile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK0Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK1Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK2Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK3Tile;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK4Tile;

@Mod.EventBusSubscriber(modid = PECore.MODID)
public class ObjHandler
{
	public static final CreativeTabs cTab = new CreativeTab();
	public static final Block alchChest = new AlchemicalChest();
	public static final Block interdictionTorch = new InterdictionTorch();
	public static final Block transmuteStone = new TransmutationStone();
	public static final Block condenser = new Condenser();
	public static final Block condenserMk2 = new CondenserMK2();
	public static final Block rmFurnaceOff = new MatterFurnace(false, true);
	public static final Block rmFurnaceOn = new MatterFurnace(true, true);
	public static final Block dmFurnaceOff = new MatterFurnace(false, false);
	public static final Block dmFurnaceOn = new MatterFurnace(true, false);
	public static final Block dmPedestal = new Pedestal();
	public static final Block matterBlock = new MatterBlock();
	public static final Block fuelBlock = new FuelBlock();
	public static final Block collectorMK0 = new Collector(0);
	public static final Block collectorMK1 = new Collector(1);
	public static final Block collectorMK2 = new Collector(2);
	public static final Block collectorMK3 = new Collector(3);
	public static final Block collectorMK4 = new Collector(4);
	public static final Block relayMK0 = new Relay(0);
	public static final Block relay = new Relay(1);
	public static final Block relayMK2 = new Relay(2);
	public static final Block relayMK3 = new Relay(3);
	public static final Block relayMK4 = new Relay(4);
	public static final Block novaCatalyst = new NovaCatalyst();
	public static final Block novaCataclysm = new NovaCataclysm();

	public static final Item philosStone = new PhilosophersStone();
	public static final Item alchBag = new AlchemicalBag();
	public static final Item repairTalisman = new RepairTalisman();
	public static final Item kleinStars = new KleinStar();
	public static final Item fuels = new AlchemicalFuel();
	public static final Item covalence = new CovalenceDust();
	public static final Item matter = new Matter();

	public static final Item dmPick = new DarkPick();
	public static final Item dmAxe = new DarkAxe();
	public static final Item dmShovel = new DarkShovel();
	public static final Item dmSword = new DarkSword();
	public static final Item dmHoe = new DarkHoe();
	public static final Item dmShears = new DarkShears();
	public static final Item dmHammer = new DarkHammer();

	public static final Item rmPick = new RedPick();
	public static final Item rmAxe = new RedAxe();
	public static final Item rmShovel = new RedShovel();
	public static final Item rmSword = new RedSword();
	public static final Item rmHoe = new RedHoe();
	public static final Item rmShears = new RedShears();
	public static final Item rmHammer = new RedHammer();
	public static final Item rmKatar = new RedKatar();
	public static final Item rmStar = new RedStar();

	public static final Item dmHelmet = new DMArmor(EntityEquipmentSlot.HEAD);
	public static final Item dmChest = new DMArmor(EntityEquipmentSlot.CHEST);
	public static final Item dmLegs = new DMArmor(EntityEquipmentSlot.LEGS);
	public static final Item dmFeet = new DMArmor(EntityEquipmentSlot.FEET);

	public static final Item rmHelmet = new RMArmor(EntityEquipmentSlot.HEAD);
	public static final Item rmChest = new RMArmor(EntityEquipmentSlot.CHEST);
	public static final Item rmLegs = new RMArmor(EntityEquipmentSlot.LEGS);
	public static final Item rmFeet = new RMArmor(EntityEquipmentSlot.FEET);

	public static final Item gemHelmet = new GemHelmet();
	public static final Item gemChest = new GemChest();
	public static final Item gemLegs = new GemLegs();
	public static final Item gemFeet = new GemFeet();

	public static final Item ironBand = new ItemPE().setTranslationKey("ring_iron_band");
	public static final Item blackHole = new BlackHoleBand();
	public static final Item angelSmite = new ArchangelSmite();
	public static final Item harvestGod = new HarvestGoddess();
	public static final Item ignition = new Ignition();
	public static final Item zero = new Zero();
	public static final Item swrg = new SWRG();
	public static final Item timeWatch = new TimeWatch();
	public static final Item everTide = new EvertideAmulet();
	public static final Item volcanite = new VolcaniteAmulet();
	public static final Item eternalDensity = new GemEternalDensity();
	public static final Item dRod1 = new DiviningRod(new String[] { "3x3x3" }).setTranslationKey("divining_rod_1");
	public static final Item dRod2 = new DiviningRod(new String[]{ "3x3x3", "16x3x3" }).setTranslationKey("divining_rod_2");
	public static final Item dRod3 = new DiviningRod(new String[] { "3x3x3", "16x3x3", "64x3x3" }).setTranslationKey("divining_rod_3");
	public static final Item mercEye = new MercurialEye();
	public static final Item voidRing = new VoidRing();
	public static final Item arcana = new Arcana();

	public static final Item dCatalyst = new DestructionCatalyst();
	public static final Item hyperLens = new HyperkineticLens();
	public static final Item cataliticLens = new CataliticLens();

	public static final Item bodyStone = new BodyStone();
	public static final Item soulStone = new SoulStone();
	public static final Item mindStone = new MindStone();
	public static final Item lifeStone = new LifeStone();

	public static final Item tome = new Tome();

	public static final Item waterOrb = new Item().setTranslationKey("pe_water_orb");
	public static final Item lavaOrb = new Item().setTranslationKey("pe_lava_orb");
	public static final Item mobRandomizer = new Item().setTranslationKey("pe_randomizer");
	public static final Item lensExplosive = new Item().setTranslationKey("pe_lens_explosive");
	public static final Item fireProjectile = new Item().setTranslationKey("pe_fire_projectile");
	public static final Item windProjectile = new Item().setTranslationKey("pe_wind_projectile");
	public static final Item transmutationTablet = new TransmutationTablet();
	public static final Item manual = new PEManual();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt)
	{
		IForgeRegistry<Block> r = evt.getRegistry();
		registerObj(r, alchChest, "alchemical_chest");
		registerObj(r, collectorMK0, "collector_mk0");
		registerObj(r, collectorMK1, "collector_mk1");
		registerObj(r, collectorMK2, "collector_mk2");
		registerObj(r, collectorMK3, "collector_mk3");
		registerObj(r, collectorMK4, "collector_mk4");
		registerObj(r, condenser, "condenser_mk1");
		registerObj(r, condenserMk2, "condenser_mk2");
		registerObj(r, dmFurnaceOff, "dm_furnace");
		registerObj(r, dmFurnaceOn, "dm_furnace_lit");
		registerObj(r, dmPedestal, "dm_pedestal");
		registerObj(r, fuelBlock, "fuel_block");
		registerObj(r, interdictionTorch, "interdiction_torch");
		registerObj(r, matterBlock, "matter_block");
		registerObj(r, novaCatalyst, "nova_catalyst");
		registerObj(r, novaCataclysm, "nova_cataclysm");
		registerObj(r, relayMK0, "relay_mk0");
		registerObj(r, relay, "relay_mk1");
		registerObj(r, relayMK2, "relay_mk2");
		registerObj(r, relayMK3, "relay_mk3");
		registerObj(r, relayMK4, "relay_mk4");
		registerObj(r, rmFurnaceOff, "rm_furnace");
		registerObj(r, rmFurnaceOn, "rm_furnace_lit");
		registerObj(r, transmuteStone, "transmutation_table");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> evt)
	{
		IForgeRegistry<Item> r = evt.getRegistry();
		registerObj(r, new ItemAlchemyChestBlock(alchChest), alchChest.getRegistryName());
		registerObj(r, new ItemCollectorBlock(collectorMK0), collectorMK0.getRegistryName());
		registerObj(r, new ItemCollectorBlock(collectorMK1), collectorMK1.getRegistryName());
		registerObj(r, new ItemCollectorBlock(collectorMK2), collectorMK2.getRegistryName());
		registerObj(r, new ItemCollectorBlock(collectorMK3), collectorMK3.getRegistryName());
		registerObj(r, new ItemCollectorBlock(collectorMK4), collectorMK4.getRegistryName());
		registerObj(r, new ItemCondenserBlock(condenser), condenser.getRegistryName());
		registerObj(r, new ItemBlock(condenserMk2), condenserMk2.getRegistryName());
		registerObj(r, new ItemDMFurnaceBlock(dmFurnaceOff), dmFurnaceOff.getRegistryName());
		registerObj(r, new ItemBlock(dmPedestal), dmPedestal.getRegistryName());
		registerObj(r, new ItemFuelBlock(fuelBlock), fuelBlock.getRegistryName());
		registerObj(r, new ItemBlock(interdictionTorch), interdictionTorch.getRegistryName());
		registerObj(r, new ItemMatterBlock(matterBlock), matterBlock.getRegistryName());
		registerObj(r, new ItemBlock(novaCatalyst), novaCatalyst.getRegistryName());
		registerObj(r, new ItemBlock(novaCataclysm), novaCataclysm.getRegistryName());
		registerObj(r, new ItemRelayBlock(relayMK0), relayMK0.getRegistryName());
		registerObj(r, new ItemRelayBlock(relay), relay.getRegistryName());
		registerObj(r, new ItemRelayBlock(relayMK2), relayMK2.getRegistryName());
		registerObj(r, new ItemRelayBlock(relayMK3), relayMK3.getRegistryName());
		registerObj(r, new ItemRelayBlock(relayMK4), relayMK4.getRegistryName());
		registerObj(r, new ItemRMFurnaceBlock(rmFurnaceOff), rmFurnaceOff.getRegistryName());
		registerObj(r, new ItemTransmutationBlock(transmuteStone), transmuteStone.getRegistryName());

		registerObj(r, philosStone, philosStone.getTranslationKey());
		registerObj(r, alchBag, alchBag.getTranslationKey());
		registerObj(r, repairTalisman, repairTalisman.getTranslationKey());
		registerObj(r, kleinStars, kleinStars.getTranslationKey());
		registerObj(r, fuels, fuels.getTranslationKey());
		registerObj(r, covalence, covalence.getTranslationKey());
		registerObj(r, matter, matter.getTranslationKey());

		registerObj(r, dmPick, dmPick.getTranslationKey());
		registerObj(r, dmAxe, dmAxe.getTranslationKey());
		registerObj(r, dmShovel, dmShovel.getTranslationKey());
		registerObj(r, dmSword, dmSword.getTranslationKey());
		registerObj(r, dmHoe, dmHoe.getTranslationKey());
		registerObj(r, dmShears, dmShears.getTranslationKey());
		registerObj(r, dmHammer, dmHammer.getTranslationKey());

		registerObj(r, rmPick, rmPick.getTranslationKey());
		registerObj(r, rmAxe, rmAxe.getTranslationKey());
		registerObj(r, rmShovel, rmShovel.getTranslationKey());
		registerObj(r, rmSword, rmSword.getTranslationKey());
		registerObj(r, rmHoe, rmHoe.getTranslationKey());
		registerObj(r, rmShears, rmShears.getTranslationKey());
		registerObj(r, rmHammer, rmHammer.getTranslationKey());
		registerObj(r, rmKatar, rmKatar.getTranslationKey());
		registerObj(r, rmStar, rmStar.getTranslationKey());

		registerObj(r, dmHelmet, dmHelmet.getTranslationKey());
		registerObj(r, dmChest, dmChest.getTranslationKey());
		registerObj(r, dmLegs, dmLegs.getTranslationKey());
		registerObj(r, dmFeet, dmFeet.getTranslationKey());

		registerObj(r, rmHelmet, rmHelmet.getTranslationKey());
		registerObj(r, rmChest, rmChest.getTranslationKey());
		registerObj(r, rmLegs, rmLegs.getTranslationKey());
		registerObj(r, rmFeet, rmFeet.getTranslationKey());

		registerObj(r, gemHelmet, gemHelmet.getTranslationKey());
		registerObj(r, gemChest, gemChest.getTranslationKey());
		registerObj(r, gemLegs, gemLegs.getTranslationKey());
		registerObj(r, gemFeet, gemFeet.getTranslationKey());

		registerObj(r, ironBand, ironBand.getTranslationKey());
		registerObj(r, blackHole, blackHole.getTranslationKey());
		registerObj(r, angelSmite, angelSmite.getTranslationKey());
		registerObj(r, harvestGod, harvestGod.getTranslationKey());
		registerObj(r, ignition, ignition.getTranslationKey());
		registerObj(r, zero, zero.getTranslationKey());
		registerObj(r, swrg, swrg.getTranslationKey());
		registerObj(r, timeWatch, timeWatch.getTranslationKey());
		registerObj(r, eternalDensity, eternalDensity.getTranslationKey());
		registerObj(r, dRod1, dRod1.getTranslationKey());
		registerObj(r, dRod2, dRod2.getTranslationKey());
		registerObj(r, dRod3, dRod3.getTranslationKey());
		registerObj(r, mercEye, mercEye.getTranslationKey());
		registerObj(r, voidRing, voidRing.getTranslationKey());
		registerObj(r, arcana, arcana.getTranslationKey());

		registerObj(r, bodyStone, bodyStone.getTranslationKey());
		registerObj(r, soulStone, soulStone.getTranslationKey());
		registerObj(r, mindStone, mindStone.getTranslationKey());
		registerObj(r, lifeStone, lifeStone.getTranslationKey());

		registerObj(r, everTide, everTide.getTranslationKey());
		registerObj(r, volcanite, volcanite.getTranslationKey());

		registerObj(r, waterOrb, waterOrb.getTranslationKey());
		registerObj(r, lavaOrb, lavaOrb.getTranslationKey());
		registerObj(r, mobRandomizer, mobRandomizer.getTranslationKey());
		registerObj(r, lensExplosive, lensExplosive.getTranslationKey());
		registerObj(r, fireProjectile, fireProjectile.getTranslationKey());
		registerObj(r, windProjectile, windProjectile.getTranslationKey());

		registerObj(r, dCatalyst, dCatalyst.getTranslationKey());
		registerObj(r, hyperLens, hyperLens.getTranslationKey());
		registerObj(r, cataliticLens, cataliticLens.getTranslationKey());

		registerObj(r, tome, tome.getTranslationKey());
		registerObj(r, transmutationTablet, transmutationTablet.getTranslationKey());
		registerObj(r, manual, manual.getTranslationKey());
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> evt)
	{
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityWaterProjectile.class)
				.id(new ResourceLocation(PECore.MODID, "water_projectile"), 1)
				.name("WaterProjectile")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityLavaProjectile.class)
				.id(new ResourceLocation(PECore.MODID, "lava_projectile"), 2)
				.name("LavaProjectile")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityMobRandomizer.class)
				.id(new ResourceLocation(PECore.MODID, "mob_randomizer"), 4)
				.name("MobRandomizer")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityLensProjectile.class)
				.id(new ResourceLocation(PECore.MODID, "lens_projectile"), 5)
				.name("LensProjectile")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityNovaCatalystPrimed.class)
				.id(new ResourceLocation(PECore.MODID, "nova_catalyst_primed"), 6)
				.name("NovaCatalystPrimed")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityNovaCataclysmPrimed.class)
				.id(new ResourceLocation(PECore.MODID, "nova_cataclysm_primed"), 7)
				.name("NovaCataclysmPrimed")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityHomingArrow.class)
				.id(new ResourceLocation(PECore.MODID, "homing_arrow"), 8)
				.name("HomingArrow")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntityFireProjectile.class)
				.id(new ResourceLocation(PECore.MODID, "fire_projectile"), 9)
				.name("FireProjectile")
				.tracker(256, 10, true).build());
		evt.getRegistry().register(EntityEntryBuilder.create()
				.entity(EntitySWRGProjectile.class)
				.id(new ResourceLocation(PECore.MODID, "swrg_projectile"), 10)
				.name("LightningProjectile")
				.tracker(256, 10, true).build());
	}

	public static void register()
	{
		//Tile Entities
		GameRegistry.registerTileEntity(AlchChestTile.class, new ResourceLocation(PECore.MODID, "alchemical_chest"));
		GameRegistry.registerTileEntity(InterdictionTile.class, new ResourceLocation(PECore.MODID, "interdiction_torch"));
		GameRegistry.registerTileEntity(CondenserTile.class, new ResourceLocation(PECore.MODID, "condenser"));
		GameRegistry.registerTileEntity(CondenserMK2Tile.class, new ResourceLocation(PECore.MODID, "condenser_mk2"));
		GameRegistry.registerTileEntity(RMFurnaceTile.class, new ResourceLocation(PECore.MODID, "rm_furnace"));
		GameRegistry.registerTileEntity(DMFurnaceTile.class, new ResourceLocation(PECore.MODID, "dm_furnace"));
		GameRegistry.registerTileEntity(CollectorMK0Tile.class, new ResourceLocation(PECore.MODID, "collector_mk0"));
		GameRegistry.registerTileEntity(CollectorMK1Tile.class, new ResourceLocation(PECore.MODID, "collector_mk1"));
		GameRegistry.registerTileEntity(CollectorMK2Tile.class, new ResourceLocation(PECore.MODID, "collector_mk2"));
		GameRegistry.registerTileEntity(CollectorMK3Tile.class, new ResourceLocation(PECore.MODID, "collector_mk3"));
		GameRegistry.registerTileEntity(CollectorMK4Tile.class, new ResourceLocation(PECore.MODID, "collector_mk4"));
		GameRegistry.registerTileEntity(RelayMK0Tile.class, new ResourceLocation(PECore.MODID, "relay_mk0"));
		GameRegistry.registerTileEntity(RelayMK1Tile.class, new ResourceLocation(PECore.MODID, "relay_mk1"));
		GameRegistry.registerTileEntity(RelayMK2Tile.class, new ResourceLocation(PECore.MODID, "relay_mk2"));
		GameRegistry.registerTileEntity(RelayMK3Tile.class, new ResourceLocation(PECore.MODID, "relay_mk3"));
		GameRegistry.registerTileEntity(RelayMK4Tile.class, new ResourceLocation(PECore.MODID, "relay_mk4"));
		GameRegistry.registerTileEntity(DMPedestalTile.class, new ResourceLocation(PECore.MODID, "dm_pedestal"));
	}

	@SubscribeEvent
	public static void addRecipes(RegistryEvent.Register<IRecipe> evt)
	{
		//Klein Stars
		for (int i = 1; i < 6; i++)
		{
			ItemStack input = new ItemStack(kleinStars, 1, i - 1);
			ItemStack output = new ItemStack(kleinStars, 1, i);
			RecipeShapelessKleinStar recipe = new RecipeShapelessKleinStar(PECore.MODID + ":klein", output, toIngredients(input, input, input, input));
			recipe.setRegistryName(PECore.MODID, String.format("klein_%d_to_%d", i - 1, i));
			evt.getRegistry().register(recipe);
		}

		evt.getRegistry().register(new RecipesCovalenceRepair().setRegistryName(PECore.MODID, "covalence_repair"));

		// RecipeSorter.register("Covalence Repair Recipes", RecipesCovalenceRepair.class, Category.SHAPELESS, "before:minecraft:shaped");
		// RecipeSorter.register("", RecipeShapedKleinStar.class, Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
		// RecipeSorter.register("", RecipeShapelessHidden.class, Category.SHAPELESS, "before:minecraft:shaped");

		registerPhiloStoneSmelting(evt.getRegistry());
	}
	
	private static <V extends IForgeRegistryEntry<V>> void registerObj(IForgeRegistry<V> registry, IForgeRegistryEntry<V> o, String name)
	{
		registerObj(registry, o, new ResourceLocation(PECore.MODID, name));
	}

	private static <V extends IForgeRegistryEntry<V>> void registerObj(IForgeRegistry<V> registry, IForgeRegistryEntry<V> o, ResourceLocation name)
	{
		registry.register(o.setRegistryName(name));
	}

	private static NonNullList<Ingredient> toIngredients(ItemStack... stacks) {
		NonNullList<Ingredient> ingr = NonNullList.create();
		for (ItemStack stack : stacks) {
			ingr.add(Ingredient.fromStacks(stack));
		}
		return ingr;
	}

	private static void registerPhiloStoneSmelting(IForgeRegistry<IRecipe> registry)
	{
		for (Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet())
		{
			if (entry.getKey().isEmpty() || entry.getValue().isEmpty())
			{
				continue;
			}

			ItemStack input = entry.getKey();
			ItemStack output = entry.getValue().copy();
			output.setCount(output.getCount() * 7);

			String inputName = input.getItem().getRegistryName().toString().replace(':', '_')+ "_" + input.getItemDamage();
			ResourceLocation recipeName = new ResourceLocation(PECore.MODID, "philstone_smelt_" + inputName);
			registry.register(new RecipeShapelessHidden("", output,
									toIngredients(new ItemStack(philosStone), input, input, input, input, input, input, input, new ItemStack(Items.COAL, 1, OreDictionary.WILDCARD_VALUE)))
								.setRegistryName(recipeName));
		}
		// RecipeSorter.register("Philosopher's Smelting Recipes", RecipeShapelessHidden.class, Category.SHAPELESS, "before:minecraft:shaped");
	}
}
