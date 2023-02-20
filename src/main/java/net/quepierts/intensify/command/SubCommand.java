package net.quepierts.intensify.command;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.PluginConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected final IntensifyReforgedPlugin plugin;
    protected final PluginConfiguration config;
    private final String name;

    protected SubCommand(IntensifyReforgedPlugin plugin, String name) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
        this.name = name;

        IntensifyCommand.add(name, this);
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);
}
