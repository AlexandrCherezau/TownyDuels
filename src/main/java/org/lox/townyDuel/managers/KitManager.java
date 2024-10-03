package org.lox.townyDuel.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KitManager {
    private Map<String, Kit> kits = new HashMap<>();
    private File kitFile;
    private FileConfiguration kitConfig;

    public KitManager(JavaPlugin plugin) {
        kitFile = new File(plugin.getDataFolder(), "kits.yml");
        if (!kitFile.exists()) {
            try {
                kitFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        kitConfig = YamlConfiguration.loadConfiguration(kitFile);
        loadKits();
    }

    public Map<String, Kit> getKits() {
        return kits;
    }

    public void saveKit(String name, ItemStack[] items) {
        Kit kit = new Kit(name, List.of(items));
        kits.put(name, kit);
        saveKitToFile(kit);
    }

    public void removeKit(String name) {
        kits.remove(name);
        kitConfig.set("kits." + name, null);
        try {
            kitConfig.save(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveKitToFile(Kit kit) {
        String path = "kits." + kit.getName();
        kitConfig.set(path + ".items", kit.getItems());
        try {
            kitConfig.save(kitFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadKits() {
        if (kitConfig.contains("kits")) {
            for (String key : kitConfig.getConfigurationSection("kits").getKeys(false)) {
                List<ItemStack> items = (List<ItemStack>) kitConfig.get("kits." + key + ".items");
                Kit kit = new Kit(key, items);
                kits.put(key, kit);
            }
        }
    }

    public void saveKits() {
        for (Kit kit : kits.values()) {
            saveKitToFile(kit);
        }
    }
}
