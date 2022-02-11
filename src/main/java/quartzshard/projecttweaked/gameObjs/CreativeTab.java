package quartzshard.projecttweaked.gameObjs;

import quartzshard.projecttweaked.PECore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class CreativeTab extends CreativeTabs
{
	public CreativeTab()
	{
		super(PECore.MODID);
	}

	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack createIcon()
	{
		return new ItemStack(ObjHandler.philosStone);
	}
}
