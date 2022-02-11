package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig.PowerFlowerValues;

public class CollectorMK0Tile extends CollectorMK1Tile
{
	public CollectorMK0Tile()
	{
		super(PowerFlowerValues.cMk0Gen, PowerFlowerValues.cMk0Max);
	}

	@Override
	protected int getInvSize()
	{
		return 8;
	}
}
