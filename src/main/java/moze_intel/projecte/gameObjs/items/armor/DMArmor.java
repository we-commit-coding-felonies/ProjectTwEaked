package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import moze_intel.projecte.PECore;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.ObjHandler;

public class DMArmor extends MatterArmor
{
	public DMArmor(EntityEquipmentSlot armorPiece)
	{
		super(armorPiece, ProjectEConfig.matterArmors.dmArmorResistance, 0.85);
		this.setCreativeTab(ObjHandler.cTab);
		this.setTranslationKey("pe_dm_armor_" + armorPiece.getIndex());
		this.setMaxDamage(ProjectEConfig.matterArmors.dmArmorDurability);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((DMArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture (ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		char index = this.armorType == EntityEquipmentSlot.LEGS ? '2' : '1';
		return PECore.MODID + ":textures/armor/darkmatter_"+index+".png";
	}
}
