package net.quepierts.intensify.utils;

import net.quepierts.intensify.PluginConfiguration;
import net.quepierts.intensify.object.IntensifierData;
import net.quepierts.intensify.object.IntensifyData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemUtils {
    private static final Random random = new Random();

    public static boolean isIntensifier(PluginConfiguration configuration, ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (!itemStack.hasItemMeta()) {
            return false;
        }

        String name = itemStack.getItemMeta().getDisplayName();

        for (IntensifierData value : configuration.reinforcers.values()) {
            if (value.base.getItemMeta().getDisplayName().equals(name)) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean isIntensifiable(PluginConfiguration configuration, ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        Material type = itemStack.getType();

        if (!configuration.reinforces.containsKey(type)) {
            return false;
        }

        IntensifyData data = configuration.reinforces.get(type);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.hasEnchant(data.enchantment)) {
            return true;
        }

        return getLevel(itemMeta.getEnchantLevel(data.enchantment), data.enchantmentLevelIncrease) < 10;
    }

    public static int getLevel(PluginConfiguration configuration, ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }

        Material type = itemStack.getType();

        if (!configuration.reinforces.containsKey(type)) {
            return 0;
        }

        IntensifyData data = configuration.reinforces.get(type);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (!itemMeta.hasEnchant(data.enchantment)) {
            return 0;
        }

        return getLevel(itemMeta.getEnchantLevel(data.enchantment), data.enchantmentLevelIncrease);
    }

    private static int getLevel(int enchantmentLevel, int increase) {
        int level = 0;

        while (enchantmentLevel > 0) {
            enchantmentLevel -= increase;

            level ++;
        }

        return level;
    }

    public static String getType(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return "";
        }

        List<String> lore = itemStack.getItemMeta().getLore();

        if (!lore.contains(PluginConfiguration.TAG_SYMBOL)) {
            return "";
        }

//        String displayName = itemStack.getItemMeta().getDisplayName();
//        int index = StringUtils.getIndex(displayName);
//
//        System.out.println(configuration.types.get(index));

        return lore.get(lore.indexOf(PluginConfiguration.TAG_SYMBOL) + 1).split("§i§7")[1];
    }

    public static void tryIntensify(PluginConfiguration configuration, Player player, ItemStack reinforcer, ItemStack target) {
        IntensifierData reinforcerData = configuration.reinforcers.get(getType(reinforcer));

        intensify(configuration, player, target, reinforcerData.additionalChance, reinforcerData.breakChance, reinforcerData.downgradeChance);
    }

    public static void intensify(PluginConfiguration configuration, Player player, ItemStack item, int chance, int breakChance, int downgradeChance) {
        IntensifyData data = configuration.reinforces.get(item.getType());

        if (data == null) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();

        int level = getLevel(configuration, item);

        boolean succeed = random.nextInt(100) < chance + data.getChance(level);
        int enchantmentLevel = itemMeta.getEnchantLevel(data.enchantment);

        if (succeed) {
            level ++;
            enchantmentLevel += data.enchantmentLevelIncrease;

            MessageUtils.sendMessage(player, configuration.message_IntensifySucceed.replace("%level%", String.valueOf(level)));

            if (level >= 7) {
                MessageUtils.broadcast(configuration.broadcast_IntensifySucceedCongratulate
                        .replace("%player%", player.getDisplayName()).replace("%level%", String.valueOf(level)));
            }
        } else {
            // Try to break item
            if (random.nextInt(100) < data.getBreakChance(level) + breakChance) {
                item.setType(Material.AIR);

                MessageUtils.sendMessage(player, configuration.message_IntensifyBroken);

                if (level >= 6) {
                    MessageUtils.broadcast(configuration.broadcast_IntensifyBroken.replace("%player%", player.getDisplayName()));
                }
                return;
            }

            // Try to downgrade
            if (random.nextInt(100) < data.getDowngradeChance(level) + downgradeChance) {
                enchantmentLevel -= data.enchantmentLevelIncrease;
                MessageUtils.sendMessage(player, configuration.message_IntensifyDowngraded.replace("%level%", String.valueOf(--level)));
            } else {
                MessageUtils.sendMessage(player, configuration.message_IntensifyFailed);
            }
        }

        setLore(configuration, itemMeta, level);
        itemMeta.addEnchant(data.enchantment, enchantmentLevel, true);
        item.setItemMeta(itemMeta);
    }

    private static void setLore(PluginConfiguration config, ItemMeta itemMeta, int level) {
        List<String> lore;

        if (itemMeta.hasLore()) {
            lore = itemMeta.getLore();
        } else {
            lore = new ArrayList<>();
        }

        if (config.vanilla_intensify) {
            setLoreVanilla(config, lore, level);
        } else {
            setLoreCustom(config, lore, level);
        }

        itemMeta.setLore(lore);
    }

    private static void setLoreVanilla(final PluginConfiguration config, final List<String> lore, int level) {
        for (String line : lore) {
            if (line.startsWith("§i§i")) {
                lore.set(lore.indexOf(line), config.lore_IntensificationTimes.replace("%level%", StringUtils.parseRomanNumeral10(level)));
                return;
            }
        }

        lore.add(config.lore_IntensificationTimes.replace("%level%", StringUtils.parseRomanNumeral10(level)));
    }

    private static void setLoreCustom(final PluginConfiguration config, final List<String> lore, int level) {
        StringBuilder builder = new StringBuilder(config.lore_IntensificationLevel_begin);

        for (int i = 0; i < 10; i++) {
            builder.append(i < level ? config.lore_IntensificationLevel_true : config.lore_IntensificationLevel_false);
        }

        builder.append(config.lore_IntensificationLevel_end);

        for (String line : lore) {
            if (line.startsWith("§i§i")) {
                lore.set(lore.indexOf(line), builder.toString());
                return;
            }
        }

        lore.add(builder.toString());
    }
}
