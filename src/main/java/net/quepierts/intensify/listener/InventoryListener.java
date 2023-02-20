package net.quepierts.intensify.listener;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.PluginConfiguration;
import net.quepierts.intensify.PluginData;
import net.quepierts.intensify.object.IntensifierData;
import net.quepierts.intensify.utils.ItemUtils;
import net.quepierts.intensify.utils.MessageUtils;
import net.quepierts.intensify.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryListener implements Listener {
    private final IntensifyReforgedPlugin plugin;
    private final PluginConfiguration configuration;
    private final PluginData data;

    public InventoryListener(IntensifyReforgedPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfiguration();
        this.data = plugin.getData();
    }

    public void register() {
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory instanceof FurnaceInventory) {
            onClickFurnace(event, (FurnaceInventory) clickedInventory);
        } else if (clickedInventory instanceof AnvilInventory
                || clickedInventory instanceof MerchantInventory
                || clickedInventory instanceof CraftingInventory) {
            preventCrafting(event);
        } else if (clickedInventory instanceof BeaconInventory) {
            doNotPutItInBeacon(event);
        }

        this.tryUpdate(event.getCursor());
    }

    @EventHandler
    public void onPlayer(InventoryDragEvent event) {
        Inventory clickedInventory = event.getView().getInventory(0);
        if (clickedInventory instanceof AnvilInventory
                || clickedInventory instanceof MerchantInventory
                || clickedInventory instanceof CraftingInventory
                || clickedInventory instanceof BeaconInventory) {
            preventDragging(event);
        }
    }

    private void preventDragging(InventoryDragEvent event) {
        ItemStack cursor = event.getCursor();

        if (ItemUtils.isIntensifier(this.configuration, cursor)) {
            event.setCancelled(true);

            MessageUtils.sendMessage(event.getWhoClicked(), configuration.message_InvalidAction);
        }
    }

    private void doNotPutItInBeacon(InventoryClickEvent event) {
        ItemStack cursor = event.getCursor();

        if (ItemUtils.isIntensifier(this.configuration, cursor)) {
            event.setCancelled(true);

            MessageUtils.sendMessage(event.getWhoClicked(), configuration.message_Beacon);
        }
    }


    private void preventCrafting(InventoryClickEvent event) {
        if (!event.getSlotType().equals(InventoryType.SlotType.CRAFTING)) {
            return;
        }

        ItemStack cursor = event.getCursor();

        if (ItemUtils.isIntensifier(this.configuration, cursor)) {
            event.setCancelled(true);

            MessageUtils.sendMessage(event.getWhoClicked(), configuration.message_InvalidAction);
        }
    }

    private void onClickFurnace(InventoryClickEvent event, FurnaceInventory inventory) {
        if (this.data.fuelMap.containsKey(inventory.getHolder().getBlock())) {
            event.setCancelled(true);
            return;
        }

        tryReplaceFuel(event, inventory);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        if (ItemUtils.isIntensifiable(this.configuration, inventory.getSmelting())
                && ItemUtils.isIntensifier(this.configuration, inventory.getFuel())) {
            this.data.playerMap.put(inventory.getHolder().getBlock(), player);
        }
    }

    private void tryReplaceFuel(InventoryClickEvent event, FurnaceInventory inventory) {
        if (!event.getClick().equals(ClickType.LEFT)) {
            return;
        }

        if (!event.getSlotType().equals(InventoryType.SlotType.FUEL)) {
            return;
        }

        ItemStack cursor = event.getCursor();

        if (!ItemUtils.isIntensifier(this.configuration, cursor)) {
            return;
        }

        ItemStack fuel = inventory.getFuel();

        event.getView().setCursor(fuel);
        inventory.setFuel(cursor);

        event.setCancelled(true);

        return;
    }

    private void tryUpdateItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return;
        }

        String displayName = itemStack.getItemMeta().getDisplayName();
        if (!displayName.startsWith("§i")) {
            return;
        }

        int index = StringUtils.getIndex(displayName);
        String type = configuration.types.get(index);
        ItemMeta base = configuration.reinforcers.get(type).base.getItemMeta();

        if (displayName.equals(base.getDisplayName())) {
            return;
        }

        itemStack.setItemMeta(base);
    }

    private void tryUpdate(ItemStack itemStack) {
        String type = ItemUtils.getType(itemStack);

        if (type.isEmpty()) {
            return;
        }

        IntensifierData data = configuration.reinforcers.get(type);

        if (data == null) {
            return;
        }

        ItemMeta current = itemStack.getItemMeta();
        ItemMeta base = data.base.getItemMeta();

        if (current.getDisplayName().equals(base.getDisplayName()) && base.getLore().containsAll(current.getLore())) {
            return;
        }

        itemStack.setItemMeta(base);
    }
}
