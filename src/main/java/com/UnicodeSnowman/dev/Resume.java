package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Resume implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        try{
            FileInputStream input = new FileInputStream("./plugins/ManHunt/portals.tmp");
            BukkitObjectInputStream bois = new BukkitObjectInputStream(input);
            this.P.portalLocations= (HashMap<Player, Location>) bois.readObject();
        }
        catch (IOException e){
            sender.sendMessage(ChatColor.RED+"An error occurred while attempting to resume. Please check to ensure portals.tmp exists.");
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Player ingamePlayer: Bukkit.getOnlinePlayers()){
            for (Player player: P.portalLocations.keySet()){
                if (ingamePlayer.getName().equals(player.getName())){
                    P.portalLocations.put(ingamePlayer, P.portalLocations.get(player));
                    P.portalLocations.remove(player);
                }
            }
        }


        return true;
    }

    public Resume(ManHunt p) {
    	P=p;
    }
}
