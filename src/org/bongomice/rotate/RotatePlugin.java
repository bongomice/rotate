package org.bongomice.rotate;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RotatePlugin extends JavaPlugin {
	
	public static WorldGuardPlugin WorldGuard = null;
	public static boolean usePermissions = false;
        public static int defaultToolID =  -1;
	
	@Override
	public void onEnable() {
		loadConfig();
		
		getServer().getPluginManager().registerEvents(new RotateBlock(), this);
		getServer().getPluginManager().registerEvents(new RotateLogout(), this);
		getServer().getPluginManager().registerEvents(new RotatePainting(), this);
		getCommand("rotate").setExecutor(new RotateCommand(this));
		
		Plugin guard = getServer().getPluginManager().getPlugin("WorldGuard");
		
		if (guard != null && guard instanceof WorldGuardPlugin) {
			WorldGuard = (WorldGuardPlugin) guard;
		}
	
	}
        public void loadConfig(){
            saveDefaultConfig();
		
		usePermissions = getConfig().getBoolean("use-permissions");
                defaultToolID = getConfig().getInt("default-tool");
		if (!usePermissions) {
			getLogger().info("Using permissions is currently disabled.");
		}               

                if (!RotateUtil.isValidTool(defaultToolID) && defaultToolID != -1){
                    
                        getLogger().info("Invalid default tool ID: " + defaultToolID +"!");
                        getLogger().info("Check your default tool Id. Setting default tool ID to -1.");
                        
                        defaultToolID = -1;
                        getConfig().set("default-tool", -1);
                        
                }
       }

}
