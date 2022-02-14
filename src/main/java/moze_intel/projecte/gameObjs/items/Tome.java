package moze_intel.projecte.gameObjs.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import moze_intel.projecte.gameObjs.ObjHandler;

public class Tome extends ItemPE
{
	public Tome()
	{
		this.setTranslationKey("tome");
		this.setCreativeTab(ObjHandler.cTab);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flags)
	{
		list.add(I18n.format("pe.tome.tooltip1"));
	}
}





