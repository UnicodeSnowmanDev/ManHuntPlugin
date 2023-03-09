package com.UnicodeSnowman.dev;

import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.*;

import static com.UnicodeSnowman.dev.Configuration.CONF;

public class util {
    public static void giveCompass(Player player) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.AQUA + "Right Click Me!");
        meta.setLore(lore);
        meta.setLodestoneTracked(false);
        meta.setLodestone(player.getLocation());
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Tracker");
        compass.setItemMeta(meta);
        compass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        player.getInventory().addItem(compass);
    }

    public static void punish(Player player) {
        if (CONF.publicPunish) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 100, 1);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 100, 1);
            player.damage(4.0);
            String message = ChatColor.GREEN + player.getName() + " IS A P P BRAIN";
            StringBuilder bars = new StringBuilder();
            bars.append("-".repeat(Math.max(0, message.length() + 1)));
            Bukkit.broadcastMessage(ChatColor.YELLOW + bars.toString());
            Bukkit.broadcastMessage(message);
            Bukkit.broadcastMessage(ChatColor.YELLOW + bars.toString());
        }
    }

    public static HashMap<Player, Location> getDimensionPortals(Environment env, HashMap<Player, Location> portalLocations) {
        //Gets All Portals in Players Dimension
        HashMap<Player, Location> tmp = new HashMap<Player, Location>();
        for (Player player : portalLocations.keySet()) {
            if (portalLocations.get(player).getWorld().getEnvironment() == env) {
                tmp.put(player, portalLocations.get(player));
            }
        }
        return tmp;
    }

    public static HashMap<Player, Location> getTrackableTargets(Player tracker, List<Player> enemyTeam, HashMap<Player, Location> portalLocations) {
        //Returns a list of all currently trackable targets for a player.
        HashMap<Player, Location> trackableTargets = new HashMap<Player, Location>();
        //HashMap<Player, Location> trackablePortals = new HashMap<Player, Location>();
        Environment trackerEnv = tracker.getWorld().getEnvironment();
        HashMap<Player, Location> dimensionPortals = getDimensionPortals(trackerEnv, portalLocations);

        for (Player tracked : enemyTeam) {
            if (trackerEnv == tracked.getWorld().getEnvironment() && tracked.isOnline()) {
                trackableTargets.put(tracked, tracked.getLocation());
            }
        }
        for (Player tracked : dimensionPortals.keySet()) {
            if (enemyTeam.contains(tracked) && tracked.isOnline()) {
                trackableTargets.put(tracked, dimensionPortals.get(tracked));
            }
        }
        //trackableTargets.putAll(trackablePortals);
        return trackableTargets;
    }

    public static Player chooseRandomPlayer(List<Player> players){
        Random rand = new Random();
        return players.get(rand.nextInt(players.size()));
    }
}
