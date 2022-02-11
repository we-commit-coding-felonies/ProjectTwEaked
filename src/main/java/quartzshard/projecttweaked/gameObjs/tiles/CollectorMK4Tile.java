package quartzshard.projecttweaked.gameObjs.tiles;

import quartzshard.projecttweaked.utils.Constants;

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
