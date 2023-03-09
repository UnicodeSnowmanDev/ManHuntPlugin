package com.UnicodeSnowman.dev;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Trackable {

    private org.bukkit.entity.Player player;
    private org.bukkit.Location location;

    public Trackable(org.bukkit.entity.Player player, org.bukkit.Location location){
        this.player = player;
        this.location = location;
    }

    public Trackable(){
    }

    public org.bukkit.entity.Player getPlayer() {
        return this.player;
    }

    public org.bukkit.Location getLocation() {
        return this.location;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void setLocation(Location location){
        this.location = location;
    }
}
