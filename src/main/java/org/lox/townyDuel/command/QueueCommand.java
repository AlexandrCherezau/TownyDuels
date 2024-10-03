package org.lox.townyDuel.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lox.townyDuel.arena.Arena;
import org.lox.townyDuel.arena.ArenaManager;
import org.lox.townyDuel.managers.KitManager;

import java.util.*;

public class QueueCommand implements CommandExecutor, TabCompleter {
    private final ArenaManager arenaManager;
    private final KitManager kitManager;
    private final Set<Player> queue = new HashSet<>();

    public QueueCommand(ArenaManager arenaManager, KitManager kitManager) {
        this.arenaManager = arenaManager;
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда может быть выполнена только игроком.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Использование: /queue {название кита}");
            return true;
        }

        String kitName = args[0];

        if (!kitManager.getKits().containsKey(kitName)) {
            player.sendMessage("Кит с таким именем не существует.");
            return true;
        }

        if (queue.contains(player)) {
            player.sendMessage("Вы уже в очереди.");
            return true;
        }

        queue.add(player);
        player.sendMessage("Вы добавлены в очередь с китом " + kitName + ".");

        if (queue.size() == 2) {
            Player player1 = (Player) queue.toArray()[0];
            Player player2 = (Player) queue.toArray()[1];

            if (teleportPlayersToRandomArena(player1, player2)) {
                giveKitToPlayer(player1, kitName);
                giveKitToPlayer(player2, kitName);
                player1.sendMessage("Вы были телепортированы на арену.");
                player2.sendMessage("Вы были телепортированы на арену.");
                queue.clear(); // Очистка очереди после телепортации
            } else {
                player1.sendMessage("Не удалось телепортировать игроков, нет доступных арен.");
                player2.sendMessage("Не удалось телепортировать игроков, нет доступных арен.");
                queue.clear();
            }
        }

        return true;
    }

    private boolean teleportPlayersToRandomArena(Player player1, Player player2) {
        if (arenaManager.getArenas().isEmpty()) {
            return false; // Если нет арен, возвращаем false
        }

        Random random = new Random();
        Object[] arenaArray = arenaManager.getArenas().values().toArray();
        Arena selectedArena = (Arena) arenaArray[random.nextInt(arenaArray.length)];

        if (selectedArena.hasSpawns()) {
            // Телепортируем игроков на pos1 и pos2 арены
            player1.teleport(selectedArena.getPos1());
            player2.teleport(selectedArena.getPos2());
            return true; // Успешно телепортировали игроков
        } else {
            return false; // Если на арене не заданы точки спавна
        }
    }

    private void giveKitToPlayer(Player player, String kitName) {
        List<ItemStack> kitItems = kitManager.getKits().get(kitName).getItems();
        player.getInventory().clear();
        for (ItemStack item : kitItems) {
            player.getInventory().addItem(item);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(kitManager.getKits().keySet());
        }
        return Collections.emptyList();
    }
}
