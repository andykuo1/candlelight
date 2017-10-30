package net.jimboi.canary.stage_a.cuplet.scene_main.component;

import net.jimboi.canary.stage_a.cuplet.scene_main.GobletEntityManager;

import org.bstone.living.LivingManager;
import org.bstone.livingentity.LivingEntity;

/**
 * Created by Andy on 9/3/17.
 */
public class SystemDamageable extends SystemLiving implements LivingManager.OnUpdateLivingsListener<LivingEntity>
{
	public SystemDamageable(GobletEntityManager entityManager)
	{
		super(entityManager.getEntityManager(), entityManager.getLivingManager());
	}

	@Override
	public void start()
	{
		this.livingManager.onUpdateLivings.addListener(this);
	}

	@Override
	public void stop()
	{
		this.livingManager.onUpdateLivings.deleteListener(this);
	}

	@Override
	public void onUpdateLivings(LivingManager<LivingEntity> livingManager)
	{
		for(LivingEntity living : livingManager.getLivings())
		{
			ComponentDamageable componentDamageable = living.getComponent(ComponentDamageable.class);
			if (componentDamageable != null)
			{
				if (componentDamageable.isBeingDamaged())
				{
					componentDamageable.doDamageTick();
				}
			}
		}
	}
}
