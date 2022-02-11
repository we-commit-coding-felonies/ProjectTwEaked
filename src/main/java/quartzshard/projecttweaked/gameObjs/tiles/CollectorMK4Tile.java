package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig.PowerFlowerValues;

public class CollectorMK4Tile extends CollectorMK1Tile
{
	public CollectorMK4Tile()
	{
		super(PowerFlowerValues.cMk4Max, PowerFlowerValues.cMk4Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
