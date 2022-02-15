package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

public class MatterArmor extends ItemArmor implements ISpecialArmor
{
    public double resistance;
	public MatterArmor(EntityEquipmentSlot armorPiece, double fullResistance)
	{
		super(ArmorMaterial.DIAMOND, 0, armorPiece);
        this.resistance = fullResistance / 4;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
	{
		double scaledResistance = this.resistance * (this.getDamage(armor) / this.getMaxDamage(armor));
		return new ArmorProperties(1, scaledResistance, (int) damage);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((MatterArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {}
}
