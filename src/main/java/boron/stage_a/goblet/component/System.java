package boron.stage_a.goblet.component;

import boron.bstone.entity.Component;
import boron.bstone.entity.EntityManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Andy on 9/3/17.
 */
public abstract class System
{
	protected final EntityManager entityManager;
	protected final Collection<Component> components = new HashSet<>();

	public System(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public abstract void start();

	public abstract void stop();
}
