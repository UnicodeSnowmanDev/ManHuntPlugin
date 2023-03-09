package com.UnicodeSnowman.dev;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class achievementLock extends BukkitRunnable {

    private final ManHunt P;

    public achievementLock(ManHunt p) {
        this.P = p;
    }

    @Override
    public void run() {
        for (World world: P.getServer().getWorlds()) {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
        }
    }

}
