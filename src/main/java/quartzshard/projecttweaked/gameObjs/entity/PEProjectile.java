package main.java.quartzshard.projecttweaked.gameObjs.entity;

import main.java.quartzshard.projecttweaked.gameObjs.items.ItemPE;
import main.java.quartzshard.projecttweaked.utils.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class PEProjectile extends EntityThrowable
{
    public PEProjectile(World world)
    {
        super(world);
    }

    public PEProjectile(World world, EntityPlayer entity)
    {
        super(world, entity);
    }

    public PEProjectile(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }


    @Override
    protected final void onImpact(@Nonnull RayTraceResult mop)
    {
        if (getThrower() instanceof EntityPlayerMP)
        {
            apply(mop);
        }
        if (!world.isRemote)
        {
            this.setDead();
        }
    }

    @Override
    public float getGravityVelocity()
    {
        return 0;
    }

    protected abstract void apply(RayTraceResult mop);

    protected final boolean tryConsumeEmc(ItemPE consumeFrom, long amount)
    {
        EntityPlayer player = ((EntityPlayer) getThrower());
        ItemStack found = PlayerHelper.findFirstItem(player, consumeFrom);
        return !found.isEmpty() && ItemPE.consumeFuel(player, found, amount, true);
    }
}
