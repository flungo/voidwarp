package me.flungo.bukkit.VoidWarp;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

public class Permissions {
	public static JavaPlugin plugin;
	
	private static Log log;
	
	private static String prefix;
	
	public Permissions(JavaPlugin instance, Log logger) {
		plugin = instance;
		log = logger;
	}
	
	private static boolean op;
	
	private static boolean bukkit;
	
	private static boolean vault;
	
	private static Permission vaultPermission = null;
	
	private void setupOPPermissions() {
		if (plugin.getConfig().getBoolean("permissions.op")) {
			log.logMessage("Attempting to configure OP permissions");
			op = true;
		} else {
			log.logMessage("OP permissions disabled by config");
			op = false;
		}
	}
	
	private void setupBukkitPermissions() {
		if (plugin.getConfig().getBoolean("permissions.bukkit")) {
			log.logMessage("Attempting to configure Bukkit Super Permissions");
			bukkit = true;
		} else {
			log.logMessage("Bukkit Super Permissions disabled by config");
			bukkit = false;
		}
	}
	
	private void setupVaultPermissions() {
		if (plugin.getConfig().getBoolean("permissions.vault")) {
			log.logMessage("Attempting to configure Vault permissions");
			if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
				log.logMessage("Vault could not be found", Level.SEVERE);
				vault = false;
			} else {
				RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		        if (permissionProvider != null) {
		        	vaultPermission = permissionProvider.getProvider();
		        }
				if (vaultPermission != null)
					vault = true;
				else 
					vault = false;
			}
		} else {
			log.logMessage("Vault permissions disabled by config");
			vault = false;
		}
    }
	
	public void setupPermissions(String nodePrefix) {
		setupOPPermissions();
		if (op) {
			log.logMessage("OP permissions set up");
		} else {
			log.logMessage("OP permissions not set up", Level.WARNING);
		}
		setupBukkitPermissions();
		if (bukkit) {
			log.logMessage("Bukkit Super Permissions set up");
		} else {
			log.logMessage("Bukkit Super Permissions not set up", Level.WARNING);
		}
		setupVaultPermissions();
		if (vault) {
			log.logMessage("Vault permissions set up");
		} else {
			log.logMessage("Vault permissions not set up", Level.WARNING);
		}
		if (!vault && !bukkit) {
			log.logMessage("No permission systems have been set up. Default permissions will be used.", Level.WARNING);
			if (!op) {
				log.logMessage("Additionally, OP permissions disabled.", Level.WARNING);
			}
		}
	}
	
	public void setupPermissions() {
		String nodePrefix = plugin.getDescription().getClass().toString().toLowerCase();
		setupPermissions(nodePrefix);
	}
	
	private boolean hasNode(Player p, String node) {
		if (bukkit && p.hasPermission(node)) return true;
		if (vault && vaultPermission.has(p, node)) return true;
		return false;
	}
	
	public boolean hasPermission(Player p, String permission) {
		if (plugin.getConfig().getBoolean("permissions.default." + permission)) return true;
		String node = prefix + "." + permission;
		if (hasNode(p, node)) return true;
		return false;
	}
	
	public boolean isAdmin(Player p) {
		if (p.isOp() && op) return true;
		String node = "voidwarp.admin";
		if (hasNode(p, node)) return true;
		return false;
	}
	
	public boolean isUser(Player p) {
		if (!plugin.getConfig().getBoolean("enable")) return false;
		String node = "voidwarp.user";
		if (hasNode(p, node)) return true;
		if (isAdmin(p)) return true;
		if (!bukkit && !vault) return true;
		return false;
	}
	
}
