package me.flungo.bukkit.VoidWarp;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidWarp extends JavaPlugin {
	
	public static VoidWarp plugin;
	
	public final Logger logger = Logger.getLogger("MineCraft");
	
	public final PlayerListeners playerListener = new PlayerListeners(this);
	
	public void onDisable() {
		logMessage("Disabled.");
	}
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
		logMessage("Enabled.");
		if (getConfig().getBoolean("enable") == false) {
			logMessage("Disabled by config, type /vw enable to enable");
		}
	}
	
	public void logMessage(String msg) {
		PluginDescriptionFile pdFile = this.getDescription();
		logger.info("[" + pdFile.getName() + " v" + pdFile.getVersion() + "] " + msg);
	}
	
	public void setWarpLocation(Location loc) {
		getConfig().set("destination.world", loc.getWorld().getName());
		getConfig().set("destination.x", loc.getBlockX());
		getConfig().set("destination.z", loc.getBlockZ());
		saveConfig();
	}
	
	public void setDropHeight (int height) {
		getConfig().set("drop-height", height);
		saveConfig();
	}
	
	public void setFallDistance (int distance) {
		getConfig().set("fall-distance", distance);
		saveConfig();
	}
	
	public Location getWarpLocation(Player p) {
		String wName = getConfig().getString("destination.world");
		Location loc;
		if (getServer().getWorlds().contains(wName)) {
			World w = getServer().getWorld(wName);
			int x = getConfig().getInt("destination.x");
			int z = getConfig().getInt("destination.z");
			int y = w.getHighestBlockYAt(x, z) + getConfig().getInt("drop-height");
			loc = new Location(w, x, y, z);
		} else {
			World w = p.getWorld();
			loc = w.getSpawnLocation();
			loc.setY(w.getHighestBlockYAt(loc) + getConfig().getInt("drop-height"));
			logMessage(ChatColor.RED + "Failed to find a world named '" + wName + "'. Teleported player to " + w.getName() + "spawn. Please check config.yml.");
		}
		return loc;
	}
}
