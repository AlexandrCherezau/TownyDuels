package org.lox.townyDuel.arena;

import org.bukkit.Location;

public class Arena {
    private String name;
    private Location pos1;
    private Location pos2;
    private Location playerSpawn1;
    private Location playerSpawn2;

    public Arena(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() {
        return name;
    }
    //asd

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getPlayerSpawn1() {
        return playerSpawn1;
    }

    public void setPlayerSpawn1(Location playerSpawn1) {
        this.playerSpawn1 = playerSpawn1;
    }

    public Location getPlayerSpawn2() {
        return playerSpawn2;
    }

    public void setPlayerSpawn2(Location playerSpawn2) {
        this.playerSpawn2 = playerSpawn2;
    }

    public boolean hasSpawns() {
        return playerSpawn1 != null && playerSpawn2 != null;
    }
}
