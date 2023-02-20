package net.quepierts.intensify.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {
    public static final List<String> INTEGERS = Arrays.asList("number", "1");
    public static final List<String> BOOLEAN = Arrays.asList("true", "false");

    public static List<String> getOnlinePlayerNames() {
        List<String> result = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            result.add(onlinePlayer.getName());
        }

        return result;
    }
}
