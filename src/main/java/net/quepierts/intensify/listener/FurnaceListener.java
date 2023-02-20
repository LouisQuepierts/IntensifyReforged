package net.quepierts.intensify.listener;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.PluginConfiguration;
import net.quepierts.intensify.PluginData;
import net.quepierts.intensify.object.IntensifyData;
import net.quepierts.intensify.utils.ItemUtils;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

public class FurnaceListener implements Listener {
    private final IntensifyReforgedPlugin plugin;
    private final PluginConfiguration configuration;
    private final PluginData data;

    public FurnaceListener(IntensifyReforgedPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.data = plugin.getData();
    }

    public void register() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    // When furnace start to burn something
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (!ItemUtils.isIntensifier(this.configuration, event.getFuel())) {
            return;
        }

        Furnace furnace = (Furnace) event.getBlock().getState();

        FurnaceInventory inventory = furnace.getInventory();

        ItemStack smelting = inventory.getSmelting();

        if (!ItemUtils.isIntensifiable(this.configuration, smelting)) {
            event.setCancelled(true);
            event.setBurning(false);
            return;
        }

        if (!this.data.playerMap.containsKey(event.getBlock())) {
            event.setCancelled(true);
            event.setBurning(false);
            return;
        }

        IntensifyData data = this.configuration.reinforces.get(smelting.getType());
        int time = data.time;

        event.setBurning(true);
        event.setBurnTime(time);

        furnace.setCookTime((short) time);

        this.data.fuelMap.put(event.getBlock(), furnace.getInventory().getFuel());
    }

    // When furnace finish burn something
    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        if (!this.data.fuelMap.containsKey(event.getBlock())) {
            return;
        }

        ItemStack result = event.getSource();
        ItemUtils.tryIntensify(this.configuration,
                this.data.playerMap.remove(event.getBlock()),
                this.data.fuelMap.remove(event.getBlock()),
                result);

        event.setResult(result);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        this.data.fuelMap.remove(event.getBlock());
    }
}
