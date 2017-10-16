package org.bstone.application;

/**
 * Created by Andy on 10/6/17.
 */
public abstract class Engine
{
	private boolean dead = false;

	protected abstract boolean onStart(Application app);

	protected abstract void onUpdate(Application app);

	protected abstract void onStop(Application app);

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}
}
