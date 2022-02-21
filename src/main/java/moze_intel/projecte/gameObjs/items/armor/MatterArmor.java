package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ISpecialArmor;

public class MatterArmor extends ItemArmor implements ISpecialArmor
{
    public double resistance;
	public double unblockableEffectiveness;
	public MatterArmor(EntityEquipmentSlot armorPiece, double fullResistance, double unblockablePenalty)
	{
		super(ArmorMaterial.DIAMOND, 0, armorPiece);
        this.resistance = fullResistance / 4;
		this.unblockableEffectiveness = 1.0 - unblockablePenalty;
	}

	@Override
	public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
		return true;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
	{
		if (source.isUnblockable()) {
			return new ArmorProperties(1, this.resistance * this.unblockableEffectiveness, (int) damage);
		}
		return new ArmorProperties(1, this.resistance, (int) damage);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((MatterArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.hsvToRGB((Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack)) / 4.66297083052F) + 0.3911f), 1.0f, 0.824f);
	}
}
