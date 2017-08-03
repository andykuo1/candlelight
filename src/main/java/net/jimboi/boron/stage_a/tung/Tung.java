package net.jimboi.boron.stage_a.tung;


import net.jimboi.boron.base.GameEngine;

/**
 * Created by Andy on 7/23/17.
 */
public class Tung
{
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		ENGINE = new GameEngine(args);
		ENGINE.run(SceneTung.class);
	}
}
