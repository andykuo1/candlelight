package boron.stage_a.goblet.component;

import boron.stage_a.goblet.GobletEntityManager;

import boron.bstone.living.LivingManager;
import boron.bstone.livingentity.LivingEntity;
import boron.bstone.transform.Transform3;

/**
 * Created by Andy on 9/3/17.
 */
public class SystemMotion extends SystemLiving implements LivingManager.OnLateUpdateLivingsListener<LivingEntity>
{
	public SystemMotion(GobletEntityManager entityManager)
	{
		super(entityManager.getEntityManager(), entityManager.getLivingManager());
	}

	@Override
	public void start()
	{
		this.livingManager.onLateUpdateLivings.addListener(this);
	}

	@Override
	public void stop()
	{
		this.livingManager.onLateUpdateLivings.deleteListener(this);
	}

	@Override
	public void onLateUpdateLivings(LivingManager<LivingEntity> livingManager)
	{
		for(LivingEntity living : livingManager.getLivings())
		{
			ComponentMotion componentMotion = living.getComponent(ComponentMotion.class);
			if (componentMotion != null)
			{
				ComponentTransform componentTransform = living.getComponent(ComponentTransform.class);
				Transform3 transform = componentTransform.getTransform();

				componentMotion.updateMotion();
				componentMotion.applyMotion(transform);

				ComponentBounding componentBounding = living.getComponent(ComponentBounding.class);
				if (componentBounding != null)
				{
					componentBounding.getBoundingBox().setCenter(transform.posX(), transform.posY());
				}
			}
		}
	}
}
