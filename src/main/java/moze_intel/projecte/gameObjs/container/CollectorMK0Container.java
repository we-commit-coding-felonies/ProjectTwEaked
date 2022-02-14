package moze_intel.projecte.gameObjs.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import moze_intel.projecte.emc.FuelMapper;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.container.slots.SlotGhost;
import moze_intel.projecte.gameObjs.container.slots.SlotPredicates;
import moze_intel.projecte.gameObjs.container.slots.ValidatedSlot;
import moze_intel.projecte.gameObjs.tiles.CollectorMK0Tile;

public class CollectorMK0Container extends CollectorMK1Container
{

	public CollectorMK0Container(InventoryPlayer invPlayer, CollectorMK0Tile collector)
	{
		super(invPlayer, collector);
	}

	@Override
	void initSlots(InventoryPlayer invPlayer)
	{
		IItemHandler aux = tile.getAux();
		IItemHandler main = tile.getInput();

		//Klein Star Slot
		this.addSlotToContainer(new ValidatedSlot(aux, CollectorMK0Tile.UPGRADING_SLOT, 124, 58, SlotPredicates.COLLECTOR_INV));

		int counter = main.getSlots() - 1;
		//Fuel Upgrade storage
		for (int i = 0; i <= 1; i++)
			for (int j = 0; j <= 3; j++)
				this.addSlotToContainer(new ValidatedSlot(main, counter--, 20 + i * 18, 8 + j * 18, SlotPredicates.COLLECTOR_INV));

		//Upgrade Result
		this.addSlotToContainer(new ValidatedSlot(aux, CollectorMK0Tile.UPGRADE_SLOT, 124, 13, SlotPredicates.COLLECTOR_INV));

		//Upgrade Target
		this.addSlotToContainer(new SlotGhost(aux, CollectorMK0Tile.LOCK_SLOT, 153, 36, SlotPredicates.COLLECTOR_LOCK));

		//Player inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		//Player hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 142));
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
		
		if (slotIndex <= 10)
		{
			if (!this.mergeItemStack(stack, 11, 46, false))
			{
				return ItemStack.EMPTY;
			}
		}
		else if (slotIndex <= 46)
		{
			if (!FuelMapper.isStackFuel(stack) || FuelMapper.isStackMaxFuel(stack) || !this.mergeItemStack(stack, 1, 8, false))
			{
				return ItemStack.EMPTY;
			}
		}
		else
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
		return player.world.getBlockState(tile.getPos()).getBlock() == ObjHandler.collectorMK0
				&& player.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5) <= 64.0;
	}
}
