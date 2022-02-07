package main.java.quartzshard.projecttweaked.integration.jei;

import main.java.quartzshard.projecttweaked.gui.GuiArcaneTablet;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
@JEIPlugin
public class ProjectEXJEIIntegration implements IModPlugin
{
	public static IJeiRuntime RUNTIME;

	@Override
	public void register(IModRegistry registry)
	{
		registry.addAdvancedGuiHandlers(ArcaneTabletJEI.INSTANCE);
		registry.addRecipeClickArea(GuiArcaneTablet.class, -60, 75, 33, 17, VanillaRecipeCategoryUid.CRAFTING);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ArcaneTabletJEI.INSTANCE, VanillaRecipeCategoryUid.CRAFTING);

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r)
	{
		r.addRecipeCategories(new AlchemyTableCategory(r.getJeiHelpers().getGuiHelper()));
	}

	//private void addInfo(IModRegistry registry, Item item)
	//{
	//	registry.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM, item.getTranslationKey() + ".tooltip");
	//}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r)
	{
		RUNTIME = r;
	}
}