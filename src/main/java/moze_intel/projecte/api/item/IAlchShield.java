package moze_intel.projecte.api.item;

import java.util.Arrays;
import java.util.List;

import moze_intel.projecte.PECore;
import moze_intel.projecte.api.PESounds;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Loader;

public interface IAlchShield {



    /**
     * Specifies the condition(s), if any, that need to be met for the shield to be active.
     * 
     * @param player The player wearing the shield item(s)
	 * @param slot The inventory slot the shield occupies
     * @return boolean specifying whether or not to shield the player
     */
    boolean shieldCondition(EntityPlayer player, int slot, ItemStack stack);

    /**
     * Handles the cancelling of damage that is blocked by the shield.
     * Should be implemented with the SubscribeEvent annotation.
     * @param event The LivingAttackEvent that we're shielding
     */
	default void onPlayerAttacked(LivingAttackEvent event, int slot, ItemStack stack)
	{	
        Entity hurt = event.getEntity();
		if (hurt.world.isRemote || !ProjectEConfig.alchemicalBarrier.enableEMCShield) {
            return;
        }
		if (hurt instanceof EntityPlayer) 
		{
			if (event.isCanceled()) return;
			event.setCanceled(shieldWithEMC((EntityPlayer)hurt, event.getAmount(), event.getSource(), slot, stack));
		}
		return;
	}

    /**
     * Tests whether the player can afford to cancel the damage with their EMC network.
     * @param player The Player using the shield
     * @param damage The amount of damage to block
     * @param source The DamageSource of the damage
     * @return whether to cancel the damage, boolean
     */
	default boolean shieldWithEMC(EntityPlayer player, float damage, DamageSource source , int slot, ItemStack stack)
	{
		int ouchieModifier = 1;
        if (ProjectEConfig.alchemicalBarrier.debugBarrier) 
        {
			PECore.LOGGER.info("*** ALCHEMICAL BARRIER DEBUG START ***");
            PECore.LOGGER.info("Logged attack event");
			PECore.LOGGER.info("Will the player be shielded?: " + shieldCondition(player, slot, stack));
			PECore.LOGGER.info("Extra information about this attack:");
			PECore.LOGGER.info("Name of attacked player: " + player.getName());
			PECore.LOGGER.info("UUID of attacked player: " + player.getUniqueID());
			PECore.LOGGER.info("Type of damage dealt: " + source.getDamageType());
			PECore.LOGGER.info("Amount of damage dealt: " + damage);
			PECore.LOGGER.info("Current EMC in player inventory: " + EMCHelper.checkPlayerFuel(player));
			PECore.LOGGER.info("Cost to the player: " + Math.pow(ProjectEConfig.alchemicalBarrier.emcShieldCost + damage, 2));
			PECore.LOGGER.info("*** ALCHEMICAL BARRIER DEBUG END ***");
        }
		if (!shieldCondition(player, slot, stack)) {
			return false;
		}

		if (Loader.isModLoaded("avaritia") && source.getTrueSource() instanceof EntityPlayer) {
			EntityPlayer attacker = (EntityPlayer) source.getTrueSource();
			if (!attacker.getHeldItemMainhand().isEmpty() && attacker.getHeldItemMainhand().getItem().equals(Item.getByNameOrId("avaritia:infinity_sword"))) {
				ouchieModifier = 1337;
			}
		}

		int costPerDamage = ProjectEConfig.alchemicalBarrier.emcShieldCost;
		if (checkListForDamageType(source.getDamageType())) {
			long cost = (long) Math.pow((damage + costPerDamage), 2 * ouchieModifier);
			if (ProjectEConfig.alchemicalBarrier.pullFromTablet) {
				IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);	
				if (cost < 0)
				{
					cost *= -1;
				}
				if (cost <= provider.getEmc() && provider.getEmc() > 0)
				{
					provider.setEmc(provider.getEmc() - cost);
					provider.sync((EntityPlayerMP)player);
					if (!ProjectEConfig.alchemicalBarrier.suppressBarrierNoise) {
						player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, PESounds.PROTECT, SoundCategory.PLAYERS, 0.45F, 1.0F);
					}
					return true;
				}
				else
				{
					switch (ProjectEConfig.alchemicalBarrier.lowEMCMode) {
						case 0:
							if (provider.getEmc() <= 0) {
								break;
							}
							float affordableDamage = provider.getEmc() / costPerDamage;
							provider.setEmc(0);
							provider.sync((EntityPlayerMP)player);
							player.attackEntityFrom(source, damage - affordableDamage);
							if (!ProjectEConfig.alchemicalBarrier.suppressBarrierNoise) {
								player.world.playSound(null, player.posX, player.posY, player.posZ, PESounds.PROTECTFAIL, SoundCategory.PLAYERS, 1.5F, 1.0F);
							}
							return true;
						case 1:
							break;
					}
					return false;
				}
			} else 
			{
				long total = EMCHelper.checkPlayerFuel(player);

				if (cost <= total && total > 0) {
					long consumed = EMCHelper.consumePlayerFuel(player, cost);
					if (!ProjectEConfig.alchemicalBarrier.suppressBarrierNoise) {
						if ( consumed > cost )
						{
							player.world.playSound(null, player.posX, player.posY, player.posZ, PESounds.WASTE, SoundCategory.PLAYERS, 0.45F, 1.0F);
						}
						if ( EMCHelper.checkPlayerFuel(player) > 0)
						{
							player.world.playSound(null, player.posX, player.posY, player.posZ, PESounds.PROTECT, SoundCategory.PLAYERS, 0.45F, 1.0F);
						}
						else
						{
							player.world.playSound(null, player.posX, player.posY, player.posZ, PESounds.PROTECTFAIL, SoundCategory.PLAYERS, 1.5F, 1.0F);
						}

					}
					return true;
				} 
				else 
				{
					switch (ProjectEConfig.alchemicalBarrier.lowEMCMode) {
						case 0:
							if (total <= 0) {
								break;
							}
							float affordableDamage = total / costPerDamage;
							EMCHelper.consumePlayerFuel(player, total);
							player.attackEntityFrom(source, damage - affordableDamage);
							if (!ProjectEConfig.alchemicalBarrier.suppressBarrierNoise) {
								player.world.playSound(null, player.posX, player.posY, player.posZ, PESounds.PROTECTFAIL, SoundCategory.PLAYERS, 1.5F, 1.0F);
							}
							return true;
						case 1:
							break;
					}
					return false;
				}
			}
			
		}
		return false;
	}

    /**
     * Check to see whether the damagetype is blockable, as per the config
     * @param type String of the damage type
     * @return boolean, does this type get blocked
     */
	default boolean checkListForDamageType(String type) {
		List<String> typeList = Arrays.asList(ProjectEConfig.alchemicalBarrier.dmgTypesList);
		if (ProjectEConfig.alchemicalBarrier.typesIsWhitelist) {
			if (typeList.contains(type)) {
				return true;
			}
			return false;
		}
		if (!typeList.contains(type)) {
			return true;
		}
		return false;
	}    
}
