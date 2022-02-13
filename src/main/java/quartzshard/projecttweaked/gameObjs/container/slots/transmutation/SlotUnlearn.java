package quartzshard.projecttweaked.gameObjs.container.slots.transmutation;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import quartzshard.projecttweaked.utils.EMCHelper;

public class SlotUnlearn extends SlotItemHandler
{
	private final TransmutationInventory inv;
	
	public SlotUnlearn(TransmutationInventory inv, int par2, int par3, int par4)
	{
		super(inv, par2, par3, par4);
		this.inv = inv;
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return !this.getHasStack() && (EMCHelper.doesItemHaveEmc(stack) || stack.getItem() == ObjHandler.tome);
	}
	
	@Override
	public void putStack(@Nonnull ItemStack stack)
	{
		if (!stack.isEmpty())
		{
			inv.handleUnlearn(stack.copy());
		}

		super.putStack(stack);
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
