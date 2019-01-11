package boron.minicraft.ld22.entity;

import boron.minicraft.ld22.crafting.Crafting;
import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.screen.CraftingMenu;

public class Anvil extends Furniture {
	public Anvil() {
		super("Anvil");
		col = Color.get(-1, 000, 111, 222);
		sprite = 0;
		width = 3;
		height = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.anvilRecipes, player));
		return true;
	}
}