package quartzshard.projecttweaked.gameObjs.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.container.slots.SlotCondenserLock;
import quartzshard.projecttweaked.gameObjs.container.slots.SlotPredicates;
import quartzshard.projecttweaked.gameObjs.container.slots.ValidatedSlot;
import quartzshard.projecttweaked.gameObjs.tiles.CondenserMK2Tile;
import quartzshard.projecttweaked.utils.EMCHelper;

public class CondenserMK2Container extends CondenserContainer
{
	public CondenserMK2Container(InventoryPlayer invPlayer, CondenserMK2Tile condenser)
	{
		super(invPlayer, condenser);
	}

	@Override
	protected void initSlots(InventoryPlayer invPlayer)
	{
		this.addSlotToContainer(new SlotCondenserLock(tile.getLock(), 0, 12, 6));

		IItemHandler input = tile.getInput();
		IItemHandler output = tile.getOutput();

		//Condenser Inventory
		//Inputs
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 6; j++)
				this.addSlotToContainer(new ValidatedSlot(input, j + i * 6, 12 + j * 18, 26 + i * 18, s -> SlotPredicates.HAS_EMC.test(s) && !tile.isStackEqualToLock(s)));

		//Outputs
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 6; j++)
				this.addSlotToContainer(new ValidatedSlot(output, j + i * 6, 138 + j * 18, 26 + i * 18, s -> false));

		//Player Inventory
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 48 + j * 18, 154 + i * 18));

		//Player Hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 48 + i * 18, 212));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		if (slotIndex == 0)
		{
			return ItemStack.EMPTY;
		}

		Slot slot = this.getSlot(slotIndex);

		if (slot == null || !slot.getHasStack())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();

		if (slotIndex <= 84)
		{
			if (!this.mergeItemStack(stack, 85, 120, false))
			{
				return ItemStack.EMPTY;
			}
		}
		else if (!EMCHelper.doesItemHaveEmc(stack) || !this.mergeItemStack(stack, 1, 42, false))
		{
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty())
		{
			slot.putStack(ItemStack.EMPTY);
		}
		else
		{
			slot.onSlotChanged();
		}

		return slot.onTake(player, stack);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player)
	{
		return player.world.getBlockState(tile.getPos()).getBlock() == ObjHandler.condenserMk2
				&& player.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5) <= 64.0;
	}
}
