package canary.cuplet.scene_main.component;

import canary.cuplet.scene_main.GobletEntityManager;

import canary.bstone.gameobject.GameObject;
import canary.bstone.transform.Transform3;

/**
 * Created by Andy on 9/3/17.
 */
public class SystemMotion extends SystemLiving
{
	public SystemMotion(GobletEntityManager entityManager)
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

	//OnLateUpdate
	public void update()
	{
		for(GameObject obj : this.gameObjectManager.getGameObjects())
		{
			ComponentMotion componentMotion = obj.getComponent(ComponentMotion.class);
			if (componentMotion != null)
			{
				ComponentTransform componentTransform = obj.getComponent(ComponentTransform.class);
				Transform3 transform = componentTransform.getTransform();

				componentMotion.updateMotion();
				componentMotion.applyMotion(transform);

				ComponentBounding componentBounding = obj.getComponent(ComponentBounding.class);
				if (componentBounding != null)
				{
					componentBounding.getBoundingBox().setCenter(transform.posX(), transform.posY());
				}
			}
		}
	}
}
