package net.quepierts.intensify.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtils {
    public static void sendMessage(CommandSender player, String message) {
        player.sendMessage("[§eIntensify§f] " + message);
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage("[§eIntensify§f] " + message);
    }
}
