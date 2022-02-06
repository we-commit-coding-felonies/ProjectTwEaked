package main.java.quartzshard.projecttweaked.gameObjs.blocks;

import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;
import main.java.quartzshard.projecttweaked.utils.Constants;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CondenserMK2 extends Condenser
{
	public CondenserMK2()
	{
		this.setTranslationKey("pe_condenser_mk2");
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new CondenserMK2Tile();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.CONDENSER_MK2_GUI, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
}
