package main.java.quartzshard.projecttweaked.integration.jei.mappers;

import main.java.quartzshard.projecttweaked.emc.FuelMapper;
import main.java.quartzshard.projecttweaked.emc.SimpleStack;
import main.java.quartzshard.projecttweaked.integration.jei.collectors.CollectorRecipeCategory;
import main.java.quartzshard.projecttweaked.integration.jei.collectors.FuelUpgradeRecipe;
import main.java.quartzshard.projecttweaked.utils.EMCHelper;
import net.minecraft.item.ItemStack;

public class JEIFuelMapper extends JEICompatMapper<FuelUpgradeRecipe>
{
    public JEIFuelMapper()
    {
        super(CollectorRecipeCategory.UID);
    }

    public void refresh()
    {
        clear();
        for(SimpleStack stack : FuelMapper.getFuelMap())
        {
            ItemStack fuelUpgrade = FuelMapper.getFuelUpgrade(stack.toItemStack());
            if (EMCHelper.getEmcValue(stack.toItemStack()) <= EMCHelper.getEmcValue(fuelUpgrade))
            {
                addRecipe(new FuelUpgradeRecipe(stack.toItemStack(), fuelUpgrade));
            }
        }
    }
}