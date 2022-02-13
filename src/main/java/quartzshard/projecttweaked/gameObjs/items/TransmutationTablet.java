package quartzshard.projecttweaked.gameObjs.items;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.utils.Constants;

public class TransmutationTablet extends ItemPE
{
	public TransmutationTablet()
	{
		this.setTranslationKey("transmutation_tablet");
		this.setMaxStackSize(1);
	}
	
	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
	{
		if (!world.isRemote)
		{
			player.openGui(PECore.instance, Constants.TRANSMUTATION_GUI, world, hand == EnumHand.MAIN_HAND ? 0 : 1, -1, -1);
		}
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
