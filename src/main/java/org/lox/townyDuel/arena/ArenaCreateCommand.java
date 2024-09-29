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
    private ArenaManager arenaManager;

    public ArenaCreateCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Недостаточно аргументов.");
            return false;
        }

        String subcommand = args[0];

        switch (subcommand) {
            case "create":
                return handleCreate(player, args);
            case "start":
                return handleStart(player, args);
            case "pos1":
                return handlePos1(player, args);
            case "pos2":
                return handlePos2(player, args);
            case "remove":
                return handleRemove(player, args);
            default:
                player.sendMessage("Неизвестная команда.");
                return false;
        }
    }

    private boolean handleCreate(Player player, String[] args) {
        if (args.length != 8) {
            player.sendMessage("Использование: /arenacreate create {x y z x2 y2 z2} {название арены}");
            return false;
        }

        try {
            Location pos1 = new Location(player.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            Location pos2 = new Location(player.getWorld(), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));
            String arenaName = args[7];

            arenaManager.createArena(arenaName, pos1, pos2);
            player.sendMessage("Арена " + arenaName + " успешно создана.");
        } catch (NumberFormatException e) {
            player.sendMessage("Неверные координаты.");
            return false;
        }
        return true;
    }

    private boolean handleStart(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("Использование: /arenacreate start {название арены}");
            return false;
        }

        String arenaName = args[1];
        Arena arena = arenaManager.getArena(arenaName);

        if (arena == null) {
            player.sendMessage("Арена с таким названием не найдена.");
            return false;
        }

        arenaManager.startEditing(arenaName);
        player.sendMessage("Режим редактирования для арены " + arenaName + " активирован.");
        return true;
    }

    private boolean handlePos1(Player player, String[] args) {
        if (!arenaManager.isEditing()) {
            player.sendMessage("Вы не в режиме редактирования арены.");
            return false;
        }

        Location location = player.getLocation();
        Arena arena = arenaManager.getEditingArena();
        arena.setPlayerSpawn1(location);
        player.sendMessage("Точка спавна 1 установлена на координатах: " + location.toString());
        return true;
    }

    private boolean handlePos2(Player player, String[] args) {
        if (!arenaManager.isEditing()) {
            player.sendMessage("Вы не в режиме редактирования арены.");
            return false;
        }

        Location location = player.getLocation();
        Arena arena = arenaManager.getEditingArena();
        arena.setPlayerSpawn2(location);
        player.sendMessage("Точка спавна 2 установлена на координатах: " + location.toString());
        return true;
    }

    private boolean handleRemove(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("Использование: /arenacreate remove {название арены}");
            return false;
        }

        String arenaName = args[1];
        if (arenaManager.removeArena(arenaName)) {
            player.sendMessage("Арена " + arenaName + " успешно удалена.");
        } else {
            player.sendMessage("Арена с таким названием не найдена.");
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
