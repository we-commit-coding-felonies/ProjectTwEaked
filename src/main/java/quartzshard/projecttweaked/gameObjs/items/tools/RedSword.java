package quartzshard.projecttweaked.gameObjs.items.tools;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import quartzshard.projecttweaked.utils.PlayerHelper;

public class RedSword extends DarkSword
{
	public RedSword() 
	{
		super("rm_sword", (byte)3, new String[]{
				"pe.redsword.mode1",
				"pe.redsword.mode2"
		});
		this.setNoRepair();
		this.peToolMaterial = "rm_tools";
		this.toolClasses.add("sword");
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase damaged, EntityLivingBase damager)
	{
		attackWithCharge(stack, damaged, damager, 1.0F);
		return true;
	}

	@Override
	public boolean doExtraFunction(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, EnumHand hand)
	{
		if (player.getCooledAttackStrength(0F) == 1)
		{
			attackAOE(stack, player, getMode(stack) == 1, REDSWORD_BASE_ATTACK, 0, hand);
			PlayerHelper.resetCooldown(player);
			return true;
		}
		else
		{
			return false;
		}
	}
}
