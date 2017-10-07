package net.jimboi.test.popcorn;

/**
 * Created by Andy on 10/6/17.
 */
public abstract class Engine
{
	private boolean dead = false;

	private final int priority;

	public Engine(int priority)
	{
		this.priority = priority;
	}

	protected abstract boolean onStart();

	protected void onResume() {}

	protected abstract void onUpdate();

	protected void onSuspend() {}

	protected abstract void onStop();

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}

	public final int getPriority()
	{
		return this.priority;
	}
}
