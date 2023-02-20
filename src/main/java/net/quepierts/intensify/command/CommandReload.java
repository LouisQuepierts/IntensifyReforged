package net.quepierts.intensify.command;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CommandReload extends SubCommand {
    public CommandReload(IntensifyReforgedPlugin plugin) {
        super(plugin, "reload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.reloadPluginConfig();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
