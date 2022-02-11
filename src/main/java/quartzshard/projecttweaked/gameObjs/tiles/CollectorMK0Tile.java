package quartzshard.projecttweaked.gameObjs.tiles;

import quartzshard.projecttweaked.utils.Constants;

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
