package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IAlchShield;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.ItemPE;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GemArmorBase extends MatterArmor implements IAlchShield
{
	public GemArmorBase(EntityEquipmentSlot armorType)
	{
		super(armorType, ProjectEConfig.matterArmors.gemArmorResistance, 0.55);
		this.setCreativeTab(ObjHandler.cTab);
		this.setTranslationKey("pe_gem_armor_" + armorType.getIndex());
		this.setMaxDamage(ProjectEConfig.matterArmors.gemArmorDurability);
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

	public static boolean hasFullUndamagedSet(EntityPlayer player)
	{
		for (ItemStack i : player.inventory.armorInventory)
		{
			if (i.isEmpty() || !(i.getItem() instanceof GemArmorBase) || i.isItemDamaged() )
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean shieldCondition(EntityPlayer player, int slot, ItemStack stack)
	{
		return GemArmorBase.hasFullUndamagedSet(player) && ProjectEConfig.matterArmors.gemArmorBarrier;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((GemArmorBase) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
		if (entity instanceof EntityPlayer) {
			if (stack.attemptDamageItem((damage * 10), entity.world.rand, null)) {
				NBTTagCompound entData = entity.getEntityData();
				if (ProjectEConfig.matterArmors.gemDowngrade) {entData.setByte("pe_gem_num_replacements", (byte) Math.min(4, (entData.getByte("pe_gem_num_replacements") + 1)));}
				// TODO: this is a bit silly, if we can find a better way of doing this that would be good
			} else {
				if (stack.getMaxDamage() - stack.getItemDamage() <= 1) {
					stack.setItemDamage(stack.getMaxDamage() - 2);
					return;
				}
				stack.damageItem(-1, entity);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		char index = this.armorType == EntityEquipmentSlot.LEGS ? '2' : '1';
		return PECore.MODID + ":textures/armor/gem_" + index + ".png";
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (stack.isItemDamaged() && ProjectEConfig.matterArmors.gemAutoRepair) {
			if (ItemPE.consumeFuel(player, stack, 16384, true)) {
				stack.damageItem(-1, player);
			}
		}
	}
}
