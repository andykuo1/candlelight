package net.jimboi.dood;

import net.jimboi.mod.Renderer;
import net.jimboi.mod.transform.Transform3;

import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

/**
 * Created by Andy on 5/22/17.
 */
public class EntityPlayer
{
	public static Entity create(EntityManager entityManager)
	{
		Transform3 transform = Transform3.create();
		return entityManager.createEntity()
				.addComponent(new ComponentTransform(Transform3.create()))
				.addComponent(new ComponentLocalDirection(new LocalDirectionVectorController(transform, Renderer.camera.getTransform())))
				.addComponent(new ComponentMotion())
				.addComponent(new ComponentControllerFirstPerson(Renderer.camera))
				.addComponent(new ComponentInstanceable("ball", "bird"));
	}
}
