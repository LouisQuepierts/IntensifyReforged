package net.quepierts.intensify.command;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.object.IntensifierData;
import net.quepierts.intensify.utils.ListUtils;
import net.quepierts.intensify.utils.MessageUtils;
import net.quepierts.intensify.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGive extends SubCommand {
    public CommandGive(IntensifyReforgedPlugin plugin) {
        super(plugin, "give");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            MessageUtils.sendMessage(sender, this.config.command_usage_give);
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            MessageUtils.sendMessage(sender, this.config.command_PlayerAbsent.replace("%player%", args[0]));
            return true;
        }

        IntensifierData data = this.config.reinforcers.get(args[1]);

        if (data == null) {
            MessageUtils.sendMessage(sender, this.config.command_IllegalType.replace("%type%", args[1]));
            return true;
        }

        ItemStack item = new ItemStack(data.base);

        int amount = 1;

        if (args.length == 3) {
            amount = StringUtils.parseInteger(args[2]);
        }

        item.setAmount(amount);

        player.getInventory().addItem(item);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return ListUtils.getOnlinePlayerNames();
            case 2:
                return new ArrayList<>(this.config.reinforcers.keySet());
            case 3:
                return ListUtils.INTEGERS;
            default:
                return Collections.emptyList();
        }
    }
}
