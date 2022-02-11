package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;

public class CollectorMK2Tile extends CollectorMK1Tile
{
	public CollectorMK2Tile()
	{
		super(ProjectTwEakedConfig.powerFlowerValues.cMk2Max, ProjectTwEakedConfig.powerFlowerValues.cMk2Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 12;
	}
}
