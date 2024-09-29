package org.lox.townyDuel.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KitManager {
    private Map<String, ItemStack[]> kits = new HashMap<>();
    
    public void saveKit(String kitName, ItemStack[] inventory) {
        kits.put(kitName, inventory.clone());
    }

    public boolean removeKit(String kitName) {
        return kits.remove(kitName) != null;
    }

    public boolean kitExists(String kitName) {
        return kits.containsKey(kitName);
    }

    public void giveKit(Player player, String kitName) {
        ItemStack[] kit = kits.get(kitName);
        if (kit != null) {
            player.getInventory().setContents(kit);
        }
    }

    public Map<String, ItemStack[]> getKits() {
        return kits;
    }
}
