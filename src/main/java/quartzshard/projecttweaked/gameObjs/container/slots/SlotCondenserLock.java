package quartzshard.projecttweaked.gameObjs.container.slots;

import quartzshard.projecttweaked.utils.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class SlotCondenserLock extends SlotGhost {
	public SlotCondenserLock(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition, SlotPredicates.HAS_EMC);
	}

	@Override
	public void putStack(@Nonnull ItemStack stack)
	{
		if (!stack.isEmpty() && ItemHelper.isDamageable(stack))
		{
			stack.setItemDamage(0);
		}

		super.putStack(stack);
	}
}
