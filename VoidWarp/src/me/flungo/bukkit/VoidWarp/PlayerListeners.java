package me.flungo.bukkit.VoidWarp;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListeners implements Listener {
	public static VoidWarp plugin;
	
	public PlayerListeners(VoidWarp instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (plugin.getConfig().getBoolean("enable")) {
			Player p = event.getPlayer();
			Location to = event.getTo();
			int y = to.getBlockY();
			if (y <= -plugin.getConfig().getInt("fall-distance")) {
				Location warp = plugin.getWarpLocation(p);
				p.teleport(warp);
				p.setFallDistance(-plugin.getConfig().getInt("drop-height"));
			}
		}
	}
	
	@EventHandler
	public void onVoidDamage(EntityDamageEvent event) {
		if (plugin.getConfig().getBoolean("enable")) {
			Entity ent = event.getEntity();
			Location loc = ent.getLocation();
			int y = loc.getBlockY();
			int y_min = -(plugin.getConfig().getInt("fall-distance") + 500);
			if (y > y_min && ent instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID)
				event.setCancelled(true);
		}
	}
}
