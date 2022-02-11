package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;

public class CollectorMK4Tile extends CollectorMK1Tile
{
	public CollectorMK4Tile()
	{
		super(ProjectTwEakedConfig.powerFlowerValues.cMk4Max, ProjectTwEakedConfig.powerFlowerValues.cMk4Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
