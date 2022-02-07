package main.java.quartzshard.projecttweaked.client;

import main.java.quartzshard.projecttweaked.PECore;
import net.minecraftforge.common.config.Config;

/**
 * @author LatvianModder
 */
@Config(modid = PECore.MODID, category = "", name = "../local/client/" + PECore.MODID)
@Config.LangKey(PECore.MODID + "_client")
public class ProjectEXClientConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	public enum EnumScreenPosition
	{
		DISABLED("Disabled"),
		TOP_LEFT("Top-Left"),
		TOP_RIGHT("Top-Right");

		private final String string;

		EnumScreenPosition(String s)
		{
			string = s;
		}

		@Override
		public String toString()
		{
			return string;
		}
	}
	public static class General
	{
		public EnumScreenPosition emc_screen_position = EnumScreenPosition.TOP_LEFT;

		public EnumSearchType search_type = EnumSearchType.NORMAL;
	}
}