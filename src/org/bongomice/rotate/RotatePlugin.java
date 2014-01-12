package org.bongomice.rotate;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RotatePlugin extends JavaPlugin {

	public static WorldGuardPlugin WorldGuard = null;
	public static RotatePlugin rotatePlugin = null;
	public static float Version = 0;

	public static boolean usePermissions = false;
	public static int defaultToolID =  -1;
	public static boolean savePlayersTools = true;
	public ConsoleCommandSender console = null;

	@Override
	public void onEnable() {
		// Source of the console.sendMessage(): http://forums.bukkit.org/threads/how-to-add-colors-to-console-messages.77082/
		console = getServer().getConsoleSender();
		loadConfig();

		getServer().getPluginManager().registerEvents(new RotateBlock(), this);
		getServer().getPluginManager().registerEvents(new RotateLogout(), this);
		getServer().getPluginManager().registerEvents(new RotatePainting(), this);
		getCommand("rotate").setExecutor(new RotateCommand(this));

		Plugin guard = getServer().getPluginManager().getPlugin("WorldGuard");
		Plugin rotate = getServer().getPluginManager().getPlugin("Rotate");

		rotatePlugin = (RotatePlugin)rotate;

		if (guard != null && guard instanceof WorldGuardPlugin) {
			WorldGuard = (WorldGuardPlugin) guard;
		}

	}

	public void loadConfig() {

		saveDefaultConfig();

		savePlayersTools =  getConfig().getBoolean("save-players-tools");
		usePermissions = getConfig().getBoolean("use-permissions");
		defaultToolID = getConfig().getInt("default-tool");

		if (!savePlayersTools){
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Player's tool will not be saved in the config.");
		} else {
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Player's tool will be saved in the config.");
		}
		if (!usePermissions) {
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Using permissions is currently disabled.");
		} else {
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Permissions are currently enabled.");
		}

		if (savePlayersTools && getConfig().isConfigurationSection("players")) {
			for(String key : getConfig().getConfigurationSection("players").getKeys(false)) {

				if (!RotateUtil.isValidTool(getConfig().getInt("players." + key)) && getConfig().getInt("players." + key) != -1) {

					console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Invalid tool ID for player: " +  key +". Setting ID to -1.");
					getConfig().set("players." + key, -1);

					saveConfig();
				}
				RotateUtil.user_tools.put(key, getConfig().getInt("players." + key));
			}
		}

		if (!RotateUtil.isValidTool(defaultToolID) && defaultToolID != -1) {

				console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Invalid default tool ID: " + defaultToolID +"!");
				console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Check your default tool Id. Setting default tool ID to -1.");

				defaultToolID = -1;
				getConfig().set("default-tool", -1);
				saveConfig();

		}
		if (defaultToolID == -1) {
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] No default tool defined.");
		} else {
			console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Default tool is: " + RotateUtil.getToolName(defaultToolID));
		}
                RotateUtil.valid_targets = loadRotatableBlocks();
		console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Config loaded.");

	}
        
        public int[] loadRotatableBlocks() {
                        
            Map<String, Object> booleans = getConfig().getConfigurationSection("rotatable_blocks").getValues(true);
            List<Integer> ls = new ArrayList();
            
            boolean bool = true;
            
            if (booleans.get("slab").equals(bool)) { 
                ls.add(44);
                ls.add(126);
            }
            if (booleans.get("rail").equals(bool)) {
                ls.add(66);
                ls.add(28);
                ls.add(27);
                ls.add(157);
            }
            if (booleans.get("piston").equals(bool)) { 
                ls.add(33); ls.add(29);
            }
            if (booleans.get("stair").equals(bool)) { 
                ls.add(109); ls.add(108); ls.add(67); ls.add(53); ls.add(128); ls.add(114);
                ls.add(136); ls.add(135); ls.add(134); ls.add(156); ls.add(163); ls.add(164);
            }
            if (booleans.get("wood").equals(bool)) { 
                ls.add(17); ls.add(162); ls.add(170);
            }
            if (booleans.get("sign").equals(bool)) { 
                ls.add(323);
                ls.add(63); 
            }
            if (booleans.get("dispenser").equals(bool)) {
                ls.add(23); 
            }
            if (booleans.get("dropper").equals(bool)) {
                ls.add(158); 
            }
            if (booleans.get("furnace").equals(bool)) {
                ls.add(61);
                ls.add(62); 
            }
            if (booleans.get("enderchest").equals(bool)) {
                ls.add(130); 
            }
            if (booleans.get("chest").equals(bool)) {
                ls.add(54); 
            }
            if (booleans.get("trapped_chest").equals(bool)) {
                ls.add(146); 
            }
            if (booleans.get("pumpkin").equals(bool)) {
                ls.add(86); 
            }
            if (booleans.get("jack_o_lantern").equals(bool)) {
                ls.add(91); 
            }
            if (booleans.get("head").equals(bool)) {
                ls.add(144); 
            }
            /*
            if (booleans.get("repeater").equals(bool)) {
                
            }
            if (booleans.get("comparator").equals(bool)) {
                
            } */
            ls.add(93);
            ls.add(94);
            ls.add(149);
            ls.add(170);
            
            RotateUtil.rotatable_painting = getConfig().getBoolean("rotatable_blocks.painting");
            
            int[] valid_targets = new int[ls.size()];
            
            for (int i = 0; i < ls.size(); i++) {
                valid_targets[i] = ls.get(i);
            }                   
            
            return valid_targets;
        }
}

