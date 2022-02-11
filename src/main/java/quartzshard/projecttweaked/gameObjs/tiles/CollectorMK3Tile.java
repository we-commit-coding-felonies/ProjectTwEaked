package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;

public class CollectorMK3Tile extends CollectorMK1Tile
{
	public CollectorMK3Tile()
	{
		super(ProjectTwEakedConfig.powerFlowerValues.cMk3Max, ProjectTwEakedConfig.powerFlowerValues.cMk3Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
