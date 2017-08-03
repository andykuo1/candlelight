package net.jimboi.boron.stage_a.candle;

import net.jimboi.boron.base.GameEngine;

/**
 * Created by Andy on 7/29/17.
 */
public class Candle
{
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		ENGINE = new GameEngine(args);
		ENGINE.run(SceneCandle.class);
	}
}
