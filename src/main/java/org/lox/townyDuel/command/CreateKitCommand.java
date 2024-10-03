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
import java.util.Collections;
import java.util.List;

public class CreateKitCommand implements CommandExecutor, TabCompleter {
    private final KitManager kitManager;

    public CreateKitCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда может быть выполнена только игроком.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Использование: /createkit save|remove {название кита}");
            return true;
        }

        String action = args[0];
        String kitName = args[1];

        switch (action.toLowerCase()) {
            case "save":
                kitManager.saveKit(kitName, player.getInventory().getContents());
                player.sendMessage("Кит '" + kitName + "' сохранён.");
                break;
            case "remove":
                kitManager.removeKit(kitName);
                player.sendMessage("Кит '" + kitName + "' удалён.");
                break;
            default:
                player.sendMessage("Неверное использование команды. Используйте: save или remove.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return List.of("save", "remove");
        } else if (args.length == 2) {
            return new ArrayList<>(kitManager.getKits().keySet());
        }
        return Collections.emptyList();
    }
}
