package net.quepierts.intensify.command;

import net.quepierts.intensify.IntensifyReforgedPlugin;
import net.quepierts.intensify.utils.ItemUtils;
import net.quepierts.intensify.utils.ListUtils;
import net.quepierts.intensify.utils.MessageUtils;
import net.quepierts.intensify.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class CommandExecute extends SubCommand {
    public CommandExecute(IntensifyReforgedPlugin plugin) {
        super(plugin, "execute");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3) {
            MessageUtils.sendMessage(sender, this.config.command_usage_execute);
            return true;
        }

        if (!(sender instanceof Player player)) {
            MessageUtils.sendMessage(sender, this.config.command_IngameOnly);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (!ItemUtils.isIntensifiable(this.config, item)) {
            MessageUtils.sendMessage(sender, this.config.command_MaxIntensificationTimes);
            return true;
        }

        int chance = StringUtils.parseInteger(args[0]);
        int breakChance = StringUtils.parseInteger(args[1]);
        int downgradeChance = StringUtils.parseInteger(args[2]);

        ItemUtils.intensify(this.config, player, item, chance, breakChance, downgradeChance);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 3) {
            return ListUtils.INTEGERS;
        } else {
            return Collections.emptyList();
        }
    }
}
