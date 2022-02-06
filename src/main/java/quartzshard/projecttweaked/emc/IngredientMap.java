package main.java.quartzshard.projecttweaked.emc;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class IngredientMap<T> {
	private final Map<T, Integer> ingredients = new HashMap<>();

	public void addIngredient(T thing, int amount) {
		ingredients.merge(thing, amount, Integer::sum);
	}

	public Map<T, Integer> getMap() {
		return Maps.newHashMap(ingredients);
	}

	@Override
	public String toString() {
		return ingredients.toString();
	}
}
