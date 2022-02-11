package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;

public class CollectorMK0Tile extends CollectorMK1Tile
{
	public CollectorMK0Tile()
	{
		super(ProjectTwEakedConfig.powerFlowerValues.cMk0Gen, ProjectTwEakedConfig.powerFlowerValues.cMk0Max);
	}

	@Override
	protected int getInvSize()
	{
		return 8;
	}
}
