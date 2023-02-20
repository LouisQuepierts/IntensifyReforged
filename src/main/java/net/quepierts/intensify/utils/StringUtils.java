package net.quepierts.intensify.utils;

import net.quepierts.intensify.PluginConfiguration;
import org.bukkit.ChatColor;

import java.util.List;

public class StringUtils {
    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> color(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, color(input.get(i)));
        }

        return input;
    }

    public static int parseInteger(String arg) {
        return parseInteger(arg, 1);
    }

    public static int parseInteger(String arg, int def) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static void addVanillaFormatInfo(PluginConfiguration config,
                                              String name,
                                              int amount,
                                              boolean present,
                                              boolean inverse, List<String> lore) {
        if (amount == 0) {
            return;
        }

        StringBuilder builder = new StringBuilder();

        if (amount > 0) {
            builder.append(inverse ? config.lore_NegativeColor : config.lore_PositiveColor)
                    .append("+").append(amount);
        } else {
            builder.append(inverse ? config.lore_PositiveColor : config.lore_NegativeColor)
                    .append(amount);
        }

        if (present) {
            builder.append("% ");
        }

        builder.append(name);

        lore.add(builder.toString());
    }

    public static String parseRomanNumeral10(int input) {
        input = Math.min(input, 10);

        switch (input) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return "";
        }
    }

    public static int getIndex(String coded) {
        String indexInfo = coded.substring(2).split("§i")[0];

        return parseInteger(indexInfo.replace("§", ""), -1);
    }

    public static String parseRomanNumeral(int input) {
        StringBuilder builder = new StringBuilder();
        while (input >= 1000) {
            input -= 1000;
            builder.append("M");
        }

        while (input >= 500) {
            input -= 500;
            builder.append("D");
        }

        while (input >= 100) {
            input -= 100;
            builder.append("C");
        }

        while (input >= 50) {
            input -= 50;
            builder.append("L");
        }

        while (input >= 10) {
            input -= 10;
            builder.append("X");
        }

        while (input >= 5) {
            input -= 5;
            builder.append("V");
        }

        while (input >= 1) {
            input --;
            builder.append("I");
        }

        return builder.toString()
                .replace("DCCCC", "CM")
                .replace("CCCC", "CD")
                .replace("LXXXX", "XC")
                .replace("XXXX", "XL")
                .replace("VIIII", "IX")
                .replace("IIII", "IV");
    }
}
