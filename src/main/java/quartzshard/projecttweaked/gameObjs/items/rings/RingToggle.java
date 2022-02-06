package main.java.quartzshard.projecttweaked.gameObjs.items.rings;

import main.java.quartzshard.projecttweaked.api.PESounds;
import main.java.quartzshard.projecttweaked.api.item.IModeChanger;
import main.java.quartzshard.projecttweaked.gameObjs.items.ItemPE;
import main.java.quartzshard.projecttweaked.utils.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class RingToggle extends ItemPE implements IModeChanger
{
	public RingToggle(String unlocalName)
	{
		this.setTranslationKey(unlocalName);
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.addPropertyOverride(ACTIVE_NAME, ACTIVE_GETTER);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}

	@Override
	public byte getMode(@Nonnull ItemStack stack)
	{
		return ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE) ? (byte) 1 : 0;
	}

	@Override
	public boolean changeMode(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		if (!ItemHelper.getOrCreateCompound(stack).getBoolean(TAG_ACTIVE))
		{
			player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.PLAYERS, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, true);
		}
		else
		{
			player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.UNCHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			stack.getTagCompound().setBoolean(TAG_ACTIVE, false);
		}
		return true;
	}
}
