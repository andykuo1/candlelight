package boron.minicraft.ld22.level.tile;

import boron.minicraft.ld22.entity.AirWizard;
import boron.minicraft.ld22.entity.Entity;
import boron.minicraft.ld22.gfx.Screen;
import boron.minicraft.ld22.level.Level;

public class InfiniteFallTile extends Tile {
	public InfiniteFallTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
	}

	public void tick(Level level, int xt, int yt) {
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		if (e instanceof AirWizard) return true;
		return false;
	}
}
