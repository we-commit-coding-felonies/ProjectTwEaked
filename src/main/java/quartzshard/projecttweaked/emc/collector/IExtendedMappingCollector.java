package main.java.quartzshard.projecttweaked.emc.collector;

import main.java.quartzshard.projecttweaked.emc.arithmetics.IValueArithmetic;

import java.util.Map;

public interface IExtendedMappingCollector<T, V extends Comparable<V>, A extends IValueArithmetic> extends IMappingCollector<T, V>
{
	void addConversion(int outnumber, T output, Map<T, Integer> ingredientsWithAmount, A arithmeticForConversion);

	void addConversion(int outnumber, T output, Iterable<T> ingredients, A arithmeticForConversion);

	A getArithmetic();
}
