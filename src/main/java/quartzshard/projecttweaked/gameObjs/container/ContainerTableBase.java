package main.java.quartzshard.projecttweaked.gameObjs.container;

import main.java.quartzshard.projecttweaked.utils.EXUtils;
import main.java.quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import main.java.quartzshard.projecttweaked.config.ProjectTwEakedConfig;
import main.java.quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import main.java.quartzshard.projecttweaked.api.item.IItemEmc;
import main.java.quartzshard.projecttweaked.gameObjs.container.inventory.TransmutationInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Credit to LatvianModder for ProjectEX tablet this is based on
 */
public class ContainerTableBase extends Container
{
    public final TransmutationInventory transmutationInventory;
	//private final int blocked;
    
	public interface KnowledgeUpdate
	{
		void updateKnowledge();
	}

	public static final int BURN = 1;
	public static final int TAKE_STACK = 2;
	public static final int TAKE_ONE = 3;
	public static final int BURN_ALT = 4;
	public static final int LEARN = 5;
	public static final int UNLEARN = 6;

	public final EntityPlayer player;
	public final IKnowledgeProvider playerData;
	public KnowledgeUpdate knowledgeUpdate;

	public ContainerTableBase(EntityPlayer player, TransmutationInventory inventory, @Nullable EnumHand hand)
	{
        this.player = player;
		this.transmutationInventory = inventory;
        this.playerData = inventory.provider;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		Slot slot = this.getSlot(slotIndex);

        if (slot == null || !slot.getHasStack()) 
		{
			return ItemStack.EMPTY;
		}
        
		ItemStack stack = slot.getStack();

		if (!stack.isEmpty())
		{
			if (!ProjectTwEakedAPI.getEMCProxy().hasValue(stack))
			{
				return ItemStack.EMPTY;
			}

			ItemStack stack1 = EXUtils.fixOutput(stack);

			if (!isItemValid(stack1))
			{
				return ItemStack.EMPTY;
			}

			int k = EXUtils.addKnowledge(player, playerData, stack1);

			if (k == 0)
			{
				return ItemStack.EMPTY;
			}
			else if (k == 2 && knowledgeUpdate != null)
			{
				knowledgeUpdate.updateKnowledge();
			}
            
			EXUtils.addEMC(transmutationInventory, (long) (ProjectTwEakedAPI.getEMCProxy().getValue(stack) * stack.getCount() * ProjectTwEakedConfig.difficulty.covalenceLoss));
			slot.putStack(ItemStack.EMPTY);
			return stack1;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}

	public boolean clickGuiSlot(ItemStack type, int mode)
	{
		ItemStack stack = player.inventory.getItemStack();

		if (mode == BURN || mode == BURN_ALT)
		{
			if (mode == BURN_ALT && stack.getItem() instanceof IItemEmc)
			{
				IItemEmc emcItem = (IItemEmc) stack.getItem();
				long stored = emcItem.getStoredEmc(stack);

				if (stored > 0L)
				{
					EXUtils.addEMC(transmutationInventory, emcItem.extractEmc(stack, stored));
				}
				else
				{
					EXUtils.removeEMC(transmutationInventory, emcItem.addEmc(stack, Math.min(playerData.getEmc(), emcItem.getMaximumEmc(stack))));
				}

				player.inventory.setItemStack(stack);
				return true;
			}

			if (stack.isEmpty() || !ProjectTwEakedAPI.getEMCProxy().hasValue(stack))
			{
				return false;
			}

			ItemStack stack1 = EXUtils.fixOutput(stack);

			if (!isItemValid(stack1))
			{
				return false;
			}

			int k = EXUtils.addKnowledge(player, playerData, stack1);

			if (k == 0)
			{
				return false;
			}
			else if (k == 2 && knowledgeUpdate != null)
			{
				knowledgeUpdate.updateKnowledge();
			}

			EXUtils.addEMC(transmutationInventory, (long) (ProjectTwEakedAPI.getEMCProxy().getValue(stack) * stack.getCount() * ProjectTwEakedConfig.difficulty.covalenceLoss));
			player.inventory.setItemStack(ItemStack.EMPTY);
			return true;
		}
		else if (mode == TAKE_STACK)
		{
			if (type.isEmpty())
			{
				return false;
			}

			ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(type, 1);
			long value = ProjectTwEakedAPI.getEMCProxy().getValue(stack1);

			if (value <= 0L)
			{
				return false;
			}

			int amount = type.getMaxStackSize();

			long max = playerData.getEmc() / value;

			if (amount > max)
			{
				amount = (int) Math.min(amount, max);
			}

			if (amount <= 0)
			{
				return false;
			}

			EXUtils.removeEMC(transmutationInventory, value * amount);
			stack1.setCount(amount);
			player.inventory.placeItemBackInInventory(player.world, stack1);
			return true;
		}
		else if (mode == TAKE_ONE)
		{
			if (type.isEmpty())
			{
				return false;
			}

			if (!stack.isEmpty() && (!ItemHandlerHelper.canItemStacksStack(type, stack) || stack.getCount() >= stack.getMaxStackSize()))
			{
				return false;
			}

			ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(type, 1);
			long value = ProjectTwEakedAPI.getEMCProxy().getValue(stack1);

			if (value <= 0L)
			{
				return false;
			}

			if (playerData.getEmc() < value)
			{
				return false;
			}

			EXUtils.removeEMC(transmutationInventory, value);

			if (!stack.isEmpty())
			{
				stack.grow(1);
			}
			else
			{
				player.inventory.setItemStack(stack1);
			}

			return true;
		}
		else if (mode == LEARN)
		{
			if (stack.isEmpty() || !ProjectTwEakedAPI.getEMCProxy().hasValue(stack))
			{
				return false;
			}

			ItemStack stack1 = EXUtils.fixOutput(stack);

			if (!isItemValid(stack1))
			{
				return false;
			}

			int k = EXUtils.addKnowledge(player, playerData, stack1);

			if (k == 0)
			{
				return false;
			}
			else if (k == 2 && knowledgeUpdate != null)
			{
				knowledgeUpdate.updateKnowledge();
			}

			return true;
		}
		else if (mode == UNLEARN)
		{
			if (stack.isEmpty())
			{
				return false;
			}

			if (playerData.removeKnowledge(EXUtils.fixOutput(stack)))
			{
				if (knowledgeUpdate != null)
				{
					knowledgeUpdate.updateKnowledge();
				}

				return true;
			}

			return false;
		}

		return false;
	}
}