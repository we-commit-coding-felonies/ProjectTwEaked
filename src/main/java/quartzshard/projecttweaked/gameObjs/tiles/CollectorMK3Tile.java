package main.java.quartzshard.projecttweaked.gameObjs.tiles;

import main.java.quartzshard.projecttweaked.utils.Constants;

public class CollectorMK3Tile extends CollectorMK1Tile
{
	public CollectorMK3Tile()
	{
		super(Constants.COLLECTOR_MK3_MAX, Constants.COLLECTOR_MK3_GEN);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
