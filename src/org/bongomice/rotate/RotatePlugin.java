package org.bongomice.rotate;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
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
		console.sendMessage(ChatColor.LIGHT_PURPLE + "[Rotate] Config loaded.");

	}
}
