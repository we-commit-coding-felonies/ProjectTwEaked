package main.java.quartzshard.projecttweaked.gameObjs.container.slots.transmutation;

import main.java.quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotConsume extends SlotItemHandler
{
	private final TransmutationInventory inv;
	
	public SlotConsume(TransmutationInventory inv, int par2, int par3, int par4)
	{
		super(inv, par2, par3, par4);
		this.inv = inv;
	}
	
	@Override
	public void putStack(@Nonnull ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return;
		}
		
		ItemStack cache = stack.copy();
		
		long toAdd = 0;
		
		while (!inv.hasMaxedEmc() && stack.getCount() > 0)
		{
			toAdd += EMCHelper.getEmcSellValue(stack);
			stack.shrink(1);
		}
		
		inv.addEmc(toAdd);
		this.onSlotChanged();
		inv.handleKnowledge(cache);
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return !inv.hasMaxedEmc() && (EMCHelper.doesItemHaveEmc(stack) || stack.getItem() == ObjHandler.tome);
	}
}
