package org.bongomice.rotate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class RotateUtil {

	static final Map<String, Integer> user_tools = new HashMap<String, Integer>();
        static final Map<String, Integer> players_tick = new HashMap<String, Integer>();
        static final Map<String, Byte> player_last_data = new HashMap<String, Byte>();

	static final int[] valid_tools = {
		290, 291, 292, 293, 294,  // Hoes
		271, 275, 258, 286, 279,  // Axes
		270, 274, 257, 278, 285,  // Pickaxes
		269, 273, 256, 284, 277,  // Shovels
		267, 268, 272, 276, 283,  // Swords
		280, 262                  // Sticks & Arrows
	};
	static final String[] tools_names = {
		"Wooden Hoe", "Stone Hoe", "Iron Hoe", "Diamond Hoe", "Golden hoe",
		"Wooden Axe", "Stone Axe", "Iron Axe", "Golden Axe", "Diamond Axe",
		"Wooden Pickaxe", "Stone Pickaxe", "Iron Pickaxe", "Diamond Pickaxe", "Golden Pickaxe",
		"Wooden Shovel", "Stone Shovel", "Iron Shovel", "Golden Shovel", "Diamond Pickaxe",
		"Iron Sword", "Wooden Sword", "Stone Sword", "Diamond Sword", "Golden sword",
		"Stick", "Arrow"
	};
        
        static boolean rotatable_painting;

	/* 
         * targets id:
         * 44, 126,                                             // Slabs
         * 66, 28, 27, 157,                                     // Rails
         * 33, 29,                                              // Pistons
         * 109, 108, 67, 53, 128, 114, 136, 135, 134, 156,	// Stairs
         * 17,                                                  // Wood
         * 323, 63,                                             // Signs
         * 23, 61, 62, 158,                                     // Dispensers, Droppers & Furnaces
         * 130, 54, 146,                                        // Enderchests & Chests
         * 86, 91,                                              // Pumpkins & Jack'O'Lanterns
         * 144                                                  // Heads
         * 
         */
        
        static int[] valid_targets;

	public static boolean isValidTarget(int typeID) {

		for (int i : valid_targets) {
			if (typeID == i) {
				return true;
			}
		}

		return false;
	}

	public static boolean isValidTool(int typeID) {

		for (int i : valid_tools) {
			if (typeID == i) {
				return true;
			}
		}

		return false;
	}

	public static void setUserTool(String username, int toolTypeID) {
		user_tools.put(username, toolTypeID);
		if (RotatePlugin.savePlayersTools) {
			RotateUtil.saveUser_Tools();
		}
	}

	public static int getUserTool(String username) {
		if (!user_tools.containsKey(username)) {
			return -1;
		}

		return user_tools.get(username);
	}

	public static String getToolName(int ID) {
		String Name = null;

		for (int i = 0; i < valid_tools.length; i++) {
			if (valid_tools[i] == ID) {
				Name =  tools_names[i];
				break;
			}
		}

		return Name;
	}

	public static void resetUserTool(String username) {
		if (!user_tools.containsKey(username)) {
			return;
		}

		user_tools.remove(username);

		if (RotatePlugin.savePlayersTools) {
			RotateUtil.saveUser_Tools();
		}
	}

	public static void saveUser_Tools() {
		RotatePlugin.rotatePlugin.getConfig().createSection("players");

		for(Entry<String, Integer> entry : user_tools.entrySet()) {
			RotatePlugin.rotatePlugin.getConfig().set("players." + entry.getKey(), entry.getValue());
			//I found this here: (http://forums.bukkit.org/threads/reading-saving-hashmaps-from-a-yml-file.88758/)
		}
		RotatePlugin.rotatePlugin.saveConfig();
	}
        
        public static boolean hasDoubleClicked(Player player) {
            boolean hasDoubleClicked = false;
            
            try {
                hasDoubleClicked = ((player.getTicksLived() - players_tick.get(player.getPlayerListName())) <= 5);
            } catch (NullPointerException e) { RotateUtil.players_tick.put(player.getPlayerListName(), player.getTicksLived()); }
            
            RotateUtil.players_tick.put(player.getPlayerListName(), player.getTicksLived());
            
            return hasDoubleClicked;
        }
        
        public static byte getPlayerLastData (Player player, byte actualData) {
            byte lastData;
            
            try {
                lastData = player_last_data.get(player.getPlayerListName());
                
            } catch (NullPointerException e) { 
                lastData = actualData;
            }
            
            return lastData;
        }

	public static boolean verify(Player player, Block block, int toolID){

		if (RotatePlugin.WorldGuard != null) {
			try {
				if (!RotatePlugin.WorldGuard.canBuild(player, block)) {
					return false;
				}
			} catch (NullPointerException e) {
				return false;
			}
		}

		if (RotatePlugin.usePermissions && !player.hasPermission("rotate.access")){
			return false;
		}

		if (RotateUtil.getUserTool(player.getPlayerListName()) != toolID
			&& RotatePlugin.defaultToolID != toolID) {
			return false;
		}


		/* To avoid player using 2 tools (default and player's defined item), it checks if the player has not already a tool ID defined,
		 * and if he has a tool ID defined, he can't use the default tool ID,*
		 * excepted if his tool ID is the same as the default tool ID
		 */
		if (RotateUtil.getUserTool(player.getPlayerListName()) != -1
			&& toolID == RotatePlugin.defaultToolID
			&& RotateUtil.getUserTool(player.getPlayerListName()) != RotatePlugin.defaultToolID)
		{
			return false;
		}

		return true;

	}
}
