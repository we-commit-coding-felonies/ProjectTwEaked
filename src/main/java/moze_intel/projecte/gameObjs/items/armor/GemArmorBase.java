package moze_intel.projecte.gameObjs.items.armor;

import javax.annotation.Nonnull;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.item.IAlchShield;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.utils.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.InvWrapper;

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
		if (entity instanceof EntityPlayer) {
			if (stack.attemptDamageItem((damage * 10), entity.world.rand, null)) {
				NBTTagCompound entData = entity.getEntityData();
				entData.setByte("pe_gem_num_replacements", (byte) Math.min(4, (entData.getByte("pe_gem_num_replacements") + 1))); // TODO: literally anything but this
				//if (entData.getByte("pe_broken_armor_types") < 15 && entData.getByte("pe_broken_armor_types") >= 0) {
				//	switch (this.armorType) {
				//		case HEAD:
				//			entData.setByte("pe_broken_armor_types", (byte) (entData.getByte("pe_broken_armor_types") + 8));
				//			break;
				//		case CHEST:
				//			entData.setByte("pe_broken_armor_types", (byte) (entData.getByte("pe_broken_armor_types") + 4));
				//			break;
				//		case LEGS:
				//			entData.setByte("pe_broken_armor_types", (byte) (entData.getByte("pe_broken_armor_types") + 2));
				//			break;
				//		case FEET:
				//			entData.setByte("pe_broken_armor_types", (byte) (entData.getByte("pe_broken_armor_types") + 1));
				//			break;
				//		default:
				//			PECore.LOGGER.error("GemArmorBase has encountered unknown armor type: " + this.armorType);
				//			PECore.LOGGER.error("Please report this bug to ProjectTwEaked!");
				//			break;
				//	}
				//} else {
				//	PECore.LOGGER.error("NBT Tag 'pe_broken_armor_types' on entity " + entity + " had an unexpected value of " + entData.getByte("pe_broken_armor_types") + "!");
				//	PECore.LOGGER.error("This value should ALWAYS be between 0 and 15! Please report this to the ProjectTwEaked devs ASAP!");
				//	entData.removeTag("pe_broken_armor_types");
				//	entData.removeTag("pe_broke_gem_armor");
				//	PECore.LOGGER.error("The tag has been removed from the entity to prevent unintended behavior. SERIOUSLY, PLEASE REPORT THIS TO THE DEVS!");
				//}
			} else {
				if (stack.getMaxDamage() - stack.getItemDamage() <= 1) {
					stack.setItemDamage(stack.getMaxDamage() - 2);
					return;
				}
				stack.damageItem(-1, entity);
			}
			//if (stack.getCount() < 1) {
			//	ItemStack newStack = null;
			//switch (this.armorType) {
			//	case HEAD:
			//		newStack = new ItemStack(ObjHandler.rmHelmet);
			//		slot = 39;
			//		System.out.println("head");
			//		break;
			//	case CHEST:
			//		newStack = new ItemStack(ObjHandler.rmChest);
			//		slot = 38;
			//		System.out.println("body");
			//		break;
			//	case LEGS:
			//		newStack = new ItemStack(ObjHandler.rmLegs);
			//		slot = 37;
			//		System.out.println("legs");
			//		break;
			//	case FEET:
			//		newStack = new ItemStack(ObjHandler.rmHelmet);
			//		slot = 36;
			//		System.out.println("feet");
			//		break;
			//	default:
			//		PECore.LOGGER.error("GemArmorBase is somehow doing something with armorType: " + this.armorType);
			//		PECore.LOGGER.error("Please report this bug to ProjectTwEaked!");
			//		newStack = new ItemStack(Items.DIAMOND); //placeholder item in case game doesnt like null items
			//		break;
			//}
			//	ItemHelper.getOrCreateCompound(newStack).setInteger("pe_wear", Math.min(9999, damage * 100));
			//	//entity.getEntityData().setBoolean("pe_broke_gem_armor", true);
			//	System.out.println(this.armorType);
			//}
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

	//public void downgradeToRM(ItemStack stack, EntityPlayer player, int damage) {
	//	System.out.println("downgrading");
	//	player.renderBrokenItemStack(stack);
	//	ItemStack newStack = null;
	//	int slot = 0;
	//	switch (this.armorType) {
	//		case HEAD:
	//			newStack = new ItemStack(ObjHandler.rmHelmet);
	//			slot = 39;
	//			System.out.println("head");
	//			break;
	//		case CHEST:
	//			newStack = new ItemStack(ObjHandler.rmChest);
	//			slot = 38;
	//			System.out.println("body");
	//			break;
	//		case LEGS:
	//			newStack = new ItemStack(ObjHandler.rmLegs);
	//			slot = 37;
	//			System.out.println("legs");
	//			break;
	//		case FEET:
	//			newStack = 	new ItemStack(ObjHandler.rmFeet);
	//			slot = 36;
	//			System.out.println("feet");
	//			break;
	//		default:
	//			PECore.LOGGER.error("GemArmorBase is somehow doing something with armorType: " + this.armorType);
	//			PECore.LOGGER.error("Please report this bug to ProjectTwEaked!");
	//			newStack = new ItemStack(Items.DIAMOND); //placeholder item in case game doesnt like null items
	//			break;
	//	}
	//	System.out.println("switch case complete: " + newStack + " will be put in slot " + slot);
	//	newStack.setCount(2);
	//	System.out.println("stack size is: " + newStack.getCount());
	//	ItemHelper.getOrCreateCompound(newStack).setInteger("pe_wear", Math.min(9999, damage * 100));
	//	InvWrapper playerInv = new InvWrapper(player.inventory);
	//	playerInv.setStackInSlot(slot, newStack);
	//	System.out.println("end of downgradeToRM function, itemstack in slot " + slot + " is: " + playerInv.getStackInSlot(slot));
	//}
}
