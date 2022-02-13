package quartzshard.projecttweaked.integration.jei.mappers;

import net.minecraft.item.ItemStack;
import quartzshard.projecttweaked.emc.FuelMapper;
import quartzshard.projecttweaked.emc.SimpleStack;
import quartzshard.projecttweaked.integration.jei.collectors.CollectorRecipeCategory;
import quartzshard.projecttweaked.integration.jei.collectors.FuelUpgradeRecipe;
import quartzshard.projecttweaked.utils.EMCHelper;

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