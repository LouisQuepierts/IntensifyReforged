package net.quepierts.intensify;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class PluginData {
    public final Map<Block, ItemStack> fuelMap = new HashMap<>();
    public final Map<Block, Player> playerMap = new HashMap<>();

}
