package boron.minicraft.ld22.entity;

import boron.minicraft.ld22.crafting.Crafting;
import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.screen.CraftingMenu;

public class Oven extends Furniture {
	public Oven() {
		super("Oven");
		col = Color.get(-1, 000, 332, 442);
		sprite = 2;
		width = 3;
		height = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.ovenRecipes, player));
		return true;
	}
}