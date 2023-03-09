package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class achievementHunt implements CommandExecutor {
	private ManHunt P;



    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        this.P.isAchievementHunt = !this.P.isAchievementHunt;
        if (this.P.isAchievementHunt) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Achievement Hunt Mode Enabled");
        }
        else {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Achievement Hunt Mode Disabled");
        }
        return true;
    }

    public achievementHunt(ManHunt p) {
    	P=p;
    }

    public static Advancement getAdvancement(String name) {
        Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
        while (it.hasNext()) {
            Advancement a = it.next();
            if (a.getKey().getKey().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }
}
