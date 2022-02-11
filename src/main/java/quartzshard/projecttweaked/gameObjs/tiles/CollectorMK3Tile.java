package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig.PowerFlowerValues;

public class CollectorMK3Tile extends CollectorMK1Tile
{
	public CollectorMK3Tile()
	{
		super(PowerFlowerValues.cMk3Max, PowerFlowerValues.cMk3Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
