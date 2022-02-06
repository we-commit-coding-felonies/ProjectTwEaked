package main.java.quartzshard.projecttweaked.gameObjs.items;

import main.java.quartzshard.projecttweaked.api.item.IProjectileShooter;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public class CataliticLens extends DestructionCatalyst implements IProjectileShooter
{
	public CataliticLens() 
	{
		this.setTranslationKey("catalitic_lens");
	}
	
	@Override
	public boolean shootProjectile(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, EnumHand hand)
	{
		return ((IProjectileShooter) ObjHandler.hyperLens).shootProjectile(player, stack, hand);
	}

	@Override
	public int getNumCharges(@Nonnull ItemStack stack)
	{
		return 7;
	}
}
