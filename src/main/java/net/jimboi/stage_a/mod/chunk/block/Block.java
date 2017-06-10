package net.jimboi.stage_a.mod.chunk.block;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Andy on 5/1/17.
 */
public abstract class Block
{
	private static final Map<String, Block> blocks = new HashMap<>();

	public static Block register(int blockID, String name, Block block)
	{
		blocks.put(name, block);
		block.setBlockID(blockID);
		return block;
	}

	public static final Block dirt = register(0, "dirt", new BlockBase());
	public static final Block stone = register(1, "stone", new BlockBase());

	public static Block getBlockByName(String name)
	{
		return blocks.get(name);
	}

	public static Block getBlockByID(int blockID)
	{
		Iterator<Block> iter = blocks.values().iterator();
		while(iter.hasNext())
		{
			Block tile = iter.next();
			if (tile.getBlockID() == blockID)
			{
				return tile;
			}
		}
		return null;
	}

	private String blockName;
	private int blockID = -1;

	public Block()
	{

	}

	private void setBlockID(int blockID)
	{
		this.blockID = blockID;
	}

	public int getBlockID()
	{
		return this.blockID;
	}

	private void setBlockName(String blockName)
	{
		this.blockName = blockName;
	}

	public String getBlockName()
	{
		return this.blockName;
	}
}
