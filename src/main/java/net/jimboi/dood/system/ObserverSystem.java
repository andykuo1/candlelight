package net.jimboi.dood.system;

import org.qsilver.entity.EntityManager;

import java.util.Observer;

/**
 * Created by Andy on 5/22/17.
 */
public abstract class ObserverSystem extends EntitySystem implements Observer
{
	public ObserverSystem(EntityManager entityManager)
	{
		super(entityManager);
	}
}
