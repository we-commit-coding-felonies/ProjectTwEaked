package quartzshard.projecttweaked.emc;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.fraction.BigFraction;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.api.event.EMCRemapEvent;
import quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import quartzshard.projecttweaked.emc.arithmetics.HiddenBigFractionArithmetic;
import quartzshard.projecttweaked.emc.arithmetics.IValueArithmetic;
import quartzshard.projecttweaked.emc.collector.DumpToFileCollector;
import quartzshard.projecttweaked.emc.collector.IExtendedMappingCollector;
import quartzshard.projecttweaked.emc.collector.LongToBigFractionCollector;
import quartzshard.projecttweaked.emc.collector.WildcardSetValueFixCollector;
import quartzshard.projecttweaked.emc.generators.BigFractionToLongGenerator;
import quartzshard.projecttweaked.emc.generators.IValueGenerator;
import quartzshard.projecttweaked.emc.json.NSSItem;
import quartzshard.projecttweaked.emc.json.NormalizedSimpleStack;
import quartzshard.projecttweaked.emc.mappers.APICustomConversionMapper;
import quartzshard.projecttweaked.emc.mappers.APICustomEMCMapper;
import quartzshard.projecttweaked.emc.mappers.CraftingMapper;
import quartzshard.projecttweaked.emc.mappers.CustomEMCMapper;
import quartzshard.projecttweaked.emc.mappers.FluidMapper;
import quartzshard.projecttweaked.emc.mappers.IEMCMapper;
import quartzshard.projecttweaked.emc.mappers.OreDictionaryMapper;
import quartzshard.projecttweaked.emc.mappers.SmeltingMapper;
import quartzshard.projecttweaked.emc.mappers.customConversions.CustomConversionMapper;
import quartzshard.projecttweaked.emc.pregenerated.PregeneratedEMC;
import quartzshard.projecttweaked.playerData.Transmutation;
import quartzshard.projecttweaked.utils.PrefixConfiguration;

public final class EMCMapper 
{
	public static final Map<SimpleStack, Long> emc = new LinkedHashMap<>();

	public static double covalenceLoss = ProjectTwEakedConfig.difficulty.covalenceLoss;
	public static boolean covalenceLossRounding = ProjectTwEakedConfig.difficulty.covalenceLossRounding;

	public static void map()
	{
		List<IEMCMapper<NormalizedSimpleStack, Long>> emcMappers = Arrays.asList(
				new OreDictionaryMapper(),
				APICustomEMCMapper.instance,
				new CustomConversionMapper(),
				new CustomEMCMapper(),
				new CraftingMapper(),
				new FluidMapper(),
				new SmeltingMapper(),
				new APICustomConversionMapper()
		);
		SimpleGraphMapper<NormalizedSimpleStack, BigFraction, IValueArithmetic<BigFraction>> mapper = new SimpleGraphMapper<>(new HiddenBigFractionArithmetic());
		IValueGenerator<NormalizedSimpleStack, Long> valueGenerator = new BigFractionToLongGenerator<>(mapper);
		IExtendedMappingCollector<NormalizedSimpleStack, Long, IValueArithmetic<BigFraction>> mappingCollector = new LongToBigFractionCollector<>(mapper);
		mappingCollector = new WildcardSetValueFixCollector<>(mappingCollector);

		Configuration config = new Configuration(new File(PECore.CONFIG_DIR, "mapping.cfg"));
		config.load();

		if (config.getBoolean("dumpEverythingToFile", "general", false,"Want to take a look at the internals of EMC Calculation? Enable this to write all the conversions and setValue-Commands to config/ProjectTwEaked/mappingdump.json")) {
			mappingCollector = new DumpToFileCollector<>(new File(PECore.CONFIG_DIR, "mappingdump.json"), mappingCollector);
		}

		boolean shouldUsePregenerated = config.getBoolean("pregenerate", "general", false, "When the next EMC mapping occurs write the results to config/ProjectTwEaked/pregenerated_emc.json and only ever run the mapping again" +
						" when that file does not exist, this setting is set to false, or an error occurred parsing that file.");

		Map<NormalizedSimpleStack, Long> graphMapperValues;
		if (shouldUsePregenerated && PECore.PREGENERATED_EMC_FILE.canRead() && PregeneratedEMC.tryRead(PECore.PREGENERATED_EMC_FILE, graphMapperValues = new HashMap<>()))
		{
			PECore.LOGGER.info(String.format("Loaded %d values from pregenerated EMC File", graphMapperValues.size()));
		}
		else
		{


			SimpleGraphMapper.setLogFoundExploits(config.getBoolean("logEMCExploits", "general", true,
					"Log known EMC Exploits. This can not and will not find all possible exploits. " +
							"This will only find exploits that result in fixed/custom emc values that the algorithm did not overwrite. " +
							"Exploits that derive from conversions that are unknown to ProjectTwEaked will not be found."
			));

			PECore.debugLog("Starting to collect Mappings...");
			for (IEMCMapper<NormalizedSimpleStack, Long> emcMapper : emcMappers)
			{
				try
				{
					if (config.getBoolean(emcMapper.getName(), "enabledMappers", emcMapper.isAvailable(), emcMapper.getDescription()) && emcMapper.isAvailable())
					{
						DumpToFileCollector.currentGroupName = emcMapper.getName();
						emcMapper.addMappings(mappingCollector, new PrefixConfiguration(config, "mapperConfigurations." + emcMapper.getName()));
						PECore.debugLog("Collected Mappings from " + emcMapper.getClass().getName());
					}
				} catch (Exception e)
				{
					PECore.LOGGER.fatal("Exception during Mapping Collection from Mapper {}. PLEASE REPORT THIS! EMC VALUES MIGHT BE INCONSISTENT!", emcMapper.getClass().getName());
					e.printStackTrace();
				}
			}
			DumpToFileCollector.currentGroupName = "NSSHelper";
			NormalizedSimpleStack.addMappings(mappingCollector);

			PECore.debugLog("Mapping Collection finished");
			mappingCollector.finishCollection();

			PECore.debugLog("Starting to generate Values:");

			config.save();

			graphMapperValues = valueGenerator.generateValues();
			PECore.debugLog("Generated Values...");

			filterEMCMap(graphMapperValues);

			if (shouldUsePregenerated) {
				//Should have used pregenerated, but the file was not read => regenerate.
				try
				{
					PregeneratedEMC.write(PECore.PREGENERATED_EMC_FILE, graphMapperValues);
					PECore.debugLog("Wrote Pregen-file!");
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}


		for (Map.Entry<NormalizedSimpleStack, Long> entry: graphMapperValues.entrySet()) {
			NSSItem normStackItem = (NSSItem)entry.getKey();
			Item obj = Item.REGISTRY.getObject(new ResourceLocation(normStackItem.itemName));
			if (obj != null)
			{
				emc.put(new SimpleStack(obj.getRegistryName(), normStackItem.damage), entry.getValue());
			} else {
				PECore.LOGGER.warn("Could not add EMC value for {}|{}. Can not get ItemID!", normStackItem.itemName, normStackItem.damage);
			}
		}

		MinecraftForge.EVENT_BUS.post(new EMCRemapEvent());
		Transmutation.cacheFullKnowledge();
		FuelMapper.loadMap();
		PECore.refreshJEI();
	}

	private static void filterEMCMap(Map<NormalizedSimpleStack, Long> map) {
		map.entrySet().removeIf(e -> !(e.getKey() instanceof NSSItem)
										|| ((NSSItem) e.getKey()).damage == OreDictionary.WILDCARD_VALUE
										|| e.getValue() <= 0);
	}

	public static boolean mapContains(SimpleStack key)
	{
		return emc.containsKey(key);
	}

	public static long getEmcValue(SimpleStack stack)
	{
		return emc.get(stack);
	}

	public static void clearMaps() {
		emc.clear();
	}
}
