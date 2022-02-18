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
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GemArmorBase extends MatterArmor implements IAlchShield
{
	public GemArmorBase(EntityEquipmentSlot armorType)
	{
		super(armorType, 0.99, 0.55);
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
		//else
		//{	
		//	//Handle the breaking and replacing
		//	System.out.println(slot);
		//	entity.renderBrokenItemStack(stack);
		//	stack.setItemDamage(0);
		//	stack.shrink(1);
		//	ItemStack newStack = null;
		//	switch (armorType) {
		//		case HEAD:
		//			newStack = new ItemStack(ObjHandler.rmHelmet);
		//			break;
		//		case CHEST:
		//			newStack = new ItemStack(ObjHandler.rmChest);
		//			break;
		//		case LEGS:
		//			newStack = new ItemStack(ObjHandler.rmLegs);
		//			break;
		//		case FEET:
		//			newStack = 	new ItemStack(ObjHandler.rmFeet);
		//			break;
		//		default:
		//			PECore.LOGGER.error("GemArmorBase is somehow doing something with armorType: " + armorType);
		//			PECore.LOGGER.error("Please report this bug to ProjectTwEaked!");
		//			break;
		//	}
		//	EntityPlayer player = (EntityPlayer)entity;
		//	IItemHandler inv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		//	inv.insertItem(slot, newStack, false);
		//	System.out.println(player.inventoryContainer.inventorySlots);
		//	InvWrapper
		//}
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
