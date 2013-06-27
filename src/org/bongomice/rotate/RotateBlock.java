package org.bongomice.rotate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

class RotateBlock implements Listener {

	@EventHandler
	public void BlockInteracted(PlayerInteractEvent event) {

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		Action click = event.getAction();

		int toolID;
		int blockID;
		byte blockData;

		try {

			blockID = block.getTypeId();
			toolID = player.getItemInHand().getTypeId();
			blockData = block.getData();

		} catch(NullPointerException e) {
			return;
		}

		//This line is useful for coding. In normal use, it's a comment. If it's not, that's a mistake.
		//player.sendMessage("Data of the block " + blockID + " : " + String.valueOf(blockData));

		if (!RotateUtil.isValidTarget(blockID)) {
			return;
		}

		if (!RotateUtil.verify(player, block, toolID)){
			return;
		}

		event.setCancelled(true);
		Bukkit.getServer().getPluginManager().callEvent(new BlockBreakEvent(block, player));
		Bukkit.getServer().getPluginManager().callEvent(new BlockPlaceEvent(block, null, null, null, player, true));

		switch (blockID) {

			case 44: // Slabs
			case 126:
				if (blockData < 8) { blockData += 8; }
				else if (blockData >= 8) { blockData -= 8; }
				else { return; }
				break;

			case 17: // Wood
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 8) { blockData += 4; }
					else if (blockData >= 8) { blockData -= 8; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 3) { blockData -= 4; }
					else if (blockData <= 3) { blockData += 8; }
					else { return; }
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
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 7) { blockData++; }
					else if (blockData == 7) { blockData = 0; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 0) { blockData--; }
					else if (blockData == 0) { blockData = 7; }
					else { return; }

				}
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
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 5) { blockData++; }
					else if (blockData == 5) { blockData = 2; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 2) { blockData--; }
					else if (blockData == 2) { blockData = 5; }
					else { return; }
				}
				break;


			case 29: // Pistons
			case 33:
				if (block.isBlockPowered() || block.isBlockIndirectlyPowered()) {
					return;
				}
			case 23: // Dispensers and Droppers
			case 158:
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 5) { blockData++; }
					else if (blockData == 5) { blockData = 0; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 0) { blockData--; }
					else if (blockData == 0) { blockData = 5; }
					else { return; }

				}
				break;

			case 27: // Rails
			case 28:
			case 66:
			case 157:
				switch(blockData) {
					case 0:
						blockData = 1;
						break;
					case 1:
						blockData = 0;
						break;
					case 8:
						blockData = 9;
						break;
					case 9:
						blockData = 8;
						break;
				}
				break;

			case 63: // Signs
			case 323:
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 15) { blockData++; }
					else if (blockData == 15) { blockData = 0; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 0) { blockData--; }
					else if (blockData == 0) { blockData = 15; }
					else { return; }

				}
				break;

			case 86: // Pumpkins and Jack'O'Lanterns
			case 91:
				if (click == Action.LEFT_CLICK_BLOCK) {

					if (blockData < 3) { blockData++; }
					else if (blockData == 3) { blockData = 0; }
					else { return; }

				} else if (click == Action.RIGHT_CLICK_BLOCK) {

					if (blockData > 0) { blockData--; }
					else if (blockData == 0) { blockData = 3; }
					else { return; }

				}
				break;

			case 144: // Heads
				if (blockData > 1) { return; }
				rotateHead(block, click);
				break;

			default:
				return;
		}

		block.setData((byte) blockData);
	}

	//I don't use CraftBukkit Block Faces because Down, Self and Up cannot be used for heads.
	static final BlockFace[] MyBlockFaces = {
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

	public void rotateHead(Block block, Action click) {

		Skull head = (Skull)block.getState();

		BlockFace bf;

		try {
			bf = head.getRotation();
		} catch ( NullPointerException e) {
			return;
		}

		int i = 0;

		while(bf != MyBlockFaces[i]) {
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

		head.setRotation(MyBlockFaces[i]);
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

		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		World world = block.getWorld();

		Block[] blocks = {
			new Location(world, x+1, y, z).getBlock(),
			new Location(world, x-1, y, z).getBlock(),
			new Location(world, x, y, z + 1).getBlock(),
			new Location(world, x, y, z - 1).getBlock()
		};

		return blocks;

	}
}