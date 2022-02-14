package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IAlchShield;
import moze_intel.projecte.gameObjs.ObjHandler;

public abstract class GemArmorBase extends ItemArmor implements ISpecialArmor, IAlchShield
{
	public GemArmorBase(EntityEquipmentSlot armorType)
	{
		super(ArmorMaterial.DIAMOND, 0, armorType);
		this.setCreativeTab(ObjHandler.cTab);
		this.setTranslationKey("pe_gem_armor_" + armorType.getIndex());
		this.setMaxDamage(0);
	}

	public static boolean hasAnyPiece(EntityPlayer player)
	{
		for (ItemStack i : player.inventory.armorInventory)
		{
			if (!i.isEmpty() && i.getItem() instanceof GemArmorBase)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean hasFullSet(EntityPlayer player)
	{
		for (ItemStack i : player.inventory.armorInventory)
		{
			if (i.isEmpty() || !(i.getItem() instanceof GemArmorBase))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
	{	
		EntityEquipmentSlot type = ((GemArmorBase) armor.getItem()).armorType;
		int priority, max;
		double ratio;

		priority = source.isExplosion() ? 1 : 0;

		if (type == EntityEquipmentSlot.CHEST || type == EntityEquipmentSlot.LEGS)
		{
			ratio = 0.3D;
		} 
		else 
		{
			ratio = 0.2D;
		}
		
		if (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET)
		{
			max = 250;
		} 
		else 
		{
			max = 350;
		}
		
		return new ArmorProperties(priority, ratio, max);
	}

	@Override
	public boolean shieldCondition(EntityPlayer player, int slot)
	{	
		return GemArmorBase.hasFullSet(player);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((GemArmorBase) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		char index = this.armorType == EntityEquipmentSlot.LEGS ? '2' : '1';
		return PECore.MODID + ":textures/armor/gem_" + index + ".png";
	}
}
