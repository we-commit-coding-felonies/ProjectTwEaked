package main.java.quartzshard.projecttweaked.gameObjs.items.rings;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.Lists;
import main.java.quartzshard.projecttweaked.api.PESounds;
import main.java.quartzshard.projecttweaked.api.item.IPedestalItem;
import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import main.java.quartzshard.projecttweaked.gameObjs.tiles.DMPedestalTile;
import main.java.quartzshard.projecttweaked.handlers.InternalTimers;
import main.java.quartzshard.projecttweaked.utils.ItemHelper;
import main.java.quartzshard.projecttweaked.utils.MathUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class LifeStone extends RingToggle implements IBauble, IPedestalItem
{
	public LifeStone()
	{
		super("life_stone");
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
			if (!consumeFuel(player, stack, 2*64, false))
			{
				stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
			}
			else
			{
				player.getCapability(InternalTimers.CAPABILITY, null).activateFeed();
				player.getCapability(InternalTimers.CAPABILITY, null).activateHeal();

				if (player.getHealth() < player.getMaxHealth() && player.getCapability(InternalTimers.CAPABILITY, null).canHeal())
				{
					world.playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.PLAYERS, 1, 1);
					player.heal(2.0F);
					removeEmc(stack, 64);
				}

				if (player.getFoodStats().needFood() && player.getCapability(InternalTimers.CAPABILITY, null).canFeed())
				{
					world.playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.PLAYERS, 1, 1);
					player.getFoodStats().addStats(2, 10);
					removeEmc(stack, 64);
				}
			}
		}
	}
	
	@Override
	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		NBTTagCompound tag  = ItemHelper.getOrCreateCompound(stack);
		tag.setBoolean(TAG_ACTIVE, !tag.getBoolean(TAG_ACTIVE));
		return true;
	}
	
	@Override
	@Optional.Method(modid = "baubles")
	public baubles.api.BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.AMULET;
	}

	@Override
	@Optional.Method(modid = "baubles")
	public void onWornTick(ItemStack stack, EntityLivingBase player) 
	{
		this.onUpdate(stack, player.getEntityWorld(), player, 0, false);
	}

	@Override
	@Optional.Method(modid = "baubles")
	public void onEquipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	@Optional.Method(modid = "baubles")
	public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {}

	@Override
	@Optional.Method(modid = "baubles")
	public boolean canEquip(ItemStack stack, EntityLivingBase player) 
	{
		return true;
	}

	@Override
	@Optional.Method(modid = "baubles")
	public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) 
	{
		return true;
	}

	@Override
	public void updateInPedestal(@Nonnull World world, @Nonnull BlockPos pos)
	{
		if (!world.isRemote && ProjectTwEakedConfig.pedestalCooldown.lifePedCooldown != -1)
		{
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof DMPedestalTile))
			{
				return;
			}
			DMPedestalTile tile = (DMPedestalTile) te;
			if (tile.getActivityCooldown() == 0)
			{
				List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, tile.getEffectBounds());

				for (EntityPlayerMP player : players)
				{
					if (player.getHealth() < player.getMaxHealth())
					{
						world.playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.BLOCKS, 1, 1);
						player.heal(1.0F); // 1/2 heart
					}
					if (player.getFoodStats().needFood())
					{
						world.playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.BLOCKS, 1, 1);
						player.getFoodStats().addStats(1, 1); // 1/2 shank
					}
				}

				tile.setActivityCooldown(ProjectTwEakedConfig.pedestalCooldown.lifePedCooldown);
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
		if (ProjectTwEakedConfig.pedestalCooldown.lifePedCooldown != -1)
		{
			list.add(TextFormatting.BLUE + I18n.format("pe.life.pedestal1"));
			list.add(TextFormatting.BLUE + I18n.format("pe.life.pedestal2", MathUtils.tickToSecFormatted(ProjectTwEakedConfig.pedestalCooldown.lifePedCooldown)));
		}
		return list;
	}
}
