package org.zilar.entity;

import net.jimboi.apricot.base.scene.Scene;

/**
 * Created by Andy on 5/22/17.
 */
public abstract class AbstractEntitySystem
{
	protected final EntityManager entityManager;

	private boolean active = false;

	public AbstractEntitySystem(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public final void start(Scene scene)
	{
		if (this.active) throw new IllegalStateException("Starting system already activeActors!");
		this.active = true;

		this.onStart(scene);
	}

	public final void stop(Scene scene)
	{
		if (!this.active) throw new IllegalStateException("Stopping system not yet activeActors!");
		this.active = false;

		this.onStop(scene);
	}

	protected abstract void onStart(Scene scene);

	protected abstract void onStop(Scene scene);

	public final EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
