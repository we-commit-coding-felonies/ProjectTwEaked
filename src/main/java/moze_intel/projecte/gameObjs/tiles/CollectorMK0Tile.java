package moze_intel.projecte.gameObjs.tiles;

import moze_intel.projecte.utils.Constants;

public class CollectorMK0Tile extends CollectorMK1Tile
{
	public CollectorMK0Tile()
	{
		super(Constants.COLLECTOR_MK0_MAX, Constants.COLLECTOR_MK0_GEN);
	}

	@Override
	protected int getInvSize()
	{
		return 8;
	}
}
