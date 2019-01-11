package boron.minicraft.ld22.entity;

import boron.minicraft.ld22.crafting.Crafting;
import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.screen.CraftingMenu;

public class Workbench extends Furniture {
	public Workbench() {
		super("Workbench");
		col = Color.get(-1, 100, 321, 431);
		sprite = 4;
		width = 3;
		height = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.workbenchRecipes, player));
		return true;
	}
}