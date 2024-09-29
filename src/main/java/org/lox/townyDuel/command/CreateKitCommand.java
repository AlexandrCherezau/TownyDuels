package org.lox.townyDuel.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lox.townyDuel.managers.KitManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateKitCommand implements CommandExecutor, TabCompleter {

    private KitManager kitManager;

    public CreateKitCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду могут использовать только игроки!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage("Использование: /createkit <save|remove> <название_кита>");
            return true;
        }

        String action = args[0];
        String kitName = args[1];

        if (action.equalsIgnoreCase("save")) {
            ItemStack[] inventoryContents = player.getInventory().getContents();
            kitManager.saveKit(kitName, inventoryContents);
            player.sendMessage("Кит '" + kitName + "' сохранён.");
        } else if (action.equalsIgnoreCase("remove")) {
            if (kitManager.removeKit(kitName)) {
                player.sendMessage("Кит '" + kitName + "' удалён.");
            } else {
                player.sendMessage("Кит с названием '" + kitName + "' не найден.");
            }
        } else {
            player.sendMessage("Неизвестная команда.");
        }

        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("save", "remove");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            return new ArrayList<>(kitManager.getKits().keySet());
        }
        return null;
    }
}
