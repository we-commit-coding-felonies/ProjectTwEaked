package moze_intel.projecte.gameObjs.tiles;

import moze_intel.projecte.utils.Constants;

public class CollectorMK4Tile extends CollectorMK1Tile
{
	public CollectorMK4Tile()
	{
		super(Constants.COLLECTOR_MK4_MAX, Constants.COLLECTOR_MK4_GEN);
	}

	@Override
	protected int getInvSize()
	{
		return 16;
	}
}
