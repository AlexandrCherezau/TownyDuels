package org.lox.townyDuel.arena;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaManager {
    private Map<String, Arena> arenas = new HashMap<>();
    private Arena editingArena = null;

    public void createArena(String name, Location pos1, Location pos2) {
        arenas.put(name, new Arena(name, pos1, pos2));
    }

    public boolean removeArena(String name) {
        return arenas.remove(name) != null;
    }

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Map<String, Arena> getArenas() {
        return arenas;
    }

    public void startEditing(String arenaName) {
        editingArena = getArena(arenaName);
    }

    public Arena getEditingArena() {
        return editingArena;
    }

    public boolean isEditing() {
        return editingArena != null;
    }

}
