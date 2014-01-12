package org.bongomice.rotate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Rails;

class RotateBlock implements Listener {

	@EventHandler
	public void BlockInteracted(PlayerInteractEvent event) {
                
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		Action click = event.getAction();

		int blockID;
		byte blockData;

		try {

			blockID = block.getTypeId();
			blockData = block.getData();

		} catch(NullPointerException e) {
			return;
		}
                
		//This line is useful for coding. In normal use, it's a comment. If it's not, that's a mistake.
		player.sendMessage("Data of the block " + blockID + " : " + String.valueOf(blockData));

		if (!isRotateCalled(event, block, player)) return;

		event.setCancelled(true);
		Bukkit.getServer().getPluginManager().callEvent(new BlockBreakEvent(block, player));
		Bukkit.getServer().getPluginManager().callEvent(new BlockPlaceEvent(block, null, null, null, player, true));
                
                switch(blockID) {
                        case 44:
                        case 126: //Slabs
                                blockData = (byte)(blockData ^ 0x8);
                                break;
                        case 17 : //Wood
                        case 162:
                        case 170:

                                if (blockData < 8) { blockData += 4; }
                                else if (blockData >= 8) { blockData -= 8; }
                                else { return; }

                                if (click == Action.RIGHT_CLICK_BLOCK) {

                                        blockData -= 8;
                                        if (blockData < 0) {
                                                blockData += 12;
                                        }
                                }
                                blockData = (blockData);
                                break;
                        case 149:
                        case 93: //Repeaters and Comparators
                        case 94:
                                if (click == Action.RIGHT_CLICK_BLOCK)  {

                                    if ((blockData & 3) == 3) { blockData -=4; }

                                    blockData++;

                                } else if (click == Action.LEFT_CLICK_BLOCK) {
                                    if ((blockData % 4) == 0) { blockData +=4; }
                                    blockData--;
                                }
                                break;
                        case 134: // Stairs
                        case 135:
                        case 136:
                        case 114:
                        case 128:
                        case 53:
                        case 67:
                        case 108:
                        case 109:
                        case 156:
                        case 163:
                        case 164:
                                if (RotateUtil.hasDoubleClicked(player)) {
                                    blockData = (byte) (RotateUtil.getPlayerLastData(player, blockData) ^ 0x4);
                                } else {
                                    RotateUtil.player_last_data.put(player.getPlayerListName(), blockData);
                                    if (blockData > 3) {
                                        blockData = getNextBlockData(blockData, click, Datas_Stairs_Up);
                                    } else if (blockData <= 3) {
                                        blockData = getNextBlockData(blockData, click, Datas_Stairs_Down);
                                    } else { return; }
                                }
                                break;
                        case 144: // Heads
                                if (blockData > 1) { return; }
                                rotateHead(block, click);
                                break;
                        case 29: // Pistons
			case 33:
				if (block.isBlockPowered() || block.isBlockIndirectlyPowered()) {
                                    return;
				}
                        case 23: // Dispensers and Droppers
			case 158:
                                if (RotateUtil.hasDoubleClicked(player)) {
                                    if (blockData < 2) {blockData = 2;}
                                    else if (blockData >= 2) { blockData = 1; }
                                    else { return; }
                                } else {
                                   if (blockData < 2) { blockData ^= 1; }
                                   else if (blockData < 7) blockData = getNextBlockData(blockData, click, Datas_Pistons);
                                   else return;
                                }
                                break;
                        case 63: // Signs
			case 323:
				blockData = getNextBlockData(blockData, click, Datas_Signs);
				break;
                        case 54: // Chest
			case 146:
				if (isDoubleChest(block)) {
					switch(blockData) {
						case 2: blockData++; break;
						case 3: blockData--; break;
						case 4: blockData++; break;
						case 5: blockData--; break;
					}
					break;
				}

			case 130: // Enderchest
			case 61:  // Furnaces
			case 62:
				blockData = getNextBlockData(blockData, click, Datas_Pistons);
				break;
                        case 86: // Pumpkins and Jack'O'Lanterns
			case 91:
				blockData = getNextBlockData(blockData, click, Datas_Pumpkins);
				break;
                        case 27: // Rails
			case 28:
			case 157:
                            blockData = rotateRails(block, click, player);
                            break;
                        case 66:
                            blockData = rotateRails(block, click, player);
                            break; 
                            
                        default:
                                return;
                        
                }
                block.setData(blockData);
                block.getState().update();
                        
        }
        static final byte[] Datas_Stairs_Down = { 2, 1, 3, 0 };
        static final byte[] Datas_Stairs_Up = { 6, 5, 7, 4 };
        static final byte[] Datas_Pistons = { 5, 3, 4, 2};
        static final byte[] Datas_Signs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
        static final byte[] Datas_Pumpkins = {2, 3, 0, 1};
        static final byte[] Datas_Rails = {6, 7, 8, 9};
        static final byte[] Datas_PoweredRails_Slope = { 11, 12, 10, 13 };
        
        public byte getNextBlockData (byte data, Action click, byte[] datas) {
            
            byte i;
            for (i = 0; i < datas.length; i++) {
                if (data == datas[i]) {
                    break;
                }
            }
            
            switch (click) {
                case RIGHT_CLICK_BLOCK:
                    i++;
                    break;
                case LEFT_CLICK_BLOCK:
                    i--;
                    break;
            }
            if (i < 0) { i = (byte) (datas.length - 1); }
            if (i == datas.length) { i = 0; }
            data = datas[i];
            return data;
            
        }
	//I don't use CraftBukkit Block Faces because Down, Self and Up cannot be used for heads.
	static final BlockFace[] Skull_BlockFaces = {
		BlockFace.NORTH,
		BlockFace.NORTH_NORTH_EAST,
		BlockFace.NORTH_EAST,
		BlockFace.EAST_NORTH_EAST,
		BlockFace.EAST,
		BlockFace.EAST_SOUTH_EAST,
		BlockFace.SOUTH_EAST,
		BlockFace.SOUTH_SOUTH_EAST,
		BlockFace.SOUTH,
		BlockFace.SOUTH_SOUTH_WEST,
		BlockFace.SOUTH_WEST,
		BlockFace.WEST_SOUTH_WEST,
		BlockFace.WEST,
		BlockFace.WEST_NORTH_WEST,
		BlockFace.NORTH_WEST,
		BlockFace.NORTH_NORTH_WEST
	};
        
        static final BlockFace[] Rails_BlockFaces = {
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST,
            BlockFace.NORTH
        };

	public void rotateHead(Block block, Action click) {

		Skull head = (Skull)block.getState();

		BlockFace bf;

		try {
			bf = head.getRotation();
		} catch ( NullPointerException e) {
			return;
		}

		int i = 0;
                
		while(bf != Skull_BlockFaces[i]) {
			i++;
		}

		if (click == Action.LEFT_CLICK_BLOCK) {

			if (i < 15) { i++; }
			else if (i == 15) { i = 0; }
			else { return; }

		} else if (click == Action.RIGHT_CLICK_BLOCK) {

			if (i > 0) { i--; }
			else if (i == 0) { i = 15; }
			else { return; }

		}

		head.setRotation(Skull_BlockFaces[i]);
		head.update();
	}

	public boolean isDoubleChest(Block block){

		for (Block tempBlock : get4NearestBlocks(block)){
			if (tempBlock.getTypeId() == block.getTypeId()) {
				return true;
			}
		}

		return false;

	}
        

	public Block[] get4NearestBlocks(Block block) {

		Block[] blocks = {
			block.getRelative(BlockFace.EAST),
			block.getRelative(BlockFace.SOUTH),
                        block.getRelative(BlockFace.WEST),
                        block.getRelative(BlockFace.NORTH)
		};

		return blocks;

	}
        
        
        public Block[] get4NearestTopRails (Block block) {
            List<Block> ls = new ArrayList<Block>();
            
            Location loc = block.getLocation();
            loc.setY(loc.getBlockY() + 1);
            Block blockAbove = loc.getBlock();

            for (Block tempBlock : get4NearestBlocks(blockAbove)){
                
                int ID = tempBlock.getTypeId();
                
                if (ID == 27 || ID == 28 || ID == 157 || ID == 66) {
                        ls.add(tempBlock);
                }
                
            }
            
            Block [] blocks = new Block[ls.size()];
            
            for (int i = 0; i < ls.size(); i++) {
                blocks[i] = ls.get(i);
            }    
            return blocks;
        }
        
        
        public byte rotateRails (Block block, Action click, Player player) {
            
                byte blockData = block.getData();
                int blockID = block.getTypeId();
                boolean isNormalRail = (blockID == 66);
                
                Rails rail = (Rails)block.getState().getData();
                List<BlockFace> bfs = new ArrayList<BlockFace>();
                
                Location loc = block.getLocation();
                loc.setY(loc.getY() + 1);
                Block blockAbove = loc.getBlock();
                
                for (Block tempBlock : get4NearestTopRails(block)) {
                    bfs.add(blockAbove.getFace(tempBlock));
                }
                
                if (RotateUtil.hasDoubleClicked(player)) {
                    
                    if (isNormalRail &&   !rail.isCurve() && !rail.isOnSlope()) { blockData = 6; }
                    
                    else if (rail.isCurve() || (!isNormalRail && !rail.isOnSlope())) {
                            if (!bfs.isEmpty()) {
                                rail.setDirection(bfs.get(0), true);
                                blockData = rail.getData();
                            } else if (isNormalRail) {
                                blockData = 0;
                            }
                        
                    } else if (rail.isOnSlope()) { blockData = (byte)(0 + (block.getData() & 8)); }
                    
                } else { 
                        if (rail.isCurve()) {
                            blockData = getNextBlockData(blockData, click, Datas_Rails);
                        } else if (!rail.isCurve() && !rail.isOnSlope()) {
                            blockData ^= 1;
                        } else if (rail.isOnSlope()) {
                            
                                byte[] datas = new byte[bfs.size()]; 
                                ListIterator li = bfs.listIterator();
                                
                                int i = 0;
                                while (li.hasNext()) {
                                    rail.setDirection((BlockFace)li.next(), true);
                                    datas[i] = rail.getData();
                                    i++;
                                }
                                try {
                                    blockData = getNextBlockData(block.getData(), click, datas);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    
                                    try {
                                        blockData = getNextBlockData(block.getData(), click, Datas_Pistons);
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        blockData = getNextBlockData (block.getData(), click, Datas_PoweredRails_Slope);
                                    }
                                }

                        }
                }

                return blockData;
        }
        
        @EventHandler
        public void onBlockDestroyed(BlockBreakEvent event) {
                Block block = event.getBlock();
		Player player = event.getPlayer();

		if (!isRotateCalled(event, block, player)) return;

		event.setCancelled(true);
        }
        
        public boolean isRotateCalled (Event event, Block block, Player player) {

		int toolID;
		int blockID;

		try {

			blockID = block.getTypeId();
			toolID = player.getItemInHand().getTypeId();

		} catch(NullPointerException e) {
			return false;
		}

		if (!RotateUtil.isValidTarget(blockID)) {
			return false;
		}

		if (!RotateUtil.verify(player, block, toolID)){
			return false;
		}
                
                return true;
        }
        
}