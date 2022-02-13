package quartzshard.projecttweaked.gameObjs.container.slots.transmutation;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import quartzshard.projecttweaked.api.item.IItemEmc;
import quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import quartzshard.projecttweaked.gameObjs.container.slots.SlotPredicates;
import quartzshard.projecttweaked.utils.Constants;
import quartzshard.projecttweaked.utils.EMCHelper;

public class SlotLock extends SlotItemHandler
{
	private final TransmutationInventory inv;
	
	public SlotLock(TransmutationInventory inv, int par2, int par3, int par4)
	{
		super(inv, par2, par3, par4);
		this.inv = inv;
	}
	
	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return SlotPredicates.RELAY_INV.test(stack);
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
			long remainEmc = Constants.TILE_MAX_EMC - inv.provider.getEmc();
			
			if (itemEmc.getStoredEmc(stack) >= remainEmc)
			{
				inv.addEmc(remainEmc);
				itemEmc.extractEmc(stack, remainEmc);
			}
			else
			{
				inv.addEmc(itemEmc.getStoredEmc(stack));
				itemEmc.extractEmc(stack, itemEmc.getStoredEmc(stack));
			}
		}
		
		if (EMCHelper.doesItemHaveEmc(stack)) {
			inv.handleKnowledge(stack.copy());
		}
	}
	
	@Nonnull
	@Override
	public ItemStack onTake(EntityPlayer player, @Nonnull ItemStack stack)
	{
		stack = super.onTake(player, stack);
		inv.updateClientTargets();
		return stack;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
