package quartzshard.projecttweaked.gameObjs.container;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import quartzshard.projecttweaked.emc.FuelMapper;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.container.slots.SlotGhost;
import quartzshard.projecttweaked.gameObjs.container.slots.SlotPredicates;
import quartzshard.projecttweaked.gameObjs.container.slots.ValidatedSlot;
import quartzshard.projecttweaked.gameObjs.tiles.CollectorMK3Tile;

public class CollectorMK3Container extends CollectorMK1Container
{
	public CollectorMK3Container(InventoryPlayer invPlayer, CollectorMK3Tile collector)
	{
		super(invPlayer, collector);
	}

	@Override
	void initSlots(InventoryPlayer invPlayer)
	{
		IItemHandler aux = tile.getAux();
		IItemHandler main = tile.getInput();

		//Klein Star Slot
		this.addSlotToContainer(new ValidatedSlot(aux, CollectorMK3Tile.UPGRADING_SLOT, 158, 58, SlotPredicates.COLLECTOR_INV));

		int counter = main.getSlots() - 1;
		//Fuel Upgrade Slot
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.addSlotToContainer(new ValidatedSlot(main, counter--, 18 + i * 18, 8 + j * 18, SlotPredicates.COLLECTOR_INV));

		//Upgrade Result
		this.addSlotToContainer(new ValidatedSlot(aux, CollectorMK3Tile.UPGRADE_SLOT, 158, 13, SlotPredicates.COLLECTOR_INV));

		//Upgrade Target
		this.addSlotToContainer(new SlotGhost(aux, CollectorMK3Tile.LOCK_SLOT, 187, 36, SlotPredicates.COLLECTOR_LOCK));

		//Player inventory
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 30 + j * 18, 84 + i * 18));

		//Player hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 30 + i * 18, 142));
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
		
		if (slotIndex <= 18)
		{
			if (!this.mergeItemStack(stack, 19, 54, false))
			{
				return ItemStack.EMPTY;
			}
		}
		else if (slotIndex <= 54)
		{
			if (!FuelMapper.isStackFuel(stack) || FuelMapper.isStackMaxFuel(stack) || !this.mergeItemStack(stack, 1, 16, false))
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
		return player.world.getBlockState(tile.getPos()).getBlock() == ObjHandler.collectorMK3
				&& player.getDistanceSq(tile.getPos().getX() + 0.5, tile.getPos().getY() + 0.5, tile.getPos().getZ() + 0.5) <= 64.0;
	}
}
