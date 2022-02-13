package quartzshard.projecttweaked.api.event;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired on both the server and client when a player is attempting to place an item in the condenser.
 * This event is cancelable
 * This event is fired on MinecraftForge#EVENT_BUS
 */
@Cancelable
public class PlayerAttemptCondenserSetEvent extends Event
{
    private final EntityPlayer player;
    private final ItemStack stack;

    public PlayerAttemptCondenserSetEvent(@Nonnull EntityPlayer entityPlayer, @Nonnull ItemStack stack)
    {
        player = entityPlayer;
        this.stack = stack;
    }

    /**
     * @return The player who is attempting to put in the condenser slot.
     */
    @Nonnull
    public EntityPlayer getPlayer()
    {
        return player;
    }

    /**
     * @return The stack that the player is trying to learn.
     */
    @Nonnull
    public ItemStack getStack()
    {
        return stack;
    }
}