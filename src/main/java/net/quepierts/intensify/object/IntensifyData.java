package net.quepierts.intensify.object;

import net.quepierts.intensify.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

public class IntensifyData {
    public static IntensifyData getInstance(final ConfigurationSection config) {
        Material material = Material.getMaterial(config.getName());

        if (material == null) {
            return null;
        }

        if (!config.contains("enchantment")) {
            return null;
        }

        String[] enchantInfo = config.getString("enchantment", "unbreaking | 1").replace(" ", "").split("\\|");
        Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantInfo[0].toLowerCase()));

        if (enchantment == null) {
            return null;
        }

        if (!config.contains("chance")) {
            return null;
        }

        String[] chanceInfo = config.getString("chance", "100 | 10").replace(" ", "").split("\\|");
        String[] breakInfo = config.getString("break", "-100 | 20").replace(" ", "").split("\\|");
        String[] downgradeInfo = config.getString("downgrade", "-100 | 20").replace(" ", "").split("\\|");

        int enchantmentIncrease = StringUtils.parseInteger(enchantInfo[1], 1);
        int chance = StringUtils.parseInteger(chanceInfo[0], 100);
        int chanceDecrease = StringUtils.parseInteger(chanceInfo[1], 10);

        int breakChance = StringUtils.parseInteger(breakInfo[0], -100);
        int breakChanceIncrease = StringUtils.parseInteger(breakInfo[1], 20);

        int downgradeChance = StringUtils.parseInteger(downgradeInfo[0], -100);
        int downgradeChanceIncrease = StringUtils.parseInteger(downgradeInfo[1], 20);

        int time = config.getInt("time", 240);

        return new IntensifyData(material,
                enchantment, enchantmentIncrease,
                chance, chanceDecrease,
                breakChance, breakChanceIncrease,
                downgradeChance, downgradeChanceIncrease,
                time);
    }

    public final Material material;

    public final Enchantment enchantment;
    public final int enchantmentLevelIncrease;

    public final int chance;
    public final int chanceDecrease;

    public final int breakChance;
    public final int breakChanceIncrease;

    public final int downgradeChance;
    public final int downgradeChanceIncrease;

    public final int time;

    private IntensifyData(Material material, Enchantment enchantment, int enchantmentLevelIncrease, int chance, int chanceDecrease, int breakChance, int breakChanceIncrease, int downgradeChance, int downgradeChanceIncrease, int time) {
        this.material = material;
        this.enchantment = enchantment;
        this.enchantmentLevelIncrease = enchantmentLevelIncrease;
        this.chance = chance;
        this.chanceDecrease = chanceDecrease;
        this.breakChance = breakChance;
        this.breakChanceIncrease = breakChanceIncrease;
        this.downgradeChance = downgradeChance;
        this.downgradeChanceIncrease = downgradeChanceIncrease;
        this.time = time;
    }

    public int getChance(int level) {
        return chance - (chanceDecrease * level);
    }

    public int getBreakChance(int level) {
        return breakChance + (breakChanceIncrease * level);
    }

    public int getDowngradeChance(int level) {
        return downgradeChance + (downgradeChanceIncrease * level);
    }
}
