package net.quepierts.intensify;

import net.quepierts.intensify.object.IntensifyData;
import net.quepierts.intensify.object.IntensifierData;
import net.quepierts.intensify.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.*;

public class PluginConfiguration {
    public static final String TAG_SYMBOL = "§i§t";

    public boolean vanilla_intensifier;
    public boolean vanilla_intensify;

    public String language;

    public String lore_WhenIntensifyItem;
    public String lore_AdditionalChance;
    public String lore_BreakChance;
    public String lore_DowngradeChance;

    public String lore_PositiveColor;
    public String lore_NegativeColor;

    public String lore_tag;

    public String lore_IntensificationLevel_begin;
    public String lore_IntensificationLevel_end;
    public String lore_IntensificationLevel_true;
    public String lore_IntensificationLevel_false;

    public String lore_IntensificationTimes;

    public String message_IntensifySucceed;
    public String message_IntensifyFailed;
    public String message_IntensifyDowngraded;
    public String message_IntensifyBroken;

    public String message_InvalidAction;
    public String message_Beacon;

    public String broadcast_IntensifySucceedCongratulate;
    public String broadcast_IntensifyBroken;

    public String command_PlayerAbsent;
    public String command_IllegalType;
    public String command_IngameOnly;
    public String command_MaxIntensificationTimes;

    public String command_usage_give;
    public String command_usage_execute;
    public String command_usage_reload;

    public final Map<String, IntensifierData> reinforcers = new HashMap<>();
    public final Map<Material, IntensifyData> reinforces = new HashMap<>();
    public final List<String> types = new ArrayList<>();

    private final List<NamespacedKey> recipes = new ArrayList<>();

    private final IntensifyReforgedPlugin plugin;

    public PluginConfiguration(IntensifyReforgedPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = this.plugin.getConfig();

        this.vanilla_intensify = config.getBoolean("intensify-info-vanilla");
        this.vanilla_intensifier = config.getBoolean("intensifier-info-vanilla");

        this.language = config.getString("lang");

        this.loadLanguage();

        this.loadReinforces(config);
        this.loadReinforcers(config);
    }

    private void loadLanguage() {
        File languageFile = new File("plugins\\IntensifyReforged\\lang\\" + language + ".yml");

        try {
            if (!languageFile.exists()) {
                languageFile.createNewFile();

                InputStream in = this.plugin.getResource("lang/en_us.yml");
                OutputStream out = new FileOutputStream(languageFile);
                byte[] buf = new byte[1000];

                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                out.close();
                in.close();
            }
        } catch (IOException ignored) {
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(languageFile);

        this.lore_WhenIntensifyItem = "§i§i" + StringUtils.color(config.getString("lore-WhenIntensifyItem"));
        this.lore_AdditionalChance = "§i§i" + StringUtils.color(config.getString("lore-AdditionalChance"));
        this.lore_BreakChance = "§i§i" + StringUtils.color(config.getString("lore-BreakChance"));
        this.lore_DowngradeChance = "§i§i" + StringUtils.color(config.getString("lore-DowngradeChance"));

        this.lore_PositiveColor = "§i§i" + StringUtils.color(config.getString("lore-PositiveColor"));
        this.lore_NegativeColor = "§i§i" + StringUtils.color(config.getString("lore-NegativeColor"));

        this.lore_tag = "§i§t" + StringUtils.color(config.getString("lore-tag"));

        this.lore_IntensificationLevel_begin = "§i§i" + StringUtils.color(config.getString("lore-custom-IntensificationLevel-begin"));
        this.lore_IntensificationLevel_end = "§i§i" + StringUtils.color(config.getString("lore-custom-IntensificationLevel-end"));
        this.lore_IntensificationLevel_true = "§i§i" + StringUtils.color(config.getString("lore-custom-IntensificationLevel-true"));
        this.lore_IntensificationLevel_false = "§i§i" + StringUtils.color(config.getString("lore-custom-IntensificationLevel-false"));

        this.lore_IntensificationTimes = "§i§i" + StringUtils.color(config.getString("lore-IntensificationTimes"));

        this.message_IntensifySucceed = StringUtils.color(config.getString("message-IntensifySucceed"));
        this.message_IntensifyFailed = StringUtils.color(config.getString("message-IntensifyFailed"));
        this.message_IntensifyDowngraded = StringUtils.color(config.getString("message-IntensifyDowngraded"));
        this.message_IntensifyBroken = StringUtils.color(config.getString("message-IntensifyBroken"));

        this.message_InvalidAction = StringUtils.color(config.getString("message-InvalidAction"));
        this.message_Beacon = StringUtils.color(config.getString("message-Beacon"));

        this.broadcast_IntensifySucceedCongratulate = StringUtils.color(config.getString("broadcast-IntensifySucceedCongratulate"));
        this.broadcast_IntensifyBroken = StringUtils.color(config.getString("broadcast-IntensifyBroken"));

        this.command_PlayerAbsent = StringUtils.color(config.getString("command-PlayerAbsent"));
        this.command_IllegalType = StringUtils.color(config.getString("command-IllegalType"));
        this.command_IngameOnly = StringUtils.color(config.getString("command-IngameOnly"));
        this.command_MaxIntensificationTimes = StringUtils.color(config.getString("command-MaxIntensificationTimes"));

        this.command_usage_give = StringUtils.color(config.getString("command-usage-give"));
        this.command_usage_execute = StringUtils.color(config.getString("command-usage-execute"));
        this.command_usage_reload = StringUtils.color(config.getString("command-usage-reload"));
    }

    private void loadReinforces(final FileConfiguration config) {
//        for (NamespacedKey recipe : this.recipes) {
//            Bukkit.removeRecipe(recipe);
//        }

        Iterator<Recipe> iterator = Bukkit.recipeIterator();

        while (iterator.hasNext()) {
            if (iterator.next() instanceof Keyed recipe) {
                if (recipe.getKey().getNamespace().equals("intensifyreforged")) {
                    Bukkit.removeRecipe(recipe.getKey());
                }
            }
        }

        this.recipes.clear();
        this.reinforces.clear();

        ConfigurationSection recipes = config.getConfigurationSection("recipes");

        IntensifyData data;
        NamespacedKey namespace;
        for (String key : recipes.getKeys(false)) {
            data = IntensifyData.getInstance(recipes.getConfigurationSection(key));

            if (data != null) {
                this.reinforces.put(data.material, data);

                namespace = new NamespacedKey(this.plugin, key);
                Bukkit.addRecipe(new FurnaceRecipe(namespace, new ItemStack(data.material), data.material, 0.0f, data.time));
            }
        }
    }

    private void loadReinforcers(final FileConfiguration config) {
        this.reinforcers.clear();
        this.types.clear();

        ConfigurationSection intensifier = config.getConfigurationSection("intensifier");

        ItemStack item;
        ConfigurationSection data;

        int i = 0;
        for (String key : intensifier.getKeys(false)) {
            data = intensifier.getConfigurationSection(key);
            item = createItemStack(data, i);

            if (item != null) {
                i++;
                this.reinforcers.put(key, new IntensifierData(data, item));
                this.types.add(key);
            }
        }
    }

    private ItemStack createItemStack(final ConfigurationSection config, int i) {
        if (!config.contains("material")) {
            return null;
        }

        Material material = Material.getMaterial(config.getString("material"));

        if (material == null) {
            return null;
        }

        ItemStack result = new ItemStack(material);

        ItemMeta itemMeta = result.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (config.contains("display-name")) {
            itemMeta.setDisplayName(StringUtils.color(config.getString("display-name")));
        }

        if (config.contains("lore")) {
            lore.addAll(StringUtils.color(config.getStringList("lore")));
        }

        this.addVanillaInfo(config, lore);

        lore.add("§i§t");
        lore.add(this.lore_tag + "§i§7" + config.getName());

        itemMeta.setLore(lore);
        result.setItemMeta(itemMeta);

        return result;
    }

    private void addVanillaInfo(final ConfigurationSection config, final List<String> lore) {
        if (!vanilla_intensifier) {
            return;
        }

        int chance = config.getInt("chance", 0);
        int breakChance = config.getInt("break", 0);
        int downgradeChance = config.getInt("downgrade", 0);

        if (chance + breakChance + downgradeChance == 0) {
            return;
        }

        lore.add("");
        lore.add(this.lore_WhenIntensifyItem);

        StringUtils.addVanillaFormatInfo(this, this.lore_AdditionalChance, chance, true, false, lore);
        StringUtils.addVanillaFormatInfo(this, this.lore_BreakChance, breakChance, true, true, lore);
        StringUtils.addVanillaFormatInfo(this, this.lore_DowngradeChance, downgradeChance, true, true, lore);
    }

}
