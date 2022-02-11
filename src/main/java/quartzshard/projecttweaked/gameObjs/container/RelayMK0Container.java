package quartzshard.projecttweaked.gameObjs.container;

import quartzshard.projecttweaked.gameObjs.container.slots.SlotPredicates;
import quartzshard.projecttweaked.gameObjs.container.slots.ValidatedSlot;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.tiles.RelayMK0Tile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class RelayMK0Container extends RelayMK1Container
{
	public RelayMK0Container(InventoryPlayer invPlayer, RelayMK0Tile relay)
	{
		super(invPlayer, relay);
	}

	@Override
	void initSlots(InventoryPlayer invPlayer)
	{
		IItemHandler input = tile.getInput();
		IItemHandler output = tile.getOutput();

		//Klein Star charge slot
		this.addSlotToContainer(new ValidatedSlot(input, 0, 67, 43, SlotPredicates.RELAY_INV));

		int counter = input.getSlots() - 1;
		//Main Relay inventory
		for (int i = 0; i <= 1; i++)
			for (int j = 0; j <= 2; j++)
				this.addSlotToContainer(new ValidatedSlot(input, counter--, 27 + i * 18, 17 + j * 18, SlotPredicates.RELAY_INV));

		//Burning slot
		this.addSlotToContainer(new ValidatedSlot(output, 0, 127, 43, SlotPredicates.IITEMEMC));

		//Player Inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 95 + i * 18));

		//Player Hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 153));
	}

	@Nonnull
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		Slot slot = this.getSlot(slotIndex);

		if (slot == null || !slot.getHasStack())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();
		ItemStack newStack = stack.copy();

		if (slotIndex < 8)
		{
			if (!this.mergeItemStack(stack, 8, this.inventorySlots.size(), true))
				return ItemStack.EMPTY;
			slot.onSlotChanged();
		}
		else if (!this.mergeItemStack(stack, 0, 7, false))
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

		return slot.onTake(player, newStack);
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer player)
	{
		return player.world.getBlockState(tile.getPos()).getBlock() == ObjHandler.relayMK0
				&& player.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5) <= 64.0;
	}
}
