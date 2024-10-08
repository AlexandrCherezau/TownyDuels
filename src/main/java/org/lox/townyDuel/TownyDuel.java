package org.lox.townyDuel;

import org.bukkit.plugin.java.JavaPlugin;
import org.lox.townyDuel.arena.ArenaCreateCommand;
import org.lox.townyDuel.arena.ArenaManager;
import org.lox.townyDuel.command.CreateKitCommand;
import org.lox.townyDuel.command.QueueCommand;
import org.lox.townyDuel.managers.KitManager;

public final class TownyDuel extends JavaPlugin {
    private ArenaManager arenaManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        kitManager = new KitManager(this);
        arenaManager = new ArenaManager();

        getCommand("queuea").setExecutor(new QueueCommand(arenaManager, kitManager));

        getCommand("createkit").setExecutor(new CreateKitCommand(kitManager));

        getCommand("arenacreate").setExecutor(new ArenaCreateCommand(arenaManager));
    }

    public KitManager getKitManager() {
        return kitManager;
    }
    @Override
    public void onDisable(){
    }
}
