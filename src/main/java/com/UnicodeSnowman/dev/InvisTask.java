package com.UnicodeSnowman.dev;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

public class InvisTask extends BukkitRunnable {

    private final ManHunt P;

    public InvisTask(ManHunt plugin) {
        this.P = plugin;
    }

    @Override
    public void run() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        if (P.isInvisHunt && !P.shuttingDown) {
            for (Player player: P.blue) {
                for (Player hunter: P.red) {
                    Location hunterLoc = hunter.getLocation();
                    //(float) (1.0 / player.getLocation().distance(hunterLoc))
                    if (player.getLocation().getWorld() == hunterLoc.getWorld() && player.getLocation().distance(hunterLoc) < 20) {
                        player.playSound(hunterLoc, Sound.BLOCK_CONDUIT_AMBIENT, (float) (1.0 / player.getLocation().distance(hunterLoc) + 2.0), 1f);
                    }
                }
            }
        }
        else {
            this.cancel();
        }
    }

}