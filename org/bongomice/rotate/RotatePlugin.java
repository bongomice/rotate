package org.bongomice.rotate;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RotatePlugin extends JavaPlugin {
	
	public static WorldGuardPlugin WorldGuard = null;
	public static boolean usePermissions = false;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		usePermissions = getConfig().getBoolean("use-permissions");
		if (!usePermissions) {
			getLogger().info("Using permissions is currently disabled.");
		}
		
		getServer().getPluginManager().registerEvents(new RotateBlock(), this);
		getServer().getPluginManager().registerEvents(new RotateLogout(), this);
		getServer().getPluginManager().registerEvents(new RotatePainting(), this);
		getCommand("rotate").setExecutor(new RotateCommand(this));
		
		Plugin guard = getServer().getPluginManager().getPlugin("WorldGuard");
		
		if (guard != null && guard instanceof WorldGuardPlugin) {
			WorldGuard = (WorldGuardPlugin) guard;
		}
	
	}

}
