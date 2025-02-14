package main.java.quartzshard.projecttweaked.impl;

import com.google.common.base.Preconditions;
import main.java.quartzshard.projecttweaked.PECore;
import main.java.quartzshard.projecttweaked.api.proxy.IEMCProxy;
import main.java.quartzshard.projecttweaked.emc.mappers.APICustomEMCMapper;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

import javax.annotation.Nonnull;

public class EMCProxyImpl implements IEMCProxy
{
    public static final IEMCProxy instance = new EMCProxyImpl();

    private EMCProxyImpl() {}

    @Override
    public void registerCustomEMC(@Nonnull ItemStack stack, long value)
    {
        Preconditions.checkNotNull(stack);
        boolean flag = Loader.instance().isInState(LoaderState.PREINITIALIZATION) || Loader.instance().isInState(LoaderState.INITIALIZATION) || Loader.instance().isInState(LoaderState.POSTINITIALIZATION);
        Preconditions.checkState(flag, String.format("Mod %s tried to register EMC at an invalid time!", Loader.instance().activeModContainer().getModId()));
        APICustomEMCMapper.instance.registerCustomEMC(stack, value);
        PECore.debugLog("Mod {} registered emc value {} for itemstack {}", Loader.instance().activeModContainer().getModId(), value, stack.toString());
    }

    @Override
    public void registerCustomEMC(@Nonnull Object o, long value)
    {
        Preconditions.checkNotNull(o);
        boolean flag = Loader.instance().isInState(LoaderState.PREINITIALIZATION) || Loader.instance().isInState(LoaderState.INITIALIZATION) || Loader.instance().isInState(LoaderState.POSTINITIALIZATION);
        Preconditions.checkState(flag, String.format("Mod %s tried to register EMC at an invalid time!", Loader.instance().activeModContainer().getModId()));
        APICustomEMCMapper.instance.registerCustomEMC(o, value);
        PECore.debugLog("Mod {} registered emc value {} for Object {}", Loader.instance().activeModContainer().getModId(), value, o);
    }

    @Override
    public boolean hasValue(@Nonnull Block block)
    {
        Preconditions.checkNotNull(block);
        return EMCHelper.doesBlockHaveEmc(block);
    }

    @Override
    public boolean hasValue(@Nonnull Item item)
    {
        Preconditions.checkNotNull(item);
        return EMCHelper.doesItemHaveEmc(item);
    }

    @Override
    public boolean hasValue(@Nonnull ItemStack stack)
    {
        Preconditions.checkNotNull(stack);
        return EMCHelper.doesItemHaveEmc(stack);
    }

    @Override
    public long getValue(@Nonnull Block block)
    {
        Preconditions.checkNotNull(block);
        return EMCHelper.getEmcValue(block);
    }

    @Override
    public long getValue(@Nonnull Item item)
    {
        Preconditions.checkNotNull(item);        
        return EMCHelper.getEmcValue(item);
    }

    @Override
    public long getValue(@Nonnull ItemStack stack)
    {
        Preconditions.checkNotNull(stack);
        return EMCHelper.getEmcValue(stack);
    }

    @Override
    public long getSellValue(@Nonnull ItemStack stack)
    {
        Preconditions.checkNotNull(stack);
        return EMCHelper.getEmcSellValue(stack);
    }
}
