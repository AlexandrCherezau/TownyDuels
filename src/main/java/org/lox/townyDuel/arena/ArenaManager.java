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
    private Map<String, Arena> arenas = new HashMap<>();
    private File arenaFile;
    private FileConfiguration arenaConfig;

    public ArenaManager(JavaPlugin plugin) {
        arenaFile = new File(plugin.getDataFolder(), "arenas.yml");
        if (!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
        loadArenas();
    }

    public void createArena(String name, Location pos1, Location pos2) {
        Arena arena = new Arena(name, pos1, pos2);
        arenas.put(name, arena);
        saveArena(arena); // Сохраняем арену в файл
    }

    public void saveArena(Arena arena) {
        String path = "arenas." + arena.getName();
        arenaConfig.set(path + ".pos1", serializeLocation(arena.getPos1()));
        arenaConfig.set(path + ".pos2", serializeLocation(arena.getPos2()));
        try {
            arenaConfig.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadArenas() {
        if (arenaConfig.contains("arenas")) {
            for (String key : arenaConfig.getConfigurationSection("arenas").getKeys(false)) {
                Location pos1 = deserializeLocation(arenaConfig.getConfigurationSection("arenas." + key + ".pos1").getValues(false));
                Location pos2 = deserializeLocation(arenaConfig.getConfigurationSection("arenas." + key + ".pos2").getValues(false));
                Arena arena = new Arena(key, pos1, pos2);
                arenas.put(key, arena);
            }
        }
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    private Map<String, Object> serializeLocation(Location location) {
        Map<String, Object> locMap = new HashMap<>();
        locMap.put("world", location.getWorld().getName());
        locMap.put("x", location.getX());
        locMap.put("y", location.getY());
        locMap.put("z", location.getZ());
        locMap.put("yaw", location.getYaw());
        locMap.put("pitch", location.getPitch());
        return locMap;
    }

    private Location deserializeLocation(Map<String, Object> locMap) {
        String worldName = (String) locMap.get("world");
        double x = (double) locMap.get("x");
        double y = (double) locMap.get("y");
        double z = (double) locMap.get("z");
        float yaw = ((Number) locMap.get("yaw")).floatValue();
        float pitch = ((Number) locMap.get("pitch")).floatValue();
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public void saveArenas() {
        for (Arena arena : arenas.values()) {
            saveArena(arena);
        }
    }
}
