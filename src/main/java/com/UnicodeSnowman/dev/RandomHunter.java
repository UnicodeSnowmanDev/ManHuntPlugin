package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.UnicodeSnowman.dev.util.chooseRandomPlayer;

public class RandomHunter implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        this.P.red.clear();
        for (String entry: this.P.redTeam.getEntries()){
            this.P.redTeam.removeEntry(entry);
        }
        
        Player player = chooseRandomPlayer(new ArrayList<Player>(Bukkit.getOnlinePlayers()));
        this.P.red.add(player);
        this.P.redTeam.addEntry(player.getName());

        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.AQUA+" Has been chosen as the Hunter");

        return true;
    }

    public RandomHunter(ManHunt p) {
    	P=p;
    }
}
