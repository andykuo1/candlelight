package canary.cuplet.scene_main.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 8/10/17.
 */
public class Explosions
{
	public static final List<ExplosionFunction> EXPLOSIONS = new ArrayList<>();

	private static ExplosionFunction register(ExplosionFunction explosion)
	{
		EXPLOSIONS.add(explosion);
		return explosion;
	}

	public static ExplosionFunction getRandomExplosion(Random random)
	{
		//return FLASH;
		//return BARS;
		return RING;
		//return EXPLOSIONS.get(random.nextInt(EXPLOSIONS.size()));
	}

	//EXPLOSIONS

	public static final ExplosionFunction FLASH = register((world, x, y) -> {
		int ix = (int) Math.floor(x);
		int iy = (int) Math.floor(y);

		int power = 6;

		for(int i = -power; i <= power; ++i)
		{
			for(int j = -power; j <= power; ++j)
			{
				if (j < 0 && (i < -power - j || i > power + j)) continue;
				if (j > 0 && (i < -power + j || i > power - j)) continue;
				Explosion.attemptFire(world, ix + i + 0.5F, iy + j + 0.5F, 1);
			}
		}
	});

	public static final ExplosionFunction BARS = register((world, x, y) -> {
		int ix = (int) Math.floor(x);
		int iy = (int) Math.floor(y);

		int power = 6;

		for(int i = -power; i <= power; ++i)
		{
			for(int j = -power; j <= power; ++j)
			{
				if (j < 0 && (i < -power - j || i > power + j)) continue;
				if (j > 0 && (i < -power + j || i > power - j)) continue;
				Explosion.attemptFire(world, ix + i + 0.5F, iy + j + 0.5F, 1 - (Math.abs(i) / (float) power));
			}
		}
	});

	public static final ExplosionFunction RING = register((world, x, y) -> {
		Explosion.explode(world, x, y, 6, true, new ArrayList<>());
	});
}
