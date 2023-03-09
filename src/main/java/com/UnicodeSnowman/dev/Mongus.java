package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Mongus implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        this.P.isHVH=false;
        if (P.isMongus) {
            this.P.isMongus=false;
            Bukkit.broadcastMessage(ChatColor.AQUA+"Mongus mode disabled.");
        } else {
            this.P.isMongus=true;
            Bukkit.broadcastMessage(ChatColor.AQUA+"Mongus mode enabled.");
        }

        return true;
    }

    public Mongus(ManHunt p) {
    	P=p;
    }
}
