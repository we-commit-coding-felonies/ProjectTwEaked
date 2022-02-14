package moze_intel.projecte.gameObjs.items.rings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import moze_intel.projecte.api.item.IPedestalItem;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.tiles.DMPedestalTile;
import moze_intel.projecte.utils.EMCHelper;
import moze_intel.projecte.utils.ItemHelper;
import moze_intel.projecte.utils.MathUtils;
import moze_intel.projecte.utils.WorldHelper;

public class HarvestGoddess extends RingToggle implements IPedestalItem
{
	public HarvestGoddess()
	{
		super("harvest_god");
		this.setNoRepair();
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) 
	{
		if (world.isRemote || par4 > 8 || !(entity instanceof EntityPlayer)) 
		{
			return;
		}
		
		super.onUpdate(stack, world, entity, par4, par5);
		
		EntityPlayer player = (EntityPlayer) entity;
		
		if (ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE))
		{
			long storedEmc = getEmc(stack);
			
			if (storedEmc == 0 && !consumeFuel(player, stack, 64, true))
			{
				stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
			}
			else
			{
				WorldHelper.growNearbyRandomly(true, world, new BlockPos(player), player);
				removeEmc(stack, EMCHelper.removeFractionalEMC(stack, 0.32F));
			}
		}
		else
		{
			WorldHelper.growNearbyRandomly(false, world, new BlockPos(player), player);
		}
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float par8, float par9, float par10)
	{
		if (world.isRemote || !player.canPlayerEdit(pos, facing, player.getHeldItem(hand)))
		{
			return EnumActionResult.FAIL;
		}
		
		if (player.isSneaking())
		{
			Object[] obj = getStackFromInventory(player.inventory.mainInventory, Items.DYE, 15, 4);

			if (obj == null) 
			{
				return EnumActionResult.FAIL;
			}
			
			ItemStack boneMeal = (ItemStack) obj[1];

			if (!boneMeal.isEmpty() && useBoneMeal(world, pos))
			{
				player.inventory.decrStackSize((Integer) obj[0], 4);
				player.inventoryContainer.detectAndSendChanges();
				return EnumActionResult.SUCCESS;
			}
			
			return EnumActionResult.FAIL;
		}
		
		return plantSeeds(world, player, pos) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}
	
	private boolean useBoneMeal(World world, BlockPos pos)
	{
		boolean result = false;

		for (BlockPos currentPos : BlockPos.getAllInBoxMutable(pos.add(-15, 0, -15), pos.add(15, 0, 15)))
		{
			IBlockState state = world.getBlockState(currentPos);
			Block crop = state.getBlock();

			if (crop instanceof IGrowable)
			{
				IGrowable growable = (IGrowable) crop;

				if (growable.canUseBonemeal(world, world.rand, currentPos, state))
				{
					if (!result)
					{
						result = true;
					}

					growable.grow(world, world.rand, currentPos.toImmutable(), state);
				}
			}
		}
		return result;
	}
	
	private boolean plantSeeds(World world, EntityPlayer player, BlockPos pos)
	{
		boolean result = false;
		
		List<StackWithSlot> seeds = getAllSeeds(player.inventory.mainInventory);
		
		if (seeds.isEmpty())
		{
			return false;
		}

		for (BlockPos currentPos : BlockPos.getAllInBox(pos.add(-8, 0, -8), pos.add(8, 0, 8)))
		{
			IBlockState state = world.getBlockState(currentPos);

			if (world.isAirBlock(currentPos))
			{
				continue;
			}

			for (int i = 0; i < seeds.size(); i++)
			{
				StackWithSlot s = seeds.get(i);
				IPlantable plant;

				if (s.stack.getItem() instanceof IPlantable)
				{
					plant = (IPlantable) s.stack.getItem();
				}
				else
				{
					plant = (IPlantable) Block.getBlockFromItem(s.stack.getItem());
				}

				if (state.getBlock().canSustainPlant(state, world, currentPos, EnumFacing.UP, plant) && world.isAirBlock(currentPos.up()))
				{
					world.setBlockState(currentPos.up(), plant.getPlant(world, currentPos.up()));
					player.inventory.decrStackSize(s.slot, 1);
					player.inventoryContainer.detectAndSendChanges();

					s.stack.shrink(1);

					if (s.stack.isEmpty())
					{
						seeds.remove(i);
					}

					if (!result)
					{
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	private List<StackWithSlot> getAllSeeds(NonNullList<ItemStack> inv)
	{
		List<StackWithSlot> result = new ArrayList<>();
		
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack stack = inv.get(i);
			
			if (!stack.isEmpty())
			{
				if (stack.getItem() instanceof IPlantable)
				{
					result.add(new StackWithSlot(stack, i));
					continue;
				}
				
				Block block = Block.getBlockFromItem(stack.getItem());
				
				if (block != null && block instanceof IPlantable)
				{
					result.add(new StackWithSlot(stack, i));
				}
			}
		}
		
		return result;
	}
	
	private Object[] getStackFromInventory(NonNullList<ItemStack> inv, Item item, int meta, int minAmount)
	{
		Object[] obj = new Object[2];
		
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack stack = inv.get(i);
			
			if (!stack.isEmpty() && stack.getCount() >= minAmount && stack.getItem() == item && stack.getItemDamage() == meta)
			{
				obj[0] = i;
				obj[1] = stack;
				return obj;
			}
		}
		
		return null;
	}

	@Override
	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		NBTTagCompound tag = ItemHelper.getOrCreateCompound(stack);
		tag.setBoolean(TAG_ACTIVE, !tag.getBoolean(TAG_ACTIVE));
		return true;
	}

	@Override
	public void updateInPedestal(@Nonnull World world, @Nonnull BlockPos pos)
	{
		if (!world.isRemote && ProjectEConfig.pedestalCooldown.harvestPedCooldown != -1)
		{
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof DMPedestalTile))
			{
				return;
			}

			DMPedestalTile tile = (DMPedestalTile) te;
			if (tile.getActivityCooldown() == 0)
			{
				WorldHelper.growNearbyRandomly(true, world, pos, null);
				tile.setActivityCooldown(ProjectEConfig.pedestalCooldown.harvestPedCooldown);
			}
			else
			{
				tile.decrementActivityCooldown();
			}
		}
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	@Override
	public List<String> getPedestalDescription()
	{
		List<String> list = new ArrayList<>();
		if (ProjectEConfig.pedestalCooldown.harvestPedCooldown != -1)
		{
			list.add(TextFormatting.BLUE + I18n.format("pe.harvestgod.pedestal1"));
			list.add(TextFormatting.BLUE + I18n.format("pe.harvestgod.pedestal2"));
			list.add(TextFormatting.BLUE +
					I18n.format("pe.harvestgod.pedestal3", MathUtils.tickToSecFormatted(ProjectEConfig.pedestalCooldown.harvestPedCooldown)));
		}
		return list;
	}

	private static class StackWithSlot
	{
		public final int slot;
		public final ItemStack stack;
		
		public StackWithSlot(ItemStack stack, int slot) 
		{
			this.stack = stack.copy();
			this.slot = slot;
		}
	}
}
