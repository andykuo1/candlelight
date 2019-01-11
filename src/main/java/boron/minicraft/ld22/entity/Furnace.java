package boron.minicraft.ld22.entity;

import boron.minicraft.ld22.crafting.Crafting;
import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.screen.CraftingMenu;

public class Furnace extends Furniture {
	public Furnace() {
		super("Furnace");
		col = Color.get(-1, 000, 222, 333);
		sprite = 3;
		width = 3;
		height = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.furnaceRecipes, player));
		return true;
	}
}