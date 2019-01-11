package boron.stage_a.goblet.component;

import boron.bstone.entity.EntityManager;
import boron.bstone.living.LivingManager;
import boron.bstone.livingentity.LivingEntity;

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
