package main.java.quartzshard.projecttweaked.gameObjs.container.slots.transmutation;

import main.java.quartzshard.projecttweaked.api.item.IItemEmc;
import main.java.quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import main.java.quartzshard.projecttweaked.gameObjs.container.slots.SlotPredicates;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotInput extends SlotItemHandler
{
	private final TransmutationInventory inv;
	
	public SlotInput(TransmutationInventory inv, int par2, int par3, int par4)
	{
		super(inv, par2, par3, par4);
		this.inv = inv;
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return SlotPredicates.RELAY_INV.test(stack);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int amount)
	{
		ItemStack stack = super.decrStackSize(amount);
		//Decrease the size of the stack
		if (stack.getItem() instanceof IItemEmc)
		{
			//If it was an EMC storing item then check for updates,
			// so that the right hand side shows the proper items
			inv.checkForUpdates();
		}
		return stack;
	}
	
	@Override
	public void putStack(@Nonnull ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return;
		}
		
		super.putStack(stack);
		
		if (stack.getItem() instanceof IItemEmc)
		{
			IItemEmc itemEmc = ((IItemEmc) stack.getItem());
			long remainingEmc = itemEmc.getMaximumEmc(stack) - itemEmc.getStoredEmc(stack);
			long availableEMC = inv.getAvailableEMC();

			if (availableEMC >= remainingEmc)
			{
				itemEmc.addEmc(stack, remainingEmc);
				inv.removeEmc(remainingEmc);
			}
			else
			{
				itemEmc.addEmc(stack, availableEMC);
				inv.removeEmc(availableEMC);
			}
		}

		if (EMCHelper.doesItemHaveEmc(stack)) {
			inv.handleKnowledge(stack.copy());
		}
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
