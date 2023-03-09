package com.UnicodeSnowman.dev;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ImposterChat implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (P.isMongus && P.red.contains((Player)sender)) {
            for (Player player:P.red) {
                player.sendMessage(ChatColor.RED+"[IC]"+" "+sender.getName()+" "+ChatColor.WHITE+String.join(" ", args));
            }
        }
        else {
            ((Player)sender).sendMessage(ChatColor.RED+"You must be on the imposter's team to use this command...");
        }
        return true;
    }

    public ImposterChat(ManHunt p) {
    	P=p;
    }
}
