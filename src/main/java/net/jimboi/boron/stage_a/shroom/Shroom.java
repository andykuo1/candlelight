package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.GameEngine;

/**
 * Created by Andy on 7/17/17.
 */
public class Shroom
{
	public static GameEngine ENGINE;

	public static void run(Class<? extends SceneShroomBase> sceneClass, String[] args)
	{
		ENGINE = new GameEngine(args);
		ENGINE.run(sceneClass);
	}

	@SuppressWarnings("unchecked")
	public static void run(String classpath, String[] args)
	{
		Class<? extends SceneShroomBase> sceneClass;
		try
		{
			Class<?> newclass = Class.forName(classpath);
			if (SceneShroomBase.class.isAssignableFrom(newclass))
			{
				sceneClass = (Class<? extends SceneShroomBase>) newclass;
			}
			else
			{
				throw new IllegalArgumentException("Not a scene! - Found invalid class with name: " + classpath);
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException("Unable to find class with name: " + classpath);
		}

		ENGINE = new GameEngine(args);
		ENGINE.run(sceneClass);
	}
}
