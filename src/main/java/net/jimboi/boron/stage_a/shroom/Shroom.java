package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.GameEngine;

/**
 * Created by Andy on 7/17/17.
 */
public class Shroom
{
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		ENGINE = new GameEngine(SceneShroom.class, args);
		ENGINE.run();
	}
}
