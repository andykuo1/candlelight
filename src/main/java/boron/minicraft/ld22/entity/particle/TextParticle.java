package boron.minicraft.ld22.entity.particle;

import boron.minicraft.ld22.entity.Entity;
import boron.minicraft.ld22.gfx.Color;
import boron.minicraft.ld22.gfx.Font;
import boron.minicraft.ld22.gfx.Screen;

public class TextParticle extends Entity {
	private String msg;
	private int col;
	private int time = 0;
	public double accX, accY, accZ;
	public double xx, yy, zz;

	public TextParticle(String msg, int x, int y, int col) {
		this.msg = msg;
		this.x = x;
		this.y = y;
		this.col = col;
		xx = x;
		yy = y;
		zz = 2;
		accX = random.nextGaussian() * 0.3;
		accY = random.nextGaussian() * 0.2;
		accZ = random.nextFloat() * 0.7 + 2;
	}

	public void tick() {
		time++;
		if (time > 60) {
			remove();
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
		x = (int) xx;
		y = (int) yy;
	}

	public void render(Screen screen) {
//		Font.draw(msg, screen, x - msg.length() * 4, y, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, x - msg.length() * 4 + 1, y - (int) (zz) + 1, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, x - msg.length() * 4, y - (int) (zz), col);
	}

}
