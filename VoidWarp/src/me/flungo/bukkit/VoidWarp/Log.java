package me.flungo.bukkit.VoidWarp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Log {
	public final Logger logger = Logger.getLogger("MineCraft");
	
	public static JavaPlugin plugin;
	
	public Log(JavaPlugin instance) {
		plugin = instance;
	}
	
	public void logMessage(String msg) {
		logMessage(msg, Level.INFO);
	}
	
	public void logMessage(String msg, Level level) {
		logger.log(level, "[" + plugin.getDescription().getName() + "] " + msg);
	}
}
