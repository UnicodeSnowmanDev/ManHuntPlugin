package com.UnicodeSnowman.dev;

import static com.UnicodeSnowman.dev.Configuration.CONF;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ManHunt extends JavaPlugin {
    public static ManHunt P;
    public Boolean isMongus = false;
    public Boolean isHVH = false;
    public Boolean isAchievementHunt = false;
    public Boolean isInvisHunt = false;

    public HashMap<Player, Location> portalLocations = new HashMap<Player, Location>();

    public List<Player> red = new ArrayList<Player>();
    public List<Player> blue = new ArrayList<Player>();


    public Player imposter;
    public Boolean isImposterRevealed = false;

    public Boolean shuttingDown = false;

    Scoreboard board;
    Team redTeam;
    Team blueTeam;

    @Override
    public void onEnable() {
    	

        P = this;
        saveDefaultConfig();
        FileConfiguration savedConfig = getConfig();
        CONF.readConfig(savedConfig);
        //getCommand("Commands").setExecutor(new Commands(P));
        getCommand("Start").setExecutor(new Start(P));
        getCommand("mongus").setExecutor(new Mongus(P));
        getCommand("hvh").setExecutor(new HVH(P));
        getCommand("resume").setExecutor(new Resume(P));
        getCommand("compass").setExecutor(new Compass(P));
        getCommand("red").setExecutor(new Red(P));
        getCommand("blue").setExecutor(new Blue(P));
        getCommand("imposterchat").setExecutor(new ImposterChat(P));
        getCommand("imposter").setExecutor(new Imposter(P));
        getCommand("resetplayer").setExecutor(new ResetPlayer(P));
        getCommand("invishunt").setExecutor(new InvisHunt(P));
        getCommand("randomhunter").setExecutor(new RandomHunter(P));
        getServer().getPluginManager().registerEvents(new MyListener(P), this);


        this.board = Bukkit.getScoreboardManager().getMainScoreboard();
        if (this.board.getTeam("Red") == null) {
            this.board.registerNewTeam("Red");

        }
        if (this.board.getTeam("Blue") == null) {
            this.board.registerNewTeam("Blue");
        }

        this.redTeam=this.board.getTeam("Red");
        this.blueTeam=this.board.getTeam("Blue");
        this.redTeam.setColor(ChatColor.RED);
        this.blueTeam.setColor(ChatColor.BLUE);

    }

    @Override
    public void onDisable() {
        P.shuttingDown = true;
        try {
            FileOutputStream portalWriter = new FileOutputStream("./plugins/ManHunt/portals.tmp");
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(portalWriter);
            boos.writeObject(this.portalLocations);
            boos.flush();
            boos.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
        for (Player player: Bukkit.getOnlinePlayers()){
            P.blueTeam.removeEntry(player.getName());
            P.redTeam.removeEntry(player.getName());
        }
    }
}
