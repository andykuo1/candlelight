package org.bstone.living;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class Living
{
	private boolean dead = false;

	protected abstract void onCreate(LivingManager livingManager);

	protected abstract void onUpdate();

	protected void onLateUpdate()
	{
	}

	protected abstract void onDestroy();

	public final void setDead()
	{
		this.dead = true;
	}

	public final boolean isDead()
	{
		return this.dead;
	}
}
