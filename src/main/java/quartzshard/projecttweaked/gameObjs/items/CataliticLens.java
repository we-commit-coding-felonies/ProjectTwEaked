package quartzshard.projecttweaked.gameObjs.items;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import quartzshard.projecttweaked.api.item.IProjectileShooter;
import quartzshard.projecttweaked.gameObjs.ObjHandler;

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
