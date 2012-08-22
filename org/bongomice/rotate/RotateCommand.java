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
    help
}

public class RotateCommand implements CommandExecutor {

    RotateCommand(Plugin plugin) { }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    
        if(!cmd.getName().equalsIgnoreCase("rotate")) {
            return false;
        }
        
        if (!(sender instanceof Player)) {
            return false;
        }
        
        Player player = (Player) sender;
        
        RotateCommandArguments argument = RotateCommandArguments.help;
        
        if (args.length != 0) {
            try {
                argument = RotateCommandArguments.valueOf(args[0].toLowerCase());
            } catch (IllegalArgumentException e) { }
        }
        
        switch (argument) {
            
            case tool:
                int currentTool = player.getItemInHand().getTypeId();
                
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
            
            case help:
            default:
                String[] helpMessage = {
                    ChatColor.RED + "Rotate v1.2 - By BongoMice",
                    ChatColor.RED + "Used to rotate those pesky Minecraft blocks.",
                    ChatColor.RED + "Currently supports stairs, slabs, pistons, rails, paintings and logs.",
                    ChatColor.RED + "-> Use \"/rotate tool\" to enable Rotate on your current tool.",
                    ChatColor.RED + "-> Use \"/rotate reset\" to reset the Rotate tool."
                };
                
                player.sendMessage(helpMessage);
                break;
            
        }
        
        return true;
    }
    
}
