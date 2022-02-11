package quartzshard.projecttweaked.emc.mappers;

import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.config.CustomEMCParser;
import quartzshard.projecttweaked.emc.json.NormalizedSimpleStack;
import quartzshard.projecttweaked.emc.collector.IMappingCollector;
import net.minecraftforge.common.config.Configuration;

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
