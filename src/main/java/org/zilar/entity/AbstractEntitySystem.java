package org.zilar.entity;

import org.qsilver.scene.Scene;

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
		if (this.active) throw new IllegalStateException("Starting system already active!");
		this.active = true;

		this.onStart(scene);
	}

	public final void stop(Scene scene)
	{
		if (!this.active) throw new IllegalStateException("Stopping system not yet active!");
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
