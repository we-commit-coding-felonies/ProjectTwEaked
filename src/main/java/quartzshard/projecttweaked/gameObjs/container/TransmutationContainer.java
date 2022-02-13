package quartzshard.projecttweaked.gameObjs.container;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import quartzshard.projecttweaked.gameObjs.container.slots.transmutation.SlotConsume;
import quartzshard.projecttweaked.gameObjs.container.slots.transmutation.SlotInput;
import quartzshard.projecttweaked.gameObjs.container.slots.transmutation.SlotLock;
import quartzshard.projecttweaked.gameObjs.container.slots.transmutation.SlotOutput;
import quartzshard.projecttweaked.gameObjs.container.slots.transmutation.SlotUnlearn;
import quartzshard.projecttweaked.network.PacketHandler;
import quartzshard.projecttweaked.network.packets.SearchUpdatePKT;
import quartzshard.projecttweaked.utils.EMCHelper;
import quartzshard.projecttweaked.utils.ItemHelper;

public class TransmutationContainer extends Container
{
	public final TransmutationInventory transmutationInventory;
	private final int blocked;

	public TransmutationContainer(InventoryPlayer invPlayer, TransmutationInventory inventory, @Nullable EnumHand hand)
	{
		this.transmutationInventory = inventory;
		
		// Transmutation Inventory
		this.addSlotToContainer(new SlotInput(transmutationInventory, 0, 43, 23));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 1, 34, 41));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 2, 52, 41));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 3, 16, 50));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 4, 70, 50));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 5, 34, 59));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 6, 52, 59));
		this.addSlotToContainer(new SlotInput(transmutationInventory, 7, 43, 77));
		this.addSlotToContainer(new SlotLock(transmutationInventory, 8, 158, 50));
		this.addSlotToContainer(new SlotConsume(transmutationInventory, 9, 107, 97));
		this.addSlotToContainer(new SlotUnlearn(transmutationInventory, 10, 89, 97));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 11, 158, 9));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 12, 176, 13));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 13, 193, 30));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 14, 199, 50));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 15, 193, 70));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 16, 176, 87));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 17, 158, 91));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 18, 140, 87));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 19, 123, 70));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 20, 116, 50));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 21, 123, 30));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 22, 140, 13));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 23, 158, 31));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 24, 177, 50));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 25, 158, 69));
		this.addSlotToContainer(new SlotOutput(transmutationInventory, 26, 139, 50));

		//Player Inventory
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++) 
				this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 35 + j * 18, 117 + i * 18));
		
		//Player Hotbar
		for (int i = 0; i < 9; i++)
			this.addSlotToContainer(new Slot(invPlayer, i, 35 + i * 18, 175));

		blocked = hand == EnumHand.MAIN_HAND ? (inventorySlots.size() - 1) - (8 - invPlayer.currentItem) : -1;
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer var1)
	{
		return true;
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
		
		if (slotIndex <= 7) //Input Slots
		{
			return ItemStack.EMPTY;
		}
		else if (slotIndex >= 11 && slotIndex <= 26) // Output Slots
		{	
			long emc = EMCHelper.getEmcValue(newStack);
			
			int stackSize = 0;

			IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

			while (transmutationInventory.getAvailableEMC() >= emc && stackSize < newStack.getMaxStackSize() && ItemHelper.hasSpace(player.inventory.mainInventory, newStack))
			{
				transmutationInventory.removeEmc(emc);
				ItemHandlerHelper.insertItemStacked(inv, ItemHelper.getNormalizedStack(stack), false);
				stackSize++;
			}
			
			transmutationInventory.updateClientTargets();
		}
		else if (slotIndex > 26)
		{
			long emc = EMCHelper.getEmcSellValue(stack);
			
			if (emc == 0 && stack.getItem() != ObjHandler.tome)
			{
				return ItemStack.EMPTY;
			}
			
			while(!transmutationInventory.hasMaxedEmc() && stack.getCount() > 0)
			{
				transmutationInventory.addEmc(emc);
				stack.shrink(1);
			}
			
			transmutationInventory.handleKnowledge(newStack);

			if (stack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
		}
		
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack slotClick(int slot, int button, ClickType flag, EntityPlayer player)
	{
		if (player.getEntityWorld().isRemote && transmutationInventory.getHandlerForSlot(slot) == transmutationInventory.outputs)
		{
			PacketHandler.sendToServer(new SearchUpdatePKT(transmutationInventory.getIndexFromSlot(slot), getSlot(slot).getStack()));
		}

		if (slot == blocked)
		{
			return ItemStack.EMPTY;
		}

		return super.slotClick(slot, button, flag, player);
	}
	
	@Override
	public boolean canDragIntoSlot(Slot slot) 
	{
		return !(slot instanceof SlotConsume || slot instanceof SlotUnlearn || slot instanceof SlotInput || slot instanceof SlotLock || slot instanceof SlotOutput);
	}
}
