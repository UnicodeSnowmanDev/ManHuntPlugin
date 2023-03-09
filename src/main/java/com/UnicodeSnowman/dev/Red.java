package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.UnicodeSnowman.dev.util.giveCompass;

public class Red implements CommandExecutor {
	private ManHunt P;

    public void setRed(Player player){
        if (this.P.red.contains(player)){return;}
        if (this.P.blue.contains(player)){
            this.P.blue.remove(player);
            this.P.blueTeam.removeEntry(player.getName());
        }

        this.P.red.add(player);
        this.P.redTeam.addEntry(player.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            this.setRed((Player)sender);
            return true;
        }
        if (args[0].equals("@a")){
            for (Player player: Bukkit.getOnlinePlayers()){
                this.setRed(player);
            }
        }
        else {
            for (String playerName: args) {
                if (Bukkit.getPlayer(playerName)!=null) {
                    this.setRed(Bukkit.getPlayer(playerName));
                }
                else {
                    sender.sendMessage(ChatColor.RED+"Player \""+playerName+"\" Not Found");
                }
            }
        }
        return true;
    }

    public Red(ManHunt p) {
    	P=p;
    }
}
