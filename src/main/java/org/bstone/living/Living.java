package org.bstone.living;

/**
 * Created by Andy on 3/4/17.
 */
public abstract class Living
{
	int id;
	private boolean dead;

	public Living()
	{
	}

	public abstract boolean onCreate();

	public void onEarlyUpdate()
	{
	}

	public void onUpdate(double delta)
	{
	}

	public void onLateUpdate()
	{
	}

	public void onDestroy()
	{
	}

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}

	public final int getLivingID()
	{
		return this.id;
	}
}
