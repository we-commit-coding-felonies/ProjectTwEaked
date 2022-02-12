package quartzshard.projecttweaked.gameObjs.items.armor;

import quartzshard.projecttweaked.PECore;
import quartzshard.projecttweaked.gameObjs.ObjHandler;
import quartzshard.projecttweaked.api.capabilities.IKnowledgeProvider;
import quartzshard.projecttweaked.api.PESounds;
import quartzshard.projecttweaked.api.ProjectTwEakedAPI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class GemArmorBase extends ItemArmor implements ISpecialArmor
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


	@SubscribeEvent
	public void onGetHurt(LivingHurtEvent event)
	{	
		Entity hurt = event.getEntity();
		if (hurt.world.isRemote) {
            return;
        }
		if (hurt instanceof EntityPlayer) 
		{
			event.setCanceled(shieldWithEMC((EntityPlayer)hurt, event.getAmount()));
		}
		return;
	}

	@SubscribeEvent
	public void onAttacked(LivingHurtEvent event)
	{	
		Entity hurt = event.getEntity();
		if (hurt.world.isRemote) {
            return;
        }
		if (hurt instanceof EntityPlayer) 
		{
			event.setCanceled(shieldWithEMC((EntityPlayer)hurt, event.getAmount()));
		}
		return;
	}
	
	@SubscribeEvent
	public void onPlayerAttacked(LivingAttackEvent event)
	{	
		Entity hurt = event.getEntity();
		if (hurt.world.isRemote) {
            return;
        }
		if (hurt instanceof EntityPlayer) 
		{
			event.setCanceled(shieldWithEMC((EntityPlayer)hurt, event.getAmount()));
		}
		return;
	}
	public boolean shieldWithEMC(EntityPlayer player, float damage)
	{
		IKnowledgeProvider provider = player.getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null);
		if (GemArmorBase.hasFullSet(player))
		{
			if (damage * 64 < provider.getEmc())
			{
				provider.setEmc(provider.getEmc() - (long)(damage*64));
				provider.sync((EntityPlayerMP)player);
				player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.UNCHARGE, SoundCategory.PLAYERS, 0.5F, 0.5F);
				return true;
			}
			else 
			{
				provider.setEmc(0);
				provider.sync((EntityPlayerMP)player);
			}
		}
		return false;
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
	public boolean handleUnblockableDamage(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot)
    {
		IKnowledgeProvider provider = entity.getCapability(ProjectTwEakedAPI.KNOWLEDGE_CAPABILITY, null);
		if (provider.getEmc() > 0) 
		{
			return GemArmorBase.hasFullSet((EntityPlayer)entity);
		}
		else 
		{
			return false;
		}
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
