package main.java.quartzshard.projecttweaked.emc.generators;

import java.util.Map;

/**
 * Defines something that can simply yield a mapping of values.
 * @param <T> The key type
 * @param <V> The value type
 */
public interface IValueGenerator<T, V extends Comparable<V>>
{
	Map<T, V> generateValues();
}
