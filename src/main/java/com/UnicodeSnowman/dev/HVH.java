package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HVH implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        this.P.isMongus=false;
        if (P.isHVH) {
            this.P.isHVH=false;
            Bukkit.broadcastMessage(ChatColor.AQUA+"Hunter vs. Hunter mode disabled.");
        } else {
            this.P.isHVH=true;
            Bukkit.broadcastMessage(ChatColor.AQUA+"Hunter vs. Hunter mode enabled.");
        }

        return true;
    }

    public HVH(ManHunt p) {
    	P=p;
    }
}
