package me.flungo.bukkit.VoidWarp;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidWarp extends JavaPlugin {
	
	public static VoidWarp plugin;
	
	public final Logger logger = Logger.getLogger("MineCraft");
	
	public final PlayerListeners playerListener = new PlayerListeners(this);
	
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " is now disabled");
	}
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		PluginDescriptionFile pdffile = this.getDescription();
		getConfig().options().copyDefaults(true);
		saveConfig();
		this.logger.info(pdffile.getName() + " version " + pdffile.getVersion() + " is enabled.");
	}
	
	public Location getWarpLocation() {
		World w = getServer().getWorld(getConfig().getString("destination.world"));
		int x = getConfig().getInt("destination.x");
		int z = getConfig().getInt("destination.z");
		Location loc = new Location(w, x, w.getHighestBlockYAt(x, z), z);
		return loc;
	}
}
