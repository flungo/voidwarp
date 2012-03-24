package me.flungo.bukkit.VoidWarp;

import me.flungo.bukkit.tools.Log;
import me.flungo.bukkit.tools.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidWarp extends JavaPlugin {
	
	public static VoidWarp plugin;
    
    public PluginManager pm;
    
    public PluginDescriptionFile pdf;
	
	public final PlayerListeners playerListener = new PlayerListeners(this);
	
	public final Log logger = new Log(this);
	
	public final Permissions permissions = new Permissions(this, logger);
	
	public void onDisable() {
		disable();
		saveConfig();
		logger.logMessage("Disabled.");
	}
	
	public void onEnable() {
		pm = getServer().getPluginManager();
    	pdf = getDescription();
		getConfig().options().copyDefaults(true);
		saveConfig();
		if (getConfig().getBoolean("enable")) {
			enable();
			logger.logMessage("Enabled.");
		} else {
			logger.logMessage("Disabled by config, type /vwenable to enable");
		}
	}
	
	private void enable() {
		pm.registerEvents(this.playerListener, this);
		permissions.setupPermissions();
	}
	
	private void disable() {
		HandlerList hl = new HandlerList();
		hl.unregister(this);
	}
	
	public void EnablePlugin(boolean setTo) {
		getConfig().set("enable", setTo);
		saveConfig();
		if (getConfig().getBoolean("enable")) enable();
		else disable();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((sender instanceof Player && permissions.isAdmin(((Player) sender).getPlayer())) || !(sender instanceof Player)) {
			if (cmd.getName().equalsIgnoreCase("vwenable")) {
				if (getConfig().getBoolean("enable")) {
					if (sender instanceof Player) {
						Player p = ((Player) sender).getPlayer();
						p.sendMessage("VoidWarp already enabled.");
					} else {
						logger.logMessage(ChatColor.RED + "Already enabled.");
					}
				} else {
					EnablePlugin(true);
					logger.logMessage(ChatColor.GREEN + "Enabled via command.");
					if (sender instanceof Player) {
						Player p = ((Player) sender).getPlayer();
						p.sendMessage("VoidWarp has been enabled.");
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("vwdisable")) {
				if (getConfig().getBoolean("enable")) {
					EnablePlugin(false);
					logger.logMessage(ChatColor.DARK_RED + "Disabled via command.");
					if (sender instanceof Player) {
						Player p = ((Player) sender).getPlayer();
						p.sendMessage("VoidWarp has been disabled.");
					}
				} else {
					if (sender instanceof Player) {
						Player p = ((Player) sender).getPlayer();
						p.sendMessage("VoidWarp already disabled.");
					} else {
						logger.logMessage(ChatColor.RED + "Already disabled.");
					}
				}
				return true;
			} else if (cmd.getName().equalsIgnoreCase("vwset")) {
				if (sender instanceof Player) {
					Player p = ((Player) sender).getPlayer(); 
					Location loc = p.getLocation();
					setWarpLocation(loc);
					p.sendMessage("Warp point for VoidWarp set!");
					String w = loc.getWorld().getName();
					int x = loc.getBlockX();
					int z = loc.getBlockZ();
					logger.logMessage("Warp has been set in the world '" + w + "at the co-ordinates: " + x + ", " + z);
				} else {
					logger.logMessage(ChatColor.RED + "/vwsetwarp cannot be run from the console");
				}
				return true;
			}
		}
		return false;
	}
	
	public void setWarpLocation(Location loc) {
		getConfig().set("destination.world", loc.getWorld().getName());
		getConfig().set("destination.x", loc.getBlockX());
		getConfig().set("destination.z", loc.getBlockZ());
		saveConfig();
	}
	
	public void setDropHeight(int height) {
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
		if (Bukkit.getWorld(wName) != null) {
			World w = getServer().getWorld(wName);
			int x = getConfig().getInt("destination.x");
			int z = getConfig().getInt("destination.z");
			int y = w.getHighestBlockYAt(x, z) + getConfig().getInt("drop-height");
			loc = new Location(w, x, y, z);
		} else {
			World w = p.getWorld();
			loc = w.getSpawnLocation();
			loc.setY(w.getHighestBlockYAt(loc) + getConfig().getInt("drop-height"));
			logger.logMessage(ChatColor.RED + "Failed to find a world named '" + wName + "'. Teleported player to " + w.getName() + " spawn. Please check config.yml.");
		}
		return loc;
	}
}
