package com.UnicodeSnowman.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public enum Configuration {
    CONF;

    @SuppressWarnings({ "unused", "unchecked" })
    public Map<GameRule<Boolean>, Boolean> readGameRules(FileConfiguration savedConfig, String key) {
    	//Reads game rules from config
    	Map<GameRule<Boolean>, Boolean> temp=new HashMap<GameRule<Boolean>, Boolean>();
    	if (savedConfig.isConfigurationSection(key)) {
    		for (String rule: savedConfig.getConfigurationSection(key).getKeys(false)) {
        		temp.put((GameRule<Boolean>)GameRule.getByName(rule), savedConfig.getConfigurationSection(key).getBoolean(rule));
        	}
    	}
    	
    	return temp;
    }
    public List<String> readStartCommands(FileConfiguration savedConfig, String key) {
    	//Reads start commands from config
    	return savedConfig.getStringList(key);
    }

    public List<PotionType> readBannedPotions(FileConfiguration savedConfig, String key) {
    	//Reads banned potion effects from config
    	List<PotionType> temp = new ArrayList<PotionType>();
    	if (savedConfig.isList(key)) {
    		for (String potion: savedConfig.getStringList(key)) {
        		temp.add(PotionType.valueOf(potion));
        	}
    	}
    	return temp;
    }
    
    public List<Enchantment> readBannedEnchantments(FileConfiguration savedConfig, String key) {
    	//Reads banned enchantments from config
    	List<Enchantment> temp = new ArrayList<Enchantment>();
    	if (savedConfig.isList(key)) {
    		for (String enchant: savedConfig.getStringList(key)) {
        		temp.add(Enchantment.getByKey(NamespacedKey.minecraft(enchant)));
        	}
    	}
    	return temp;
    }
    
    
    public Map<GameRule<Boolean>, Boolean> gameRulesGeneral;
    public Map<GameRule<Boolean>, Boolean> hvhRules;
    public Map<GameRule<Boolean>, Boolean> mongusRules;
    
    public List<String> startCommands;
    public List<String> hvhCommands;
    public List<String> mongusCommands;
    
    public Boolean publicPunish;
    public List<PotionType> bannedPotions;
    public List<Enchantment> bannedEnchantments;

    public int worldBorder;
    public int netherBorder;
    
    public void readConfig(FileConfiguration savedConfig) {
    	this.gameRulesGeneral=this.readGameRules(savedConfig, "gameRulesGeneral");
    	this.hvhRules=this.readGameRules(savedConfig, "hvhRules");
    	this.mongusRules=this.readGameRules(savedConfig, "mongusRules");
    	
    	this.startCommands=this.readStartCommands(savedConfig, "startCommands");
    	this.hvhCommands=this.readStartCommands(savedConfig, "hvhCommands");
    	this.mongusCommands=this.readStartCommands(savedConfig, "mongusCommands");
    	
    	this.publicPunish=savedConfig.getBoolean("publicPunish");
    	this.bannedPotions = this.readBannedPotions(savedConfig, "bannedPotions");
    	this.bannedEnchantments = this.readBannedEnchantments(savedConfig, "bannedEnchantments");
    	
        this.worldBorder = savedConfig.getInt("worldBorder");
        this.netherBorder = savedConfig.getInt("netherBorder");
    }
}
