package main.java.quartzshard.projecttweaked;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import main.java.quartzshard.projecttweaked.emc.EMCMapper;
import main.java.quartzshard.projecttweaked.handlers.InternalAbilities;
import main.java.quartzshard.projecttweaked.handlers.InternalTimers;
import main.java.quartzshard.projecttweaked.integration.jei.PEJeiPlugin;
import main.java.quartzshard.projecttweaked.playerData.Transmutation;
import main.java.quartzshard.projecttweaked.config.CustomEMCParser;
import main.java.quartzshard.projecttweaked.config.NBTWhitelistParser;
import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import main.java.quartzshard.projecttweaked.fixes.CapInventoryWalker;
import main.java.quartzshard.projecttweaked.fixes.TENameFix;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import main.java.quartzshard.projecttweaked.gameObjs.tiles.*;
import main.java.quartzshard.projecttweaked.impl.AlchBagImpl;
import main.java.quartzshard.projecttweaked.impl.IMCHandler;
import main.java.quartzshard.projecttweaked.impl.KnowledgeImpl;
import main.java.quartzshard.projecttweaked.impl.TransmutationOffline;
import main.java.quartzshard.projecttweaked.network.PacketHandler;
import main.java.quartzshard.projecttweaked.network.ThreadCheckUUID;
import main.java.quartzshard.projecttweaked.network.commands.ProjectTwEakedCMD;
import main.java.quartzshard.projecttweaked.proxies.IProxy;
import main.java.quartzshard.projecttweaked.utils.DummyIStorage;
import main.java.quartzshard.projecttweaked.utils.GuiHandler;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mod(modid = PECore.MODID, name = PECore.MODNAME, version = PECore.VERSION, acceptedMinecraftVersions = "[1.12,]", dependencies = PECore.DEPS, updateJSON = PECore.UPDATE_JSON)
@Mod.EventBusSubscriber(modid = PECore.MODID)
public class PECore
{
	public static final String MODID = "projecte";
	public static final String MODNAME = "ProjectTwEaked";
	public static final String VERSION = "@VERSION@";
	public static final String DEPS = "required-after:forge@[13.20.0.2253,);after:baubles@[1.3.3,);after:jei@[4.6.0,)";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/sinkillerj/ProjectTwEaked/mc1.12.x/update.json";
	public static final GameProfile FAKEPLAYER_GAMEPROFILE = new GameProfile(UUID.fromString("590e39c7-9fb6-471b-a4c2-c0e539b2423d"), "[" + MODNAME + "]");
	public static final int DATA_VERSION = 1;
	public static File CONFIG_DIR;
	public static File PREGENERATED_EMC_FILE;
	public static boolean DEV_ENVIRONMENT;
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	@Instance(MODID)
	public static PECore instance;
	
	@SidedProxy(clientSide = "main.java.quartzshard.projecttweaked.proxies.ClientProxy", serverSide = "main.java.quartzshard.projecttweaked.proxies.ServerProxy")
	public static IProxy proxy;

	public static final List<String> uuids = new ArrayList<>();

	public static void debugLog(String msg, Object... args)
	{
		if (DEV_ENVIRONMENT || ProjectTwEakedConfig.misc.debugLogging)
		{
			LOGGER.info(msg, args);
		} else
		{
			LOGGER.debug(msg, args);
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		DEV_ENVIRONMENT = ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"));

		CONFIG_DIR = new File(event.getModConfigurationDirectory(), MODNAME);
		
		if (!CONFIG_DIR.exists())
		{
			CONFIG_DIR.mkdirs();
		}

		PREGENERATED_EMC_FILE = new File(CONFIG_DIR, "pregenerated_emc.json");

		PacketHandler.register();

		AlchBagImpl.init();
		KnowledgeImpl.init();
		CapabilityManager.INSTANCE.register(InternalTimers.class, new DummyIStorage<>(), InternalTimers::new);
		CapabilityManager.INSTANCE.register(InternalAbilities.class, new DummyIStorage<>(), () -> new InternalAbilities(null));
		
		NetworkRegistry.INSTANCE.registerGuiHandler(PECore.instance, new GuiHandler());

		proxy.registerKeyBinds();
		ObjHandler.register();

		proxy.registerRenderers();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerLayerRenderers();

		CompoundDataFixer fixer = FMLCommonHandler.instance().getDataFixer();
		ModFixs modFixer = fixer.init(MODID, DATA_VERSION);

		// Fixers
		modFixer.registerFix(FixTypes.BLOCK_ENTITY, new TENameFix());

		// Walkers
		// These two do not have extra layer of indirection so can use the vanilla walker
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(AlchChestTile.class, "Items"));
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(DMPedestalTile.class, "Items"));

		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new CapInventoryWalker(
				ImmutableSet.of(CollectorMK1Tile.class, CollectorMK2Tile.class, CollectorMK3Tile.class),
				"Input", "AuxSlots"));
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new CapInventoryWalker(CondenserTile.class, "Input", "LockSlot"));
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new CapInventoryWalker(CondenserMK2Tile.class, "Input", "LockSlot", "Output"));
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new CapInventoryWalker(
				ImmutableSet.of(DMFurnaceTile.class, RMFurnaceTile.class),
				"Input", "Output", "Fuel"));
		fixer.registerWalker(FixTypes.BLOCK_ENTITY, new CapInventoryWalker(
				ImmutableSet.of(RelayMK1Tile.class, RelayMK2Tile.class, RelayMK3Tile.class),
				"Input", "Output"));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		NBTWhitelistParser.init();
		proxy.initializeManual();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new ProjectTwEakedCMD());

		if (!ThreadCheckUUID.hasRunServer())
		{
			new ThreadCheckUUID(true).start();
		}

		long start = System.currentTimeMillis();

		CustomEMCParser.init();

		LOGGER.info("Starting server-side EMC mapping.");

		EMCMapper.map();

		LOGGER.info("Registered " + EMCMapper.emc.size() + " EMC values. (took " + (System.currentTimeMillis() - start) + " ms)");
	}

	@Mod.EventHandler
	public void serverStopping (FMLServerStoppingEvent event)
	{
		TransmutationOffline.cleanAll();
	}
	
	@Mod.EventHandler
	public void serverQuit(FMLServerStoppedEvent event)
	{
		Transmutation.clearCache();
		EMCMapper.clearMaps();
	}

	@Mod.EventHandler
	public void onIMCMessage(FMLInterModComms.IMCEvent event)
	{
		for (FMLInterModComms.IMCMessage msg : event.getMessages())
		{
			IMCHandler.handleIMC(msg);
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(MODID))
		{
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}

	public static void refreshJEI()
	{
		if (Loader.isModLoaded("jei"))
		{
			PEJeiPlugin.refresh();
		}
	}
}
