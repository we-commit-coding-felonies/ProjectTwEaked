package main.java.quartzshard.projecttweaked.integration.jei;

import main.java.quartzshard.projecttweaked.integration.jei.collectors.CollectorRecipeCategory;
import main.java.quartzshard.projecttweaked.integration.jei.mappers.JEICompatMapper;
import main.java.quartzshard.projecttweaked.integration.jei.mappers.JEIFuelMapper;
import main.java.quartzshard.projecttweaked.integration.jei.world_transmute.WorldTransmuteRecipeCategory;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import main.java.quartzshard.projecttweaked.gameObjs.ObjHandler;
import main.java.quartzshard.projecttweaked.gameObjs.container.PhilosStoneContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class PEJeiPlugin implements IModPlugin
{
    public static IJeiRuntime RUNTIME = null;
    private static List<JEICompatMapper> mappers = new ArrayList<>();

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {}

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registry) {}

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new WorldTransmuteRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CollectorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(@Nonnull IModRegistry registry)
    {
        // todo finish this, add alchbag
        registry.addRecipes(WorldTransmuteRecipeCategory.getAllTransmutations(), WorldTransmuteRecipeCategory.UID);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(PhilosStoneContainer.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);

        registry.addRecipeCatalyst(new ItemStack(ObjHandler.philosStone), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.philosStone), WorldTransmuteRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.collectorMK0), CollectorRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.collectorMK1), CollectorRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.collectorMK2), CollectorRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.collectorMK3), CollectorRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ObjHandler.collectorMK4), CollectorRecipeCategory.UID);

        mappers.add(new JEIFuelMapper());
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
    {
        RUNTIME = jeiRuntime;
    }

    public static void refresh()
    {
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            Minecraft.getMinecraft().addScheduledTask(() -> mappers.forEach(JEICompatMapper::refresh));
        }
    }

}
