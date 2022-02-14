package moze_intel.projecte.emc.mappers;

import net.minecraftforge.common.config.Configuration;
import moze_intel.projecte.PECore;
import moze_intel.projecte.config.CustomEMCParser;
import moze_intel.projecte.emc.collector.IMappingCollector;
import moze_intel.projecte.emc.json.NormalizedSimpleStack;

public class CustomEMCMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
	@Override
	public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper, Configuration config) {
		for (CustomEMCParser.CustomEMCEntry entry : CustomEMCParser.currentEntries.entries) {
			PECore.debugLog("Adding custom EMC value for {}: {}", entry.nss, entry.emc);
			mapper.setValueBefore(entry.nss, entry.emc);
		}
	}

	@Override
	public String getName() {
		return "CustomEMCMapper";
	}

	@Override
	public String getDescription() {
		return "Uses the `custom_emc.json` File to add EMC values.";
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
