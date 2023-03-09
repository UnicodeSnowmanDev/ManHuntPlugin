package com.UnicodeSnowman.dev;

import static com.UnicodeSnowman.dev.Configuration.CONF;
import static com.UnicodeSnowman.dev.util.*;

import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Start implements CommandExecutor {
	private ManHunt P;

	String[] choices = { "story/mine_stone", "story/upgrade_tools", "story/smelt_iron", "story/obtain_armor",
			"story/lava_bucket", "story/iron_tools", "story/deflect_arrow", "story/form_obsidian",
			"story/mine_diamond", "story/enter_the_nether", "story/shiny_gear", "story/enchant_item",
			"story/follow_ender_eye", "nether/return_to_sender", "nether/find_bastion",
			"nether/obtain_ancient_debris", "nether/fast_travel", "nether/find_fortress",
			"nether/obtain_crying_obsidian", "nether/distract_piglin", "nether/ride_strider", "nether/loot_bastion",
			"nether/get_wither_skull", "nether/obtain_blaze_rod", "nether/charge_respawn_anchor",
			"nether/explore_nether", "nether/brew_potion", "adventure/kill_a_mob", "adventure/trade",
			"adventure/ol_betsy", "adventure/sleep_in_bed", "adventure/shoot_arrow", "adventure/summon_iron_golem",
			"husbandry/breed_an_animal", "husbandry/tame_an_animal", "husbandry/fishy_business",
			"husbandry/tactical_fishing" };

	String[] names = { "Stone Age", "Getting an Upgrade", "Acquire Hardware", "Suit Up", "Hot Stuff",
			"Isn't It Iron Pick", "Not Today, Thank You", "Ice Bucket Challenge", "Diamonds!",
			"We Need to Go Deeper", "Cover Me With Diamonds", "Enchanter", "Eye Spy", "Return to Sender",
			"Those Were the Days", "Hidden in the Depths", "Subspace Bubble", "A Terrible Fortress",
			"Who is Cutting Onions?", "Oh Shiny", "This Boat Has Legs", "War Pigs", "Spooky Scary Skeleton",
			"Into Fire", "Not Quite \"Nine\" Lives", "Hot Tourist Destinations", "Local Brewery", "Monster Hunter",
			"What a Deal!", "Ol' Betsy", "Sweet Dreams", "Take Aim", "Hired Help", "The Parrots and the Bats",
			"Best Friends Forever", "Fishy Business", "Tactical Fishing" };
	
	public void setGameRule(GameRule<Boolean> rule, Boolean value) {
		for (World world: Bukkit.getWorlds()) {
			world.setGameRule(rule, value);
		}
	}
	
	public void setWorldBorder(int overworldRadius, int netherRadius) {
		for (World world: Bukkit.getWorlds()) {
			if (world.getEnvironment()==Environment.NETHER) {
				WorldBorder border=world.getWorldBorder();
				border.setCenter(0.0, 0.0);
				border.setSize(netherRadius*2);
			}
			else {
				WorldBorder border=world.getWorldBorder();
				border.setCenter(0.0, 0.0);
				border.setSize(overworldRadius*2);
			}
		}
	}
	
	public Boolean setStartCommand(String command) {
		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		return Bukkit.dispatchCommand(consoleSender, command);
	}



    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
    	//Set Game Rules
    	Bukkit.broadcastMessage(ChatColor.DARK_GREEN+"Setting Game Rules...");
    	for (GameRule<Boolean> rule: CONF.gameRulesGeneral.keySet()) {
    		this.setGameRule(rule, CONF.gameRulesGeneral.get(rule));
    	}
    	//Set World Border
    	Bukkit.broadcastMessage(ChatColor.DARK_GREEN+"Setting World Border...");
    	this.setWorldBorder(CONF.worldBorder, CONF.netherBorder);
    	//Run Starting Commands
    	Bukkit.broadcastMessage(ChatColor.DARK_GREEN+"Running Starting Commands...");
    	for (String startCommand: CONF.startCommands) {
    		if (!setStartCommand(startCommand)) {
    			Bukkit.broadcastMessage(ChatColor.RED+"Command \""+startCommand+"\" Failed To Run.");
    		}
    	}

    	if (P.isMongus) {
    		for (GameRule<Boolean> rule: CONF.mongusRules.keySet()) {
        		this.setGameRule(rule, CONF.mongusRules.get(rule));
        	}
    		for (String startCommand: CONF.mongusCommands) {
        		if (!setStartCommand(startCommand)) {
        			Bukkit.broadcastMessage(ChatColor.RED+"Command \""+startCommand+"\" Failed To Run.");
        		}
        	}
    	}
    	if (P.isHVH) {
    		for (GameRule<Boolean> rule: CONF.hvhRules.keySet()) {
        		this.setGameRule(rule, CONF.hvhRules.get(rule));
        	}
    		for (String startCommand: CONF.hvhCommands) {
        		if (!setStartCommand(startCommand)) {
        			Bukkit.broadcastMessage(ChatColor.RED+"Command \""+startCommand+"\" Failed To Run.");
        		}
        	}
    	}
		if (P.isMongus) {
			if (P.imposter!=null && P.imposter.isOnline()){
				if (!P.isImposterRevealed){
					P.blueTeam.removeEntry(P.imposter.getName());
				} else {
					P.redTeam.removeEntry(P.imposter.getName());
				}
				P.red.remove(P.imposter);
			}

			P.imposter=chooseRandomPlayer(new ArrayList<Player>(Bukkit.getOnlinePlayers()));
			for (Player player: Bukkit.getOnlinePlayers()){
				if (player!=P.imposter){
					P.blue.add(player);
				} else {
					P.red.add(player);
				}
				P.blueTeam.addEntry(player.getName());
				giveCompass(player);
			}
			Bukkit.broadcastMessage(ChatColor.AQUA + "There is one " + ChatColor.RED + "IMPOSTER" + ChatColor.AQUA + " Among Us.");
			P.imposter.sendMessage(ChatColor.AQUA + "You Are the SUSSY BAKA!!!!");
		} else if (P.isHVH){
			for (Player player: Bukkit.getOnlinePlayers()){
				giveCompass(player);
			}
		} else {
			for (Player player: P.red){
				giveCompass(player);
			}
		}
		if (P.isInvisHunt) {
			for (Player player: Bukkit.getOnlinePlayers()) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
			}

			ItemStack boots = new ItemStack(Material.IRON_BOOTS);
			boots.addEnchantment(Enchantment.BINDING_CURSE, 1);
			boots.addEnchantment(Enchantment.VANISHING_CURSE, 1);
			boots.addEnchantment(Enchantment.DURABILITY, 3);
			for (Player bluePlayer: P.blue) {
				bluePlayer.getInventory().setBoots(boots);
			}
			InvisTask task = new InvisTask(P);
			task.runTaskTimer(P, 0, 10);
		}
		Bukkit.broadcastMessage(ChatColor.BOLD+""+ChatColor.GREEN+"Game Starting... "+ChatColor.GOLD+"GO!");
        return true;
    }
    
    public Start(ManHunt p) {
    	P=p;
    }

	public static Advancement getAdvancement(String name) {
		Iterator<Advancement> it = Bukkit.getServer().advancementIterator();
		while (it.hasNext()) {
			Advancement a = it.next();
			if (a.getKey().getKey().equalsIgnoreCase(name)) {
				return a;
			}
		}
		return null;
	}
}
