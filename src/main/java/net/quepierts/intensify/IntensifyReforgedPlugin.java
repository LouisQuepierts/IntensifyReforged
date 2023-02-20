package net.quepierts.intensify;

import net.quepierts.intensify.command.CommandExecute;
import net.quepierts.intensify.command.CommandGive;
import net.quepierts.intensify.command.CommandReload;
import net.quepierts.intensify.command.IntensifyCommand;
import net.quepierts.intensify.listener.FurnaceListener;
import net.quepierts.intensify.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class IntensifyReforgedPlugin extends JavaPlugin {
    private final File configPath = new File("plugins\\IntensifyReforged");
    private PluginConfiguration configuration;
    private PluginData data;

    @Override
    public void onEnable() {
        this.configuration = new PluginConfiguration(this);
        this.data = new PluginData();
        this.reloadPluginConfig();

        this.initializeCommand();
        this.initializeListener();
    }

    private void initializeCommand() {
        new IntensifyCommand(this).register();
        new CommandReload(this);
        new CommandGive(this);
        new CommandExecute(this);
    }

    private void initializeListener() {
        new FurnaceListener(this).register();
        new InventoryListener(this).register();
    }

    @Override
    public void onDisable() {

    }

    public void reloadPluginConfig() {

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        reloadConfig();

        this.configuration.load();

    }

    @Override
    public void saveDefaultConfig() {
        if (!configPath.exists()) {
            saveResource("config.yml", false);
            saveResource("lang\\en_us.yml", false);
            saveResource("lang\\zh_cn.yml", false);
        }
    }

    public PluginConfiguration getConfiguration() {
        return configuration;
    }

    public PluginData getData() {
        return data;
    }
}
