package org.bongomice.rotate;

import org.bukkit.Art;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RotatePainting implements Listener {

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent event) {

		if (!(event.getRightClicked() instanceof Painting)) {
			return;
		}
                
                if (!RotateUtil.rotatable_painting) {
                    return;
                }

		Player player =  event.getPlayer();
		Painting painting;
		int toolID;
		Block block;
		Block attachedBlock;

		try {
			toolID = player.getItemInHand().getTypeId();
			painting =  (Painting) event.getRightClicked();
			block = painting.getLocation().getBlock();
			attachedBlock = getAttachedBlock(painting);
		} catch(NullPointerException e) {
			return;
		}

		if (!RotateUtil.verify(player, block, toolID)){
			return;
		}

		Bukkit.getServer().getPluginManager().callEvent(new PaintingPlaceEvent(painting, player, attachedBlock, painting.getFacing()));

		rotatePainting(painting, Action.RIGHT_CLICK_BLOCK);
	}

	@EventHandler
	public void onLeftClick(PaintingBreakByEntityEvent event) {

		if (!(event.getRemover() instanceof Player)) {
			return;
		}
                
                if (!RotateUtil.rotatable_painting) {
                    return;
                }

		Player player =  (Player)event.getRemover();
		int toolID;
		Painting painting;
		Block block;
		Block attachedBlock;

		try {
			toolID = player.getItemInHand().getTypeId();
			painting = event.getPainting();
			block = painting.getLocation().getBlock();
			attachedBlock = getAttachedBlock(painting);
		} catch(NullPointerException e) {
			return;
		}

		if (!RotateUtil.verify(player, block, toolID)){
			return;
		}

		Bukkit.getServer().getPluginManager().callEvent(new PaintingPlaceEvent(painting, player, attachedBlock, painting.getFacing()));

		rotatePainting(painting, Action.LEFT_CLICK_BLOCK);
	}

	public void rotatePainting(Painting painting, Action click) {

		Art art;

		try {
			art = painting.getArt();
		} catch ( NullPointerException e) {
			return;
		}

		int i = 0;
		while(art != Art.values()[i]) {
			i++;
		}

		int max;

		if (RotatePlugin.Version <= 1.4){ max = 25; }
		else { max = 24; }

		if (click ==  Action.RIGHT_CLICK_BLOCK) {
			if (i == max) { art = Art.KEBAB; } else { art = Art.values()[i+1]; }
		} else {
			if (i == 0) { art = Art.DONKEYKONG; } else { art = Art.values()[i-1]; }
		}

		painting.setArt(art);
	}

	public Block getAttachedBlock (Painting painting){

		Block attachedBlock;
		BlockFace attachedFace = painting.getAttachedFace();

		int X = painting.getLocation().getBlockX();
		int Y = painting.getLocation().getBlockY();
		int Z = painting.getLocation().getBlockZ();

		X += attachedFace.getModX();
		Y += attachedFace.getModY();
		Z += attachedFace.getModZ();


		attachedBlock = (new Location(painting.getWorld(), X, Y, Z)).getBlock();

		return attachedBlock;
	}

}
