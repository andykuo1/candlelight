package net.jimboi.boron.stage_a.goblet.component;

import org.bstone.entity.EntityManager;
import org.bstone.living.LivingManager;
import org.bstone.livingentity.LivingEntity;

/**
 * Created by Andy on 9/3/17.
 */
public abstract class SystemLiving extends System
{
	protected final LivingManager<LivingEntity> livingManager;

	public SystemLiving(EntityManager entityManager, LivingManager<LivingEntity> livingManager)
	{
		super(entityManager);
		this.livingManager = livingManager;
	}
}
