package net.jimboi.test.pye;

import java.awt.Graphics;

/**
 * Created by Andy on 10/15/17.
 */
public class GameObject
{
	protected GameWorld world;
	private boolean dead = false;

	public void onCreate()
	{
	}

	public void onDestroy()
	{
	}

	public void onUpdate()
	{
	}

	public void onRender(Graphics g)
	{
	}

	public void setWorld(GameWorld world)
	{
		this.world = world;
	}

	public GameWorld getWorld()
	{
		return this.world;
	}

	public void setDead()
	{
		this.dead = true;
	}

	public boolean isDead()
	{
		return this.dead;
	}
}
