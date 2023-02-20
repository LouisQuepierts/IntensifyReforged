package net.quepierts.intensify.object;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class IntensifierData {
    public final ItemStack base;
    public final String name;
    public final int additionalChance;
    public final int breakChance;
    public final int downgradeChance;

    public IntensifierData(final ConfigurationSection config, ItemStack base) {
        this.base = base;

        this.name = config.getName();

        this.additionalChance = config.getInt("chance", 0);
        this.breakChance = config.getInt("break", 0);
        this.downgradeChance = config.getInt("downgrade", 0);
    }
}
