package me.flungo.bukkit.VoidWarp;

import java.util.logging.Level;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Permissions {
	public static VoidWarp plugin;
	
	public Permissions(VoidWarp instance) {
		plugin = instance;
	}
	
	private static boolean op;
	
	private static boolean bukkit;
	
	private static boolean vault;
	
	private static Permission vaultPermission = null;
	
	private void setupOPPermissions() {
		if (plugin.getConfig().getBoolean("permissions.op")) {
			plugin.logMessage("Attempting to configure OP permissions");
			op = true;
		} else {
			plugin.logMessage("OP permissions disabled by config");
			op = false;
		}
	}
	
	private void setupBukkitPermissions() {
		if (plugin.getConfig().getBoolean("permissions.bukkit")) {
			plugin.logMessage("Attempting to configure Bukkit Super Permissions");
			bukkit = true;
		} else {
			plugin.logMessage("Bukkit Super Permissions disabled by config");
			bukkit = false;
		}
	}
	
	private void setupVaultPermissions() {
		if (plugin.getConfig().getBoolean("permissions.vault")) {
			plugin.logMessage("Attempting to configure Vault permissions");
			if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
				plugin.logMessage("Vault could not be found", Level.SEVERE);
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
			plugin.logMessage("Vault permissions disabled by config");
			vault = false;
		}
    }
	
	public void setupPermissions() {
		setupOPPermissions();
		if (op) {
			plugin.logMessage("OP permissions set up");
		} else {
			plugin.logMessage("OP permissions not set up", Level.WARNING);
		}
		setupBukkitPermissions();
		if (bukkit) {
			plugin.logMessage("Bukkit Super Permissions set up");
		} else {
			plugin.logMessage("Bukkit Super Permissions not set up", Level.WARNING);
		}
		setupVaultPermissions();
		if (vault) {
			plugin.logMessage("Vault permissions set up");
		} else {
			plugin.logMessage("Vault permissions not set up", Level.WARNING);
		}
		if (!vault && !bukkit) {
			plugin.logMessage("No permission systems have been set up. All users will be teleported in void.", Level.WARNING);
			if (!op) {
				plugin.logMessage("Additionally, OP permissions disabled. No ingame commands.", Level.WARNING);
			}
		}
	}
	
	private boolean hasNode(Player p, String node) {
		if (bukkit && p.hasPermission(node)) return true;
		if (vault && vaultPermission.has(p, node)) return true;
		return false;
	}
	
	public boolean isAdmin(Player p) {
		if (p.isOp() && op) return true;
		if (plugin.getConfig().getBoolean("permissions.nodes.admin")) {
			String node = "voidwarp.admin";
			if (hasNode(p, node)) return true;
		}
		return false;
	}
	
	public boolean isUser(Player p) {
		if (!plugin.getConfig().getBoolean("enable")) return false;
		if (plugin.getConfig().getBoolean("permissions.nodes.user")) {
			String node = "voidwarp.user";
			if (hasNode(p, node)) return true;
		}
		if (isAdmin(p)) return true;
		if (!bukkit && !vault) return true;
		return false;
	}
}
