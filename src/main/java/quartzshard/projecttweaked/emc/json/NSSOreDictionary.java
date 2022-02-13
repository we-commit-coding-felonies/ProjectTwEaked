package quartzshard.projecttweaked.emc.json;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class NSSOreDictionary implements NormalizedSimpleStack {
	static final Map<String, NormalizedSimpleStack> oreDictStacks = new HashMap<>();

	public final String od;

	private NSSOreDictionary(String od) {
		this.od = od;
	}

	@Nullable
	public static NormalizedSimpleStack create(String oreDictionaryName) {
		return oreDictStacks.computeIfAbsent(oreDictionaryName, NSSOreDictionary::new);
	}

	@Override
	public int hashCode() {
		return od.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NSSOreDictionary && this.od.equals(((NSSOreDictionary) o).od);
	}

	@Override
	public String json() {
		return "OD|" + this.od;
	}

	@Override
	public String toString() {
		return "OD: " + od;
	}
}
