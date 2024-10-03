package org.lox.townyDuel.arena;

import org.bukkit.Location;

public class Arena {
    private String name;
    private Location pos1;
    private Location pos2;

    public Arena(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public boolean hasSpawns() {
        return pos1 != null && pos2 != null;
    }
}
