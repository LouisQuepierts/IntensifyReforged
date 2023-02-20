package net.quepierts.intensify.command;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.PluginConfiguration;
import net.quepierts.intensify.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;


public class IntensifyCommand implements TabExecutor {
    private static final Map<String, SubCommand> subCommands = new HashMap<>();
    private static final List<String> subCommandIDs = new ArrayList<>();

    public static void add(String name, SubCommand command) {
        subCommands.put(name, command);
        subCommandIDs.add(name);
    }

    private final IntensifyReforgedPlugin plugin;
    private final PluginConfiguration config;

    public IntensifyCommand(IntensifyReforgedPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfiguration();
    }

    public void register() {
        this.plugin.getCommand("intensify").setExecutor(this);
        this.plugin.getCommand("intensify").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, this.config.command_usage_give);
            MessageUtils.sendMessage(sender, this.config.command_usage_execute);
            MessageUtils.sendMessage(sender, this.config.command_usage_reload);
            return true;
        }

        if (subCommandIDs.contains(args[0])) {
            String[] argsNew = new String[args.length - 1];
            System.arraycopy(args, 1, argsNew, 0, argsNew.length);
            return subCommands.get(args[0]).onCommand(sender, command, label, argsNew);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subCommandIDs;
        }

        if (!subCommandIDs.contains(args[0])) {
            return Collections.emptyList();
        }

        String[] argsNew = new String[args.length - 1];
        System.arraycopy(args, 1, argsNew, 0, argsNew.length);
        return subCommands.get(args[0]).onTabComplete(sender, command, alias, argsNew);
    }
}
