package boron.minicraft.ld22.item;

import boron.minicraft.ld22.entity.Entity;
import boron.minicraft.ld22.entity.ItemEntity;
import boron.minicraft.ld22.entity.Player;
import boron.minicraft.ld22.gfx.Screen;
import boron.minicraft.ld22.level.Level;
import boron.minicraft.ld22.level.tile.Tile;
import boron.minicraft.ld22.screen.ListItem;

public class Item implements ListItem {
	public int getColor() {
		return 0;
	}

	public int getSprite() {
		return 0;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public void renderInventory(Screen screen, int x, int y) {
	}

	public boolean interact(Player player, Entity entity, int attackDir) {
		return false;
	}

	public void renderIcon(Screen screen, int x, int y) {
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		return false;
	}
	
	public boolean isDepleted() {
		return false;
	}

	public boolean canAttack() {
		return false;
	}

	public int getAttackDamageBonus(Entity e) {
		return 0;
	}

	public String getName() {
		return "";
	}

	public boolean matches(Item item) {
		return item.getClass() == getClass();
	}
}