package com.UnicodeSnowman.dev;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Vector;

public class ExampleTask extends BukkitRunnable {
    /*
    static boolean isValid(Location loc)
    {
        if (loc.getBlock().isLiquid() || loc.getBlock().getBlockData() instanceof Waterlogged || loc.getBlock().getType() == Material.KELP_PLANT || loc.getBlock().getType() == Material.KELP || loc.getBlock().getType() == Material.SEAGRASS || loc.getBlock().getType() == Material.TALL_SEAGRASS || loc.getBlock().getType() == Material.GLOW_LICHEN){
            return true;
        }
        if (loc.getBlock().getType() != Material.WATER || loc.getBlock().getType() == Material.AIR) {
            return false;
        }
        return false;
    }

    static void fix(Location loc) {
        if (loc.getBlock().getBlockData() instanceof Waterlogged) {
            ((Waterlogged)loc.getBlock().getBlockData()).setWaterlogged(false);
        }
        else {
            loc.getBlock().setType(Material.AIR);
        }
    }*/
    static boolean isValid(Location loc) {
        if (loc.getBlock().getType() == Material.STONE) {
            return true;
        }
        return false;
    }
    static void fix(Location loc) {
        loc.getBlock().setType(Material.AIR);
    }


    static int floodFillUtil(Location loc, int limit)
    {
        Vector<Location> queue = new Vector<Location>();
        // Base cases
        if (loc.getBlock().getType() != Material.STONE) {
            return 0;
        }

        queue.add(loc);
        loc.getBlock().setType(Material.AIR);

        World world = loc.getWorld();


        int count = 0;

        while (queue.size()>0) {
            if (count>limit) {
                return count;
            }
            Location n = queue.remove(0);

            int posX = n.getBlockX();
            int posY = n.getBlockY();
            int posZ = n.getBlockZ();

            Location x_plus = new Location(world, posX+1, posY, posZ);
            if (isValid(x_plus)) {
                fix(x_plus);
                queue.add(x_plus);
                count++;
            }
            Location x_minus = new Location(world, posX-1, posY, posZ);
            if (isValid(x_minus)) {
                fix(x_minus);
                queue.add(x_minus);
                count++;
            }
            Location y_plus = new Location(world, posX, posY+1, posZ);
            if (isValid(y_plus)) {
                fix(y_plus);
                queue.add(y_plus);
                count++;
            }
            Location y_minus = new Location(world, posX, posY-1, posZ);
            if (isValid(y_minus)) {
                fix(y_minus);
                queue.add(y_minus);
                count++;
            }
            Location z_plus = new Location(world, posX, posY, posZ+1);
            if (isValid(z_plus)) {
                fix(z_plus);
                queue.add(z_plus);
                count++;
            }
            Location z_minus = new Location(world, posX, posY, posZ-1);
            if (isValid(z_minus)) {
                fix(z_minus);
                queue.add(z_minus);
                count++;
            }
        }
        return count;
    }


    private final ManHunt P;
    private final Location loc;
    private final int limit;

    public ExampleTask(ManHunt plugin, Location loc, int limit) {
        this.P = plugin;
        this.loc = loc;
        this.limit = limit;
    }

    @Override
    public void run() {
        // What you want to schedule goes here
        P.getServer().broadcastMessage("Starting!");
        int count = floodFillUtil(this.loc, this.limit);
        P.getServer().broadcastMessage("Finished, modified "+String.valueOf(count)+" Blocks.");
    }
}
