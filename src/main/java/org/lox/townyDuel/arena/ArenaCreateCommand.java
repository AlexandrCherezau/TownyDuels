package org.lox.townyDuel.arena;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaCreateCommand implements CommandExecutor, TabCompleter {
    private final ArenaManager arenaManager;
    private String editingArena = null;  // Для отслеживания арены в режиме редактирования

    public ArenaCreateCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда может быть выполнена только игроком.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 8 && args[0].equalsIgnoreCase("create")) {
            String arenaName = args[7];
            try {
                double x1 = Double.parseDouble(args[1]);
                double y1 = Double.parseDouble(args[2]);
                double z1 = Double.parseDouble(args[3]);
                double x2 = Double.parseDouble(args[4]);
                double y2 = Double.parseDouble(args[5]);
                double z2 = Double.parseDouble(args[6]);

                Location pos1 = new Location(player.getWorld(), x1, y1, z1);
                Location pos2 = new Location(player.getWorld(), x2, y2, z2);

                arenaManager.createArena(arenaName, pos1, pos2);
                player.sendMessage("Арена '" + arenaName + "' создана.");
            } catch (NumberFormatException e) {
                player.sendMessage("Неверные координаты.");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            String arenaName = args[1];
            if (arenaManager.getArena(arenaName) != null) {
                editingArena = arenaName;
                player.sendMessage("Режим редактирования арены '" + arenaName + "' активирован.");
            } else {
                player.sendMessage("Арена с именем '" + arenaName + "' не найдена.");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("pos1")) {
            if (editingArena != null) {
                Location playerLocation = player.getLocation();
                arenaManager.setSpawnPoint1(editingArena, playerLocation);
                player.sendMessage("Точка спавна 1 установлена для арены '" + editingArena + "'.");
            } else {
                player.sendMessage("Сначала выберите арену с помощью '/arenacreate start {название арены}'.");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("pos2")) {
            if (editingArena != null) {
                Location playerLocation = player.getLocation();
                arenaManager.setSpawnPoint2(editingArena, playerLocation);
                player.sendMessage("Точка спавна 2 установлена для арены '" + editingArena + "'.");
            } else {
                player.sendMessage("Сначала выберите арену с помощью '/arenacreate start {название арены}'.");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            String arenaName = args[1];
            if (arenaManager.getArena(arenaName) != null) {
                arenaManager.removeArena(arenaName);
                player.sendMessage("Арена '" + arenaName + "' удалена.");
            } else {
                player.sendMessage("Арена с именем '" + arenaName + "' не найдена.");
            }
        } else {
            player.sendMessage("Использование: /arenacreate create {x y z x2 y2 z2} {название арены}");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "start", "pos1", "pos2", "remove").stream()
                    .filter(subcommand -> subcommand.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("remove"))) {
            return new ArrayList<>(arenaManager.getArenas().keySet()).stream()
                    .filter(arenaName -> arenaName.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}
