package com.UnicodeSnowman.dev;

import static com.UnicodeSnowman.dev.Configuration.CONF;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Commands implements CommandExecutor {
	private ManHunt P;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        //Bukkit.broadcastMessage(this.P.portalLocations.toString());
        //((Player)sender).setArrowsInBody(Integer.valueOf(args[0]));
        for (World world: P.getServer().getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }
        BukkitTask task = new achievementLock(this.P).runTaskLater(this.P, 200);
        return true;
    }
    
    public Commands(ManHunt p) {
    	P=p;
    }
}
