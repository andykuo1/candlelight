package net.jimboi.dood.system;

import net.jimboi.dood.entity.EntityManager;

import org.bstone.util.listener.Listenable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/22/17.
 */
public abstract class EntitySystem
{
	private static List<EntitySystem> ACTIVE_SYSTEMS = new ArrayList<>();

	public static void stopAll()
	{
		while (!ACTIVE_SYSTEMS.isEmpty())
		{
			ACTIVE_SYSTEMS.get(0).stop();
		}
	}

	protected final EntityManager entityManager;

	private List<Listenable> listenables = new ArrayList<>();
	private boolean active = false;

	public EntitySystem(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public final void start()
	{
		if (this.active) throw new IllegalStateException();
		this.active = true;

		ACTIVE_SYSTEMS.add(this);

		this.onStart();
	}

	public final void stop()
	{
		if (!this.active) throw new IllegalStateException();
		this.onStop();
		this.clear();

		ACTIVE_SYSTEMS.remove(this);

		this.active = false;
	}

	public abstract void onStart();

	public abstract void onStop();

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	@SuppressWarnings("unchecked")
	protected EntitySystem registerListenable(Listenable listenable)
	{
		this.listenables.add(listenable);
		listenable.addListener(this);
		return this;
	}

	@SuppressWarnings("unchecked")
	public void clear()
	{
		for (Listenable listenable : this.listenables)
		{
			listenable.deleteListener(this);
		}
	}

	public final boolean isActive()
	{
		return this.active;
	}
}
