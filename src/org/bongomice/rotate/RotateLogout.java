package org.bongomice.rotate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RotateLogout implements Listener {

	@EventHandler
	public void ResetPlayersData(PlayerQuitEvent event) {
            
                String name = event.getPlayer().getPlayerListName();
                
		if (!RotatePlugin.savePlayersTools) RotateUtil.resetUserTool(name);
                if (RotateUtil.players_tick.containsKey(name)) RotateUtil.players_tick.remove(name);
                if (RotateUtil.player_last_data.containsKey(name)) RotateUtil.player_last_data.remove(name);
                
	}
        
        

}
