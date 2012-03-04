package me.flungo.bukkit.VoidWarp;

import org.bukkit.Location;
import org.bukkit.World;
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
		Player p = event.getPlayer();
		World w = p.getWorld();
		Location to = event.getTo();
		int y = to.getBlockY();
		if (y <= -50) {
			Location spawn = new Location(w, w.getSpawnLocation().getX(), w.getHighestBlockYAt(w.getSpawnLocation())+2, w.getSpawnLocation().getZ());
			p.teleport(spawn);
			p.setFallDistance(0);
		}
	}
	
	@EventHandler
	public void onVoidDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID)
			event.setCancelled(true);
	}
}
