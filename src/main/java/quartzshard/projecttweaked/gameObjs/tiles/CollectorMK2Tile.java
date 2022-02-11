package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig.PowerFlowerValues;

public class CollectorMK2Tile extends CollectorMK1Tile
{
	public CollectorMK2Tile()
	{
		super(PowerFlowerValues.cMk2Max, PowerFlowerValues.cMk2Gen);
	}

	@Override
	protected int getInvSize()
	{
		return 12;
	}
}
