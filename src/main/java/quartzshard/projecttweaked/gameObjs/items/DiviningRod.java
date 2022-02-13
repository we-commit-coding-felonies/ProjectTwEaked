package quartzshard.projecttweaked.gameObjs.items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quartzshard.projecttweaked.api.item.IModeChanger;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.utils.EMCHelper;
import quartzshard.projecttweaked.utils.ItemHelper;
import quartzshard.projecttweaked.utils.PlayerHelper;
import quartzshard.projecttweaked.utils.WorldHelper;

public class DiviningRod extends ItemPE implements IModeChanger
{
	// Modes should be in the format depthx3x3
	private final String[] modes;

	public DiviningRod(String[] modeDesc)
	{
		modes = modeDesc;
	}
	
	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}

		PlayerHelper.swingItem(player, hand);
		List<Long> emcValues = new ArrayList<>();
		long totalEmc = 0;
		int numBlocks = 0;

		byte mode = getMode(player.getHeldItem(hand));
		int depth = getDepthFromMode(mode);
		AxisAlignedBB box = WorldHelper.getDeepBox(pos, facing, depth);

		for (BlockPos digPos : WorldHelper.getPositionsFromBox(box))
		{
			IBlockState state = world.getBlockState(digPos);
			Block block = state.getBlock();

			if (world.isAirBlock(digPos))
			{
				continue;
			}

			List<ItemStack> drops = block.getDrops(world, digPos, state, 0);

			if (drops.size() == 0)
			{
				continue;
			}

			ItemStack blockStack = drops.get(0);
			long blockEmc = EMCHelper.getEmcValue(blockStack);

			if (blockEmc == 0)
			{
				Map<ItemStack, ItemStack> map = FurnaceRecipes.instance().getSmeltingList();

				for (Entry<ItemStack, ItemStack> entry : map.entrySet())
				{
					if (entry == null || entry.getKey().isEmpty())
					{
						continue;
					}

					if (ItemHelper.areItemStacksEqualIgnoreNBT(entry.getKey(), blockStack))
					{
						long currentValue = EMCHelper.getEmcValue(entry.getValue());

						if (currentValue != 0)
						{
							if (!emcValues.contains(currentValue))
							{
								emcValues.add(currentValue);
							}

							totalEmc += currentValue;
						}
					}
				}
			}
			else
			{
				if (!emcValues.contains(blockEmc))
				{
					emcValues.add(blockEmc);
				}

				totalEmc += blockEmc;
			}

			numBlocks++;
		}

		if (numBlocks == 0)
		{
			return EnumActionResult.FAIL;
		}

		long[] maxValues = new long[3];

		for (int i = 0; i < 3; i++)
		{
			maxValues[i] = 1;
		}

		emcValues.sort(Comparator.reverseOrder());

		int num = emcValues.size() >= 3 ? 3 : emcValues.size();

		for (int i = 0; i < num; i++)
		{
			maxValues[i] = emcValues.get(i);
		}

		player.sendMessage(new TextComponentTranslation("pe.divining.avgemc", numBlocks, (totalEmc / numBlocks)));

		if (this == ObjHandler.dRod2 || this == ObjHandler.dRod3)
		{
			player.sendMessage(new TextComponentTranslation("pe.divining.maxemc", maxValues[0]));
		}

		if (this == ObjHandler.dRod3)
		{
			player.sendMessage(new TextComponentTranslation("pe.divining.secondmax", maxValues[1]));
			player.sendMessage(new TextComponentTranslation("pe.divining.thirdmax", maxValues[2]));
		}

		return EnumActionResult.SUCCESS;
	}

	/**
	 * Gets the first number in the mode description.
	 */
	private int getDepthFromMode(byte mode)
	{
		String modeDesc = modes[mode];
		// Subtract one because of how the box method works
		return Integer.parseInt(modeDesc.substring(0, modeDesc.indexOf('x'))) - 1;
	}

	@Override
	public byte getMode(@Nonnull ItemStack stack)
	{
		return ItemHelper.getOrCreateCompound(stack).getByte(TAG_MODE);
	}

	@Override
	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		if (modes.length == 1)
		{
			return false;
		}
		if (getMode(stack) == modes.length - 1)
		{
			ItemHelper.getOrCreateCompound(stack).setByte(TAG_MODE, ((byte) 0));
		}
		else
		{
			ItemHelper.getOrCreateCompound(stack).setByte(TAG_MODE, ((byte) (getMode(stack) + 1)));
		}

		player.sendMessage(new TextComponentTranslation("pe.item.mode_switch", modes[getMode(stack)]));

		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags)
	{
		list.add(I18n.format("pe.item.mode") + ": " + TextFormatting.AQUA + modes[getMode(stack)]);
	}
}
