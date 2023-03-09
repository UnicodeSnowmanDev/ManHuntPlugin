package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

public class InvisHunt implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        P.isInvisHunt = !P.isInvisHunt;
        if (P.isInvisHunt) {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Invis Mode Enabled");
        }
        else {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Invis Mode Disabled");
        }
        return true;
    }

    public InvisHunt(ManHunt p) {
    	P=p;
    }
}
