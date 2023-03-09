package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetPlayer implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if (player!=null) {
            P.red.remove(player);
            P.blue.remove(player);
            P.redTeam.removeEntry(player.getName());
            P.blueTeam.removeEntry(player.getName());
            if (P.imposter == player) {
                P.imposter = null;
                P.isImposterRevealed = false;
            }
            P.portalLocations.remove(player);
            sender.sendMessage(ChatColor.GREEN + "Reset Player");
        } else {
            sender.sendMessage(ChatColor.RED+"Player \""+args[0]+"\" Not Found");
        }
        return true;
    }

    public ResetPlayer(ManHunt p) {
    	P=p;
    }
}
