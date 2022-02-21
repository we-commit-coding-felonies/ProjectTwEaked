package moze_intel.projecte.gameObjs.items.rings;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import moze_intel.projecte.api.PESounds;
import moze_intel.projecte.api.item.IModeChanger;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.utils.ItemHelper;

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
		NBTTagCompound tag  = ItemHelper.getOrCreateCompound(stack);
		if (!tag.getBoolean(TAG_ACTIVE)) {
			player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.HEAL, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		else {
			player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.UNCHARGE, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
		tag.setBoolean(TAG_ACTIVE, !tag.getBoolean(TAG_ACTIVE));
		return true;
	}
}
