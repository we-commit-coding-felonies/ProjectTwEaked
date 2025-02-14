package main.java.quartzshard.projecttweaked.gameObjs.items;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Internal interface for PlayerChecks.
 */
public interface IFireProtector
{
    /**
     * @return If this stack currently should protect the bearer from fire
     */
    boolean canProtectAgainstFire(ItemStack stack, EntityPlayerMP player);
}
