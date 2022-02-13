package quartzshard.projecttweaked.integration.jei.mappers;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.recipe.IRecipeWrapper;
import quartzshard.projecttweaked.integration.jei.PEJeiPlugin;

public abstract class JEICompatMapper<T extends IRecipeWrapper>
{
    private final List<T> jeiRecipes = new ArrayList<>();
    private final String recipeCategory;

    public JEICompatMapper(String recipeCategory)
    {
        this.recipeCategory = recipeCategory;
    }

    public abstract void refresh();

    public void clear()
    {
        for (T recipe : jeiRecipes)
        {
            PEJeiPlugin.RUNTIME.getRecipeRegistry().removeRecipe(recipe, recipeCategory);
        }
        jeiRecipes.clear();
    }

    public void addRecipe(T recipe)
    {
        jeiRecipes.add(recipe);
        PEJeiPlugin.RUNTIME.getRecipeRegistry().addRecipe(recipe, recipeCategory);
    }
}