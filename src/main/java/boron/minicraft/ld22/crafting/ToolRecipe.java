package boron.minicraft.ld22.crafting;

import boron.minicraft.ld22.entity.Player;
import boron.minicraft.ld22.item.ToolItem;
import boron.minicraft.ld22.item.ToolType;

public class ToolRecipe extends Recipe {
	private ToolType type;
	private int level;

	public ToolRecipe(ToolType type, int level) {
		super(new ToolItem(type, level));
		this.type = type;
		this.level = level;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ToolItem(type, level));
	}
}
