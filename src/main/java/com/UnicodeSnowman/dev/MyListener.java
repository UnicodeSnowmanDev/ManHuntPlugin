package com.UnicodeSnowman.dev;

import static com.UnicodeSnowman.dev.Configuration.CONF;
import static com.UnicodeSnowman.dev.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.network.protocol.game.PacketPlayOutCamera;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MyListener implements Listener {
    ManHunt P;

    public MyListener(ManHunt p) {
        this.P = p;
    }


    public Trackable getClosestPlayer(Player tracker) {
        Trackable closestPlayer = new Trackable(null, null);
        HashMap<Player, Location> trackableTargets = null;
        if (P.red.contains(tracker)) {
            trackableTargets = getTrackableTargets(tracker, P.blue, P.portalLocations);
        } else {
            trackableTargets = getTrackableTargets(tracker, P.red, P.portalLocations);
        }

        if (trackableTargets.size() == 0) {
            return closestPlayer;
        }
        for (Player tracked : trackableTargets.keySet()) {
            if (closestPlayer.getPlayer() == null) {
                closestPlayer.setPlayer(tracked);
                closestPlayer.setLocation(trackableTargets.get(tracked));
            }
            double trackedDistance = tracker.getLocation().distance(trackableTargets.get(tracked));
            double closestPlayerDistance = tracker.getLocation().distance(trackableTargets.get(closestPlayer.getPlayer()));
            if (trackedDistance < closestPlayerDistance) {
                closestPlayer.setPlayer(tracked);
                closestPlayer.setLocation(trackableTargets.get(tracked));
            }
        }

        return closestPlayer;
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        //Handle the cancellation of banned potions.
        List<ItemStack> results = event.getResults();
        for (ItemStack potion : results) {
            PotionMeta meta = (PotionMeta) potion.getItemMeta();
            if (meta != null) {
                if (CONF.bannedPotions.contains(meta.getBasePotionData().getType())) {
                    //Check if the PotionType is included in the banned list
                    event.setCancelled(true);
                    //Cancel the event indefinitely until an observer is present
                    List<HumanEntity> viewers = event.getContents().getViewers();
                    if (viewers.size() > 0) {
                        //Punish them lmao
                        for (HumanEntity human : viewers) {
                            Player player = Bukkit.getPlayer(human.getName());
                            punish(player);
                            player.sendMessage(ChatColor.RED + "Sorry but you cannot brew that potion.");
                        }
                    }
                    break;
                }
            }

        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        //Handles banned enchantments at the enchantment table.
        for (Enchantment itemEnchant : event.getEnchantsToAdd().keySet()) {
            if (CONF.bannedEnchantments.contains(itemEnchant)) {
                event.setCancelled(true);
                punish(event.getEnchanter());
                event.getEnchanter().sendMessage(ChatColor.RED + "Sorry but one or more of those enchantments are banned.");
                event.getEnchanter().closeInventory();
                EntityHuman entityHuman = (EntityHuman) ((CraftPlayer) event.getEnchanter()).getHandle();
                entityHuman.a((net.minecraft.world.item.ItemStack) null, 0);
                return;
            }
        }
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        //Handles anvil events to prevent enchanted books from being used.
        ItemStack item = event.getResult();
        for (Enchantment itemEnchant : item.getEnchantments().keySet()) {
            if (CONF.bannedEnchantments.contains(itemEnchant)) {
                //Set the anvil result to a joke item
                ItemStack poop = new ItemStack(Material.COCOA_BEANS);
                ItemMeta poopMeta = poop.getItemMeta();
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.AQUA + "Stinky Poo Poo!");
                poopMeta.setLore(lore);
                poopMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "POOP");
                poop.setItemMeta(poopMeta);
                event.setResult(poop);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        //GetDimensionPortals
        P.portalLocations.put(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasLore()) {
            return;
        }
        if (event.getItem().getItemMeta().getLore().contains(ChatColor.AQUA + "Right Click Me!") && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (!(P.red.contains(event.getPlayer())) && !(P.blue.contains(event.getPlayer()))) {
                event.getPlayer().sendMessage(ChatColor.RED + "You Must Be On A Team To Use This");
                return;
            }
            if (!P.isHVH && P.blue.contains(event.getPlayer())){
                event.getPlayer().sendMessage(ChatColor.RED + "You Must Be On Red Team To Use This");
                return;
            }

            Trackable closestPlayer = this.getClosestPlayer(event.getPlayer());
            if (closestPlayer.getPlayer() == null) {
                event.getPlayer().sendMessage(ChatColor.RED + "No Player To Track");
                return;
            }
            ItemStack compass = event.getItem();
            CompassMeta meta = (CompassMeta) compass.getItemMeta();
            meta.setLodestone(closestPlayer.getLocation());
            compass.setItemMeta(meta);
            boolean isPortal = P.portalLocations.containsValue(closestPlayer.getLocation());
            if (P.isMongus) {
                if (isPortal) {
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Now Tracking Portal");
                } else {
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Now Tracking Player");
                }
                return;
            }
            if (isPortal) {
                event.getPlayer().sendMessage(ChatColor.AQUA + "Now Tracking The Portal of " + closestPlayer.getPlayer().getName());
            } else {
                event.getPlayer().sendMessage(ChatColor.AQUA + "Now Tracking " + closestPlayer.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        P.portalLocations.remove(event.getEntity());
        if (P.isMongus) {
            if (P.imposter == event.getEntity()) {
                Bukkit.broadcastMessage(ChatColor.RED + "The Sussy Baka, " + ChatColor.GOLD + "" + event.getEntity().getName() + ChatColor.RED + ", has been killed");
                event.setDeathMessage("");
            } else {
                event.setDeathMessage(ChatColor.RED + event.getEntity().getName() + ChatColor.GREEN + " Was not the ImPosTEr!!!!!");
            }
        }
        if (!P.isHVH && P.blue.contains(event.getEntity())) {
            P.blue.remove(event.getEntity());
            P.blueTeam.removeEntry(event.getEntity().getName());
            P.red.add(event.getEntity());
            P.redTeam.addEntry(event.getEntity().getName());
        }
        if (event.getEntity()==P.imposter && !P.isImposterRevealed) {
            P.blueTeam.removeEntry(event.getEntity().getName());
            P.redTeam.addEntry(event.getEntity().getName());
            P.isImposterRevealed=true;
        }

        if (event.getEntity().getKiller()!=null && P.isMongus){
            for (World world: P.getServer().getWorlds()) {
                world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            }
            BukkitTask task = new achievementLock(this.P).runTaskLater(this.P, 200);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        if (P.red.contains(event.getPlayer())) {
            giveCompass(event.getPlayer());
        }
        if (P.isInvisHunt) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
                }

            }.runTaskLater(P, 5);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if (event.getPlayer().getMainHand()==MainHand.LEFT){
            Bukkit.broadcastMessage(ChatColor.RED+event.getPlayer().getName()+" Is an affront to GOD");
        }
        for (Player player: P.red) {
            if (event.getPlayer().getName().equals(player.getName())){
                P.red.set(P.red.indexOf(player), event.getPlayer());
            }
        }
        for (Player player: P.blue) {
            if (event.getPlayer().getName().equals(player.getName())){
                P.blue.set(P.blue.indexOf(player), event.getPlayer());
            }
        }
        for (Player player: P.portalLocations.keySet()){
            if (event.getPlayer().getName().equals(player.getName())){
                P.portalLocations.put(event.getPlayer(), P.portalLocations.get(player));
                P.portalLocations.remove(player);
            }
        }
        if (P.imposter!=null && event.getPlayer().getName().equals(P.imposter.getName())) {
            P.imposter=event.getPlayer();
        }
        if (P.red.contains(event.getPlayer())) {
            if (P.isMongus && P.imposter==event.getPlayer() && !P.isImposterRevealed){
                P.blueTeam.addEntry(event.getPlayer().getName());
            } else {
                P.redTeam.addEntry(event.getPlayer().getName());
            }
            giveCompass(event.getPlayer());
        } else if (P.blue.contains(event.getPlayer())) {
            P.blueTeam.addEntry(event.getPlayer().getName());
            if (P.isHVH) {
                giveCompass(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        P.blueTeam.removeEntry(event.getPlayer().getName());
        P.redTeam.removeEntry(event.getPlayer().getName());
    }

    @EventHandler
    public void onHandSwitch(PlayerChangedMainHandEvent event) {
        if (event.getMainHand() == MainHand.RIGHT) {
            Bukkit.broadcastMessage(ChatColor.RED+event.getPlayer().getName()+" Is an affront to GOD");
        }
    }
    /*
    @EventHandler
    public void onAchievement(PlayerAdvancementDoneEvent event) {
        Bukkit.broadcastMessage(event.getAdvancement().getKey().toString());
    }
    */
}


