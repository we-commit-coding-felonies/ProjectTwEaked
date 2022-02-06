package main.java.quartzshard.projecttweaked.emc.mappers;

import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.config.CustomEMCParser;
import main.java.quartzshard.projecttweaked.emc.json.NormalizedSimpleStack;
import main.java.quartzshard.projecttweaked.emc.collector.IMappingCollector;
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
