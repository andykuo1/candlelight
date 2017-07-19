package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.GameEngine;
import net.jimboi.boron.stage_a.woot.SceneWoot;

/**
 * Created by Andy on 7/17/17.
 */
public class Shroom
{
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		ENGINE = new GameEngine(SceneWoot.class, args);
		ENGINE.run();
	}
}
