package org.bongomice.rotate;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

enum RotateCommandArguments {
	tool,
	reset,
	help,
	getdefaulttool,
	setdefaulttool
}

public class RotateCommand implements CommandExecutor {

	RotateCommand(Plugin plugin) { }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to use Rotate.");
			return false;
		}

		Player player = (Player) sender;

		if (RotatePlugin.usePermissions && !player.hasPermission("rotate.access")) {
			player.sendMessage(ChatColor.RED + "You do not have the permission to use Rotate.");
			return true;
		}

		RotateCommandArguments argument = RotateCommandArguments.help;

		if (args.length != 0) {
			try {
				argument = RotateCommandArguments.valueOf(args[0].toLowerCase());
			} catch (IllegalArgumentException e) { }
		}

		int currentTool = player.getItemInHand().getTypeId();

		switch (argument) {

			case tool:

				if (!RotateUtil.isValidTool(currentTool)) {
					player.sendMessage(ChatColor.RED + "The item you are holding can not be used with Rotate.");
				} else {
					RotateUtil.setUserTool(player.getPlayerListName(), currentTool);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Rotate is enabled on your current tool.");
				}

				break;

			case reset:
				RotateUtil.resetUserTool(player.getPlayerListName());
				player.sendMessage(ChatColor.LIGHT_PURPLE + "Your Rotate tool has been reset.");
				break;

			case getdefaulttool:
				if (RotatePlugin.defaultToolID == -1) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "There is no default Rotate tool.");
					return true;
				}

				player.sendMessage(ChatColor.LIGHT_PURPLE + "The current default Rotate tool is the " + RotateUtil.getToolName(RotatePlugin.defaultToolID) + ".");
				break;

			case setdefaulttool:

				if (RotatePlugin.usePermissions && !player.hasPermission("rotate.set-default-Tool")) {
					player.sendMessage(ChatColor.RED + "You do not have permission to perform this command.");
					return true;
				}
				if (!RotateUtil.isValidTool(currentTool)  && currentTool != 0) {
					player.sendMessage(ChatColor.RED + "The item you are holding can not be used with Rotate.");
					return true;
				}

				RotatePlugin.rotatePlugin.getConfig().set("default-tool", currentTool);
				RotatePlugin.defaultToolID = currentTool;

				if (currentTool == 0) {
					RotatePlugin.rotatePlugin.getConfig().set("default-tool", -1);
					RotatePlugin.defaultToolID = -1;
				}
				RotatePlugin.rotatePlugin.saveConfig();
				if (currentTool == 0) {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "There is no more default Rotate tool.");
				} else {
					player.sendMessage(ChatColor.LIGHT_PURPLE + "Your current tool has been setted as the default Rotate tool.");
				}

				break;

			case help:
			default:
				String[] helpMessage = {
					ChatColor.RED + "Rotate v1.5 - By BongoMice",
					ChatColor.RED + "Used to rotate those pesky Minecraft blocks.",
					ChatColor.RED + "Supports stairs, heads, signs, dispensers, furnaces, slabs, pistons, rails, paintings, chests, pumpkins and logs.",
					ChatColor.RED + "-> Use \"/rotate tool\" to enable Rotate on your current tool.",
					ChatColor.RED + "-> Use \"/rotate reset\" to reset the Rotate tool.",
					ChatColor.RED + "-> Use \"/rotate getDefaultTool\" to get the default Rotate tool.",
					ChatColor.RED + "-> You do not have the permission to use \"/rotate setDefaultTool\" to set the default Rotate tool."
				};

				if (player.hasPermission("rotate.set-default-tool")) {
					helpMessage[6] = ChatColor.RED + "-> Use \"/rotate setDefaultTool\" to set the default Rotate tool.";
				};

				player.sendMessage(helpMessage);
				break;

		}

		return true;

	}

}
