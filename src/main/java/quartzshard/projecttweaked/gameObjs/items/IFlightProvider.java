package main.java.quartzshard.projecttweaked.gameObjs.items;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Internal interface for PlayerChecks.
 */
public interface IFlightProvider
{
    /**
     * @return If this stack currently should provide its bearer flight
     */
    boolean canProvideFlight(ItemStack stack, EntityPlayerMP player);
}
