package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.UnicodeSnowman.dev.util.giveCompass;

public class Compass implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            giveCompass((Player)sender);
            return true;
        }
        if (args[0].equals("@a")){
            for (Player player: Bukkit.getOnlinePlayers()){
                giveCompass(player);
            }
        }
        else {
            for (String playerName: args) {
                if (Bukkit.getPlayer(playerName)!=null) {
                    giveCompass(Bukkit.getPlayer(playerName));
                }
                else {
                    sender.sendMessage(ChatColor.RED+"Player \""+playerName+"\" Not Found");
                }
            }
        }
        return true;
    }

    public Compass(ManHunt p) {
    	P=p;
    }
}
