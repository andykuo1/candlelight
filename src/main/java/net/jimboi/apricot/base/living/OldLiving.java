package net.jimboi.apricot.base.living;

/**
 * Created by Andy on 3/4/17.
 */
@Deprecated
public abstract class OldLiving
{
	int id;
	private boolean dead;

	public OldLiving()
	{
	}

	public abstract boolean onCreate();

	public void onEarlyUpdate()
	{
	}

	public void onUpdate()
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
