package org.bongomice.rotate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class RotateLogout implements Listener {

	@EventHandler
	public void ToolReset(PlayerQuitEvent event) {
		if (RotatePlugin.savePlayersTools) {
			return;
		}
		RotateUtil.resetUserTool(event.getPlayer().getPlayerListName());
	}

}
