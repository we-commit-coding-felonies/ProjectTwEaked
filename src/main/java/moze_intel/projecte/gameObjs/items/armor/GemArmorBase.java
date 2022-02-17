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
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class GemArmorBase extends MatterArmor implements IAlchShield
{
	public GemArmorBase(EntityEquipmentSlot armorType)
	{
		super(armorType, 1);
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
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
	{	
		return new ArmorProperties(2, 1, 10000);
	}

	@Override
	public boolean shieldCondition(EntityPlayer player, int slot, ItemStack stack)
	{
		return GemArmorBase.hasFullUndamagedSet(player);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((GemArmorBase) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {

		if ( (this.getDamage(stack) + damage*10) < this.getMaxDamage(stack) )
		{
			stack.damageItem(damage * 10, entity);
		}
		else
		{	
			//Handle the breaking and replacing
			//stack.shrink(1);
			/* ItemStack newStack = new ItemStack(Items.DIAMOND_CHESTPLATE);
			EntityPlayer player = (EntityPlayer)entity;
			IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
			inv.insertItem(slot, newStack, false); */
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
