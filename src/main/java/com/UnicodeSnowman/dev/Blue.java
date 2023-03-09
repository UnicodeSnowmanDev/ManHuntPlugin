package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Blue implements CommandExecutor {
	private ManHunt P;

    public void setBlue(Player player){
        if (this.P.blue.contains(player)){return;}
        if (this.P.red.contains(player)){
            this.P.red.remove(player);
            this.P.blueTeam.removeEntry(player.getName());
        }

        this.P.blue.add(player);
        this.P.blueTeam.addEntry(player.getName());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            this.setBlue((Player)sender);
            return true;
        }
        if (args[0].equals("@a")){
            for (Player player: Bukkit.getOnlinePlayers()){
                this.setBlue(player);
            }
        }
        else {
            for (String playerName: args) {
                if (Bukkit.getPlayer(playerName)!=null) {
                    this.setBlue(Bukkit.getPlayer(playerName));
                }
                else {
                    sender.sendMessage(ChatColor.RED+"Player \""+playerName+"\" Not Found");
                }
            }
        }
        return true;
    }

    public Blue(ManHunt p) {
    	P=p;
    }
}
