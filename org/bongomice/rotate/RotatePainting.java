
package org.bongomice.rotate;

import org.bukkit.Art;
import org.bukkit.block.Block;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class RotatePainting implements Listener {
    
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        
		if (!(event.getRightClicked() instanceof Painting)) {
			return;
		}

		Player player =  event.getPlayer();
		Painting painting;		
		int toolID;
		Block block;

		 try {
			toolID = player.getItemInHand().getTypeId();
			painting =  (Painting) event.getRightClicked();
			block = painting.getLocation().getBlock();
		} catch(NullPointerException e) {		
			return; 
		}
		 
		if (RotatePlugin.WorldGuard != null) {
			try {
				if (!RotatePlugin.WorldGuard.canBuild(player, block)) {
					return;
				}
			} catch (NullPointerException e) {
				return;
			}
		}
		
		if (RotateUtil.getUserTool(player.getPlayerListName()) != toolID) {
			return;
		}
		
		event.setCancelled(true);
		rotatePainting(painting, Action.RIGHT_CLICK_BLOCK);


	}
    
    @EventHandler
    public void onLeftClick(PaintingBreakByEntityEvent event){
       
	   if (!(event.getRemover() instanceof Player)) {
		   return;
	   }
	   
	   Player player =  (Player)event.getRemover();
	   int toolID;
	   Painting painting;

	   try {
			toolID = player.getItemInHand().getTypeId();
			painting = event.getPainting();
	   } catch(NullPointerException e){         
			return; 
	   }
	   
		if (RotateUtil.getUserTool(player.getPlayerListName()) != toolID) {
			return;
		}

	   event.setCancelled(true);
	   rotatePainting(painting, Action.LEFT_CLICK_BLOCK);


}
    
    public void rotatePainting(Painting painting, Action click) {
       
       Art art;
		
       try {
		   art = painting.getArt();
	   } catch ( NullPointerException e){
		   return;
	   }
	   int i = 0;
       
	   while(art != Art.values()[i]){
			i++;
		}
       
       if (click ==  Action.RIGHT_CLICK_BLOCK){
		   
		   if (i == 24) {
			   art = Art.KEBAB;
		   } else {
			   art = Art.values()[i+1];
		   }
		   
		   
	   } else {
		   
		   if (i == 0) {
			   art = Art.DONKEYKONG;
		   } else {
			   art = Art.values()[i-1];
		   }
		   
	   }
	   
	   painting.setArt(art);
    }
    
}
            
