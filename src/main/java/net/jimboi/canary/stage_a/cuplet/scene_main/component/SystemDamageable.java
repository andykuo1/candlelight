package net.jimboi.canary.stage_a.cuplet.scene_main.component;

import net.jimboi.canary.stage_a.cuplet.scene_main.GobletEntityManager;

import org.bstone.gameobject.GameObject;

/**
 * Created by Andy on 9/3/17.
 */
public class SystemDamageable extends SystemLiving
{
	public SystemDamageable(GobletEntityManager entityManager)
	{
		super(entityManager);
	}

	@Override
	public void start()
	{
	}

	@Override
	public void stop()
	{
	}

	//OnUpdate
	public void update()
	{
		for(GameObject obj : this.gameObjectManager.getGameObjects())
		{
			if (obj.isDead()) continue;

			ComponentDamageable componentDamageable = obj.getComponent(ComponentDamageable.class);
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
