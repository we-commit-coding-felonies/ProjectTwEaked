package moze_intel.projecte.emc.json;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.Fluid;

public class NSSFluid implements NormalizedSimpleStack {
	public final String name;

	private NSSFluid(Fluid f) {
		this.name = f.getName();
	}

	@Nonnull
	public static NormalizedSimpleStack create(Fluid fluid) {
		//TODO cache The fluid normalizedSimpleStacks?
		return new NSSFluid(fluid);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NSSFluid && name.equals(((NSSFluid) o).name);
	}

	@Override
	public String json() {
		return "FLUID|" + this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return "Fluid: " + this.name;
	}
}
