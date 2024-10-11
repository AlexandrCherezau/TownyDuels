package org.lox.townyDuel.arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaManager {
    private final File arenasFile;
    private FileConfiguration arenasConfig;
    private final Map<String, Arena> arenas = new HashMap<>();
    private Arena editingArena = null;
    private final Map<String, Location> spawnPoint1 = new HashMap<>();
    private final Map<String, Location> spawnPoint2 = new HashMap<>();

    public ArenaManager() {
        File directory = new File("plugins/TownyDuel/arenas");
        if (!directory.exists()) {
            directory.mkdirs(); // Создаём директорию, если она не существует
        }

        arenasFile = new File(directory, "arenas.yml");

        try {
            if (!arenasFile.exists()) {
                arenasFile.createNewFile(); // Создаём файл, если он не существует
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        loadArenas();
    }

    public void createArena(String name, Location pos1, Location pos2) {
        Arena arena = new Arena(name, pos1, pos2);
        arenas.put(name, arena);
        saveArena(arena);
    }

    public void saveArena(Arena arena) {
        arenas.put(arena.getName(), arena);
        arenasConfig.set("arenas." + arena.getName() + ".pos1", arena.getPos1());
        arenasConfig.set("arenas." + arena.getName() + ".pos2", arena.getPos2());
        saveConfig();
    }

    public void removeArena(String name) {
        arenas.remove(name);
        spawnPoint1.remove(name);
        spawnPoint2.remove(name);
        arenasConfig.set("arenas." + name, null);
        saveConfig();
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    private void saveConfig() {
        try {
            arenasConfig.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArenas() {
        if (arenasConfig.contains("arenas")) {
            for (String name : arenasConfig.getConfigurationSection("arenas").getKeys(false)) {
                Location pos1 = (Location) arenasConfig.get("arenas." + name + ".pos1");
                Location pos2 = (Location) arenasConfig.get("arenas." + name + ".pos2");
                Arena arena = new Arena(name, pos1, pos2);
                arenas.put(name, arena);
            }
        }
    }
    public Map<String, Arena> getArenas() {
        return arenas;
    }
    public void setSpawnPoint1(String arenaName, Location location) {
        spawnPoint1.put(arenaName, location);
        arenasConfig.set("arenas." + arenaName + ".spawnPoint1", location);
        saveConfig();
    }
    public void setSpawnPoint2(String arenaName, Location location) {
        spawnPoint2.put(arenaName, location);
        arenasConfig.set("arenas." + arenaName + ".spawnPoint2", location);
        saveConfig();
    }
}
