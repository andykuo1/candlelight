package boron.minicraft.ld22.entity;

import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.gfx.Screen;
import boron.minicraft.ld22.item.Item;
import boron.minicraft.ld22.sound.Sound;

public class ItemEntity extends Entity {
	private int lifeTime;
	protected int walkDist = 0;
	protected int dir = 0;
	public int hurtTime = 0;
	protected int xKnockback, yKnockback;
	public double accX, accY, accZ;
	public double xx, yy, zz;
	public Item item;
	private int time = 0;

	public ItemEntity(Item item, int x, int y) {
		this.item = item;
		xx = this.x = x;
		yy = this.y = y;
		width = 3;
		height = 3;

		zz = 2;
		accX = random.nextGaussian() * 0.3;
		accY = random.nextGaussian() * 0.2;
		accZ = random.nextFloat() * 0.7 + 1;

		lifeTime = 60 * 10 + random.nextInt(60);
	}

	public void tick() {
		time++;
		if (time >= lifeTime) {
			remove();
			return;
		}
		xx += accX;
		yy += accY;
		zz += accZ;
		if (zz < 0) {
			zz = 0;
			accZ *= -0.5;
			accX *= 0.6;
			accY *= 0.6;
		}
		accZ -= 0.15;
		int ox = x;
		int oy = y;
		int nx = (int) xx;
		int ny = (int) yy;
		int expectedx = nx - x;
		int expectedy = ny - y;
		move(nx - x, ny - y);
		int gotx = x - ox;
		int goty = y - oy;
		xx += gotx - expectedx;
		yy += goty - expectedy;

		if (hurtTime > 0) hurtTime--;
	}

	public boolean isBlockableBy(Mob mob) {
		return false;
	}

	public void render(Screen screen) {
		if (time >= lifeTime - 6 * 20) {
			if (time / 6 % 2 == 0) return;
		}
		screen.render(x - 4, y - 4, item.getSprite(), Color.get(-1, 0, 0, 0), 0);
		screen.render(x - 4, y - 4 - (int) (zz), item.getSprite(), item.getColor(), 0);
	}

	protected void touchedBy(Entity entity) {
		if (time > 30) entity.touchItem(this);
	}

	public void take(Player player) {
		Sound.pickup.play();
		player.score++;
		item.onTake(this);
		remove();
	}
}
