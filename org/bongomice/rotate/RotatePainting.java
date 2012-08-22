
package org.bongomice.rotate;

import org.bukkit.Art;
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

		 try {
			toolID = player.getItemInHand().getTypeId();
			painting =  (Painting) event.getRightClicked();			
		} catch(NullPointerException e) {		
			return; 
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
	   
       
       /*switch(art){
           
            case KEBAB: painting.setArt(Art.AZTEC);
                break;
            case AZTEC: painting.setArt(Art.ALBAN);
                break;
            case ALBAN: painting.setArt(Art.AZTEC2);
                break;
            case AZTEC2: painting.setArt(Art.BOMB);
                break;
            case BOMB: painting.setArt(Art.PLANT);
                break;
            case PLANT: painting.setArt(Art.WASTELAND);
                break;
            case WASTELAND: painting.setArt(Art.POOL);
                break;
            case POOL: painting.setArt(Art.COURBET);
                break;
            case COURBET: painting.setArt(Art.SEA);
                break;
            case SEA: painting.setArt(Art.SUNSET);
                break;
            case SUNSET: painting.setArt(Art.CREEBET);
                break;
            case CREEBET: painting.setArt(Art.WANDERER);
                break;
            case WANDERER: painting.setArt(Art.GRAHAM);
                break;
            case GRAHAM: painting.setArt(Art.MATCH);
                break;
            case MATCH: painting.setArt(Art.BUST);
                break;
            case BUST: painting.setArt(Art.STAGE);
                break;
            case STAGE: painting.setArt(Art.VOID);
                break;
            case VOID: painting.setArt(Art.SKULL_AND_ROSES );
                break;
            case SKULL_AND_ROSES: painting.setArt(Art.FIGHTERS);
                break;
            case FIGHTERS: painting.setArt(Art.POINTER);
                break;
            case POINTER: painting.setArt(Art.PIGSCENE);
                break;
            case PIGSCENE: painting.setArt(Art.BURNINGSKULL);
                break;
            case BURNINGSKULL: painting.setArt(Art.SKELETON);
                break;
            case SKELETON: painting.setArt(Art.DONKEYKONG);
                break;
            case DONKEYKONG: painting.setArt(Art.KEBAB);
                break;
            default:
                return;
    
            }*/
    }
    
}
            
