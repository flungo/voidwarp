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
	
	public static Permission vaultPermission = null;
	
	private boolean setupVaultPermissions() {
		if (plugin.getConfig().getBoolean("permissions.vault")) {
			plugin.logMessage("Attempting to configure Vault permissions");
			if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
				plugin.logMessage("Vault is not installed", Level.SEVERE);
				return false;
			}
	        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        if (permissionProvider != null) {
	        	vaultPermission = permissionProvider.getProvider();
	        }
		} else {
			plugin.logMessage("Vault permissions disabled by config");
		}
		return (vaultPermission != null);
    }
	
	public void setupPermissions() {
		if (setupVaultPermissions()) {
			plugin.logMessage("Vault permissions set up");
		} else {
			plugin.logMessage("Vault permissions not set up");
		}
	}
	
	public boolean isAdmin(Player p) {
		if (p.isOp() && plugin.getConfig().getBoolean("permissions.op")) return true;
		if (p.hasPermission("voidwarp.admin") && plugin.getConfig().getBoolean("permissions.nodes.admin")) return true;
		return false;
	}
	
	public boolean isUser(Player p) {
		if (!plugin.getConfig().getBoolean("enable")) return false;
		if (p.hasPermission("voidwarp.admin") && plugin.getConfig().getBoolean("permissions.nodes.admin")) return true;
		if (p.hasPermission("voidwarp.user") && plugin.getConfig().getBoolean("permissions.nodes.user")) return true;
		return false;
	}
}
