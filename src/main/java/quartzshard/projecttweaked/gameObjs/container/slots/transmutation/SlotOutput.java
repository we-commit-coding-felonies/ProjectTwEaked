package quartzshard.projecttweaked.gameObjs.container.slots.transmutation;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import quartzshard.projecttweaked.utils.EMCHelper;

public class SlotOutput extends SlotItemHandler
{
	private final TransmutationInventory inv;
	
	public SlotOutput(TransmutationInventory inv, int par2, int par3, int par4)
	{
		super(inv, par2, par3, par4);
		this.inv = inv;
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int amount)
	{
		ItemStack stack = getStack().copy();
		stack.setCount(amount);
		long emcValue = amount * EMCHelper.getEmcValue(stack);
		if (emcValue > inv.getAvailableEMC()) {
			//Requesting more emc than available
			//Container expects stacksize=0-Itemstack for 'nothing'
			stack.setCount(0);
			return stack;
		}
		inv.removeEmc(emcValue);
		inv.checkForUpdates();
		
		return stack;
	}
	
	@Override
	public void putStack(@Nonnull ItemStack stack) {}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer player) {
		return !getHasStack() || EMCHelper.getEmcValue(getStack()) <= inv.getAvailableEMC();
	}
}
