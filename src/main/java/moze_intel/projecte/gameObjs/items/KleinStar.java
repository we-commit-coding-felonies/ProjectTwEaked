package moze_intel.projecte.gameObjs.items;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.utils.EMCHelper;
import moze_intel.projecte.utils.ItemHelper;

public class KleinStar extends ItemPE implements IItemEmc
{

	public KleinStar()
	{
		this.setTranslationKey("klein_star");
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.hasTagCompound();
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		long starEmc = getEmc(stack);
		
		if (starEmc == 0)
		{
			return 1.0D;
		}
		
		return 1.0D - starEmc / (double) EMCHelper.getKleinStarMaxEmc(stack);
	}

	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && PECore.DEV_ENVIRONMENT)
		{
			setEmc(stack, EMCHelper.getKleinStarMaxEmc(stack));
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
	
	@Nonnull
	@Override
	public String getTranslationKey(ItemStack stack)
	{
		if (stack.getItemDamage() > 5)
		{
			return "pe.debug.metainvalid";
		}

		return super.getTranslationKey()+ "_" + (stack.getItemDamage() + 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs cTab, NonNullList<ItemStack> list)
	{
		if (isInCreativeTab(cTab))
		{
			for (int i = 0; i < 6; ++i)
			{
				list.add(new ItemStack(this, 1, i));
			}
		}
	}

	public enum EnumKleinTier
	{
		EIN("ein"),
		ZWEI("zwei"),
		DREI("drei"),
		VIER("vier"),
		SPHERE("sphere"),
		OMEGA("omega");

		public final String name;
		EnumKleinTier(String name)
		{
			this.name = name;
		}
	}

	// -- IItemEmc -- //

	@Override
	public long addEmc(@Nonnull ItemStack stack, long toAdd)
	{
		long add = Math.min(getMaximumEmc(stack) - getStoredEmc(stack), toAdd);
		addEmcToStack(stack, add);
		return add;
	}

	@Override
	public long extractEmc(@Nonnull ItemStack stack, long toRemove)
	{
		long sub = Math.min(getStoredEmc(stack), toRemove);
		removeEmc(stack, sub);
		return sub;
	}

	@Override
	public long getStoredEmc(@Nonnull ItemStack stack)
	{
		return getEmc(stack);
	}

	@Override
	public long getMaximumEmc(@Nonnull ItemStack stack)
	{
		return EMCHelper.getKleinStarMaxEmc(stack);
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return covalenceColorFade(Minecraft.getMinecraft().world.init(), stack);
	}


	// display stuff

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (ItemHelper.getOrCreateCompound(stack).getInteger("offset") == 0)
		{
			ItemHelper.getOrCreateCompound(stack).setInteger("offset", (int)Math.round(Math.random() * ProjectEConfig.misc.barColorFadeSpeed));
		}
		return;
	}
	public int covalenceColorFade(World world, ItemStack stack) {
		int offset = ItemHelper.getOrCreateCompound(stack).getInteger("offset");
		int totalTime = ProjectEConfig.misc.barColorFadeSpeed;
		int fadeSpeed = totalTime/2;
		float fade = ((world.getWorldTime() % totalTime) + offset) % totalTime;
		//high 0.605555555555
		//low 0.303055555556
		if (fade < fadeSpeed) {
			return MathHelper.hsvToRGB(0.3911f + ((0.2154f * fade) / fadeSpeed), 1.0f, 0.824f);
		}
		else {
			return MathHelper.hsvToRGB(0.6056f - ((0.2154f * (fade - fadeSpeed)) / fadeSpeed), 1.0f, 0.824f);
		}
	}
}
