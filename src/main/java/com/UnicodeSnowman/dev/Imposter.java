package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Imposter implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (P.imposter!=null && P.imposter.isOnline()){
            if (!P.isImposterRevealed){
                P.blueTeam.removeEntry(P.imposter.getName());
            } else {
                P.redTeam.removeEntry(P.imposter.getName());
            }
            P.red.remove(P.imposter);
        }

        if (args.length==0){
            P.imposter=(Player)sender;
            P.blue.remove((Player)sender);
            P.redTeam.removeEntry(((Player)sender).getName());

            P.red.add((Player)sender);
            P.blueTeam.addEntry(((Player)sender).getName());
            P.isImposterRevealed=false;
            return true;
        }
        if (Bukkit.getPlayer(args[0])!=null){
            P.imposter=Bukkit.getPlayer(args[0]);
            P.blue.remove(Bukkit.getPlayer(args[0]));
            P.redTeam.removeEntry(Bukkit.getPlayer(args[0]).getName());

            P.red.add(Bukkit.getPlayer(args[0]));
            P.blueTeam.addEntry(Bukkit.getPlayer(args[0]).getName());
            P.isImposterRevealed=false;
        }
        return true;
    }

    public Imposter(ManHunt p) {
    	P=p;
    }
}
