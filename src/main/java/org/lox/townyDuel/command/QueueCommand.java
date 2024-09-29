package org.lox.townyDuel.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lox.townyDuel.arena.Arena;
import org.lox.townyDuel.arena.ArenaManager;
import org.lox.townyDuel.managers.KitManager;

import java.util.*;

public class QueueCommand implements CommandExecutor, TabCompleter {
    private ArenaManager arenaManager;
    private KitManager kitManager;
    private Set<Player> queue = new HashSet<>();

    public QueueCommand(ArenaManager arenaManager, KitManager kitManager) {
        this.arenaManager = arenaManager;
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эту команду могут использовать только игроки!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("Используйте: /queue {название кита}");
            return false;
        }

        String kitName = args[0];

        if (!kitManager.getKits().containsKey(kitName)) {
            player.sendMessage("Кит с названием '" + kitName + "' не найден.");
            return true;
        }
        addToQueue(player, kitName);

        return true;
    }

    private void addToQueue(Player player, String kitName) {
        queue.add(player);
        player.sendMessage("Вы добавлены в очередь с китом '" + kitName + "'.");

        if (queue.size() >= 2) {
            Player[] players = queue.toArray(new Player[0]);
            Player player1 = players[0];
            Player player2 = players[1];

            kitManager.giveKit(player1, kitName);
            kitManager.giveKit(player2, kitName);

            if (teleportPlayersToRandomArena(player1, player2)) {
                player1.sendMessage("Вы были телепортированы на арену для сражения!");
                player2.sendMessage("Вы были телепортированы на арену для сражения!");
            } else {
                player1.sendMessage("Ошибка: не удалось телепортировать на арену.");
                player2.sendMessage("Ошибка: не удалось телепортировать на арену.");
            }

            queue.remove(player1);
            queue.remove(player2);
        }
    }

    private boolean teleportPlayersToRandomArena(Player player1, Player player2) {
        if (arenaManager.getArenas().isEmpty()) {
            return false;
        }

        Random random = new Random();
        Object[] arenaArray = arenaManager.getArenas().values().toArray();
        Arena selectedArena = (Arena) arenaArray[random.nextInt(arenaArray.length)];

        if (selectedArena.hasSpawns()) {
            player1.teleport(selectedArena.getPlayerSpawn1());
            player2.teleport(selectedArena.getPlayerSpawn2());
            return true;
        } else {
            return false;
        }
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(kitManager.getKits().keySet());
        }
        return null;
    }
}
