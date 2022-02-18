package moze_intel.projecte.gameObjs.items.armor;

import java.util.List;

import javax.annotation.Nonnull;

import moze_intel.projecte.PECore;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.utils.ItemHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IGoggles;

@Optional.Interface(iface = "thaumcraft.api.items.IGoggles", modid = "Thaumcraft")
public class RMArmor extends MatterArmor implements IGoggles
{
	public int maxWear;
	public RMArmor(EntityEquipmentSlot armorType)
	{
		super(armorType, 0.95, 0.7);
		this.setCreativeTab(ObjHandler.cTab);
		this.setTranslationKey("pe_rm_armor_" + armorType.getIndex());
		this.setMaxDamage(0);
		this.maxWear = 10000;
	}
	
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
	{
		double duraLeft = 1.0 - getDurabilityForDisplay(armor);
		System.out.println("Armor Type: " + this.armorType);
		System.out.println("Unblockable: " + source.isUnblockable());
		System.out.println("Damage: " + damage);
		System.out.println("Armor Durability: " + duraLeft);
		System.out.println("Unblockable Effectiveness: " + this.unblockableEffectiveness);
		if (source.isUnblockable()) {
			System.out.println("'ratio': " + (this.resistance * duraLeft) * this.unblockableEffectiveness);
			return new ArmorProperties(1, (this.resistance * duraLeft) * this.unblockableEffectiveness, (int) damage);
		}
		System.out.println("'ratio': " + this.resistance * duraLeft);
		return new ArmorProperties(1, this.resistance * duraLeft, (int) damage);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot)
	{
		EntityEquipmentSlot type = ((RMArmor) armor.getItem()).armorType;
		return (type == EntityEquipmentSlot.HEAD || type == EntityEquipmentSlot.FEET) ? 4 : 6;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
		NBTTagCompound stackTag = ItemHelper.getOrCreateCompound(stack);
		if (!stackTag.hasKey("pe_wear")) {
			stackTag.setInteger("pe_wear", 1);
		} else if (stackTag.getInteger("pe_wear") < this.maxWear) {
			System.out.println("damage: " + damage);
			System.out.println("tern: " + (source.isUnblockable() ? 0.01 : 1));
			System.out.println("damage/tern: " + (damage / (source.isUnblockable() ? 0.01 : 1)));
			stackTag.setInteger("pe_wear", (int) (stackTag.getInteger("pe_wear") + 9 + (damage / (source.isUnblockable() ? 0.01 : 1))));
		}
		if (stackTag.getInteger("pe_wear") > this.maxWear) {
			stackTag.setInteger("pe_wear", this.maxWear);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getArmorTexture (ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
	{
		char index = this.armorType == EntityEquipmentSlot.LEGS ? '2' : '1';
		return PECore.MODID + ":textures/armor/redmatter_"+index+".png";
	}

	@Override
	@Optional.Method(modid = "Thaumcraft")
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) 
	{
		return ((RMArmor) itemstack.getItem()).armorType == EntityEquipmentSlot.HEAD;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		NBTTagCompound stackTag = ItemHelper.getOrCreateCompound(stack);
		if (stackTag.getInteger("pe_wear") > 0 && world.getTotalWorldTime() % (int) ( 3 + ( 10 * getDurabilityForDisplay(stack) ) ) == 0) {
			stackTag.setInteger("pe_wear", stackTag.getInteger("pe_wear") - 1);
		}
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		if (ItemHelper.getOrCreateCompound(stack).hasKey("pe_wear")) {
			return (double) ItemHelper.getOrCreateCompound(stack).getInteger("pe_wear") / this.maxWear;
		}
        return 0.0;
    }

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return ItemHelper.getOrCreateCompound(stack).getInteger("pe_wear") > 0;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> tooltips, ITooltipFlag flags)
    {
		NBTTagCompound stackTag = ItemHelper.getOrCreateCompound(stack);
        tooltips.add(I18n.format("pe.rmarmor.wear", stackTag.getInteger("pe_wear"), this.maxWear));
    }
}
