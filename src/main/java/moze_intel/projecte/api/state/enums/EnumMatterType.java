package moze_intel.projecte.api.state.enums;

import javax.annotation.Nonnull;

import net.minecraft.util.IStringSerializable;

public enum EnumMatterType implements IStringSerializable
{
    DARK_MATTER("dark_matter"),
    RED_MATTER("red_matter");

    private final String name;

    EnumMatterType(String name)
    {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
