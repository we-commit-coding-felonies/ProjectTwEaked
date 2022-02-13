package quartzshard.projecttweaked.gameObjs.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import quartzshard.projecttweaked.utils.WorldHelper;

public class InterdictionTile extends TileEntity implements ITickable
{
	@Override
	public void update()
	{
		WorldHelper.repelEntitiesInAABBFromPoint(world, new AxisAlignedBB(pos.add(-8, -8, -8), pos.add(8, 8, 8)), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, false);
	}
}
