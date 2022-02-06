package main.java.quartzshard.projecttweaked.config;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class TomeEnabledFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return () -> ProjectTwEakedConfig.difficulty.craftableTome;
    }
}
