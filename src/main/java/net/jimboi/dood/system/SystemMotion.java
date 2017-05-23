package net.jimboi.dood.system;

import net.jimboi.dood.component.ComponentLocalDirection;
import net.jimboi.dood.component.ComponentMotion;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.mod.transform.Transform3;

import org.bstone.util.MathUtil;
import org.joml.Vector3f;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/22/17.
 */
public class SystemMotion extends EntitySystem implements Scene.OnSceneUpdateListener
{
	protected final Scene scene;

	public SystemMotion(EntityManager entityManager, Scene scene)
	{
		super(entityManager);

		this.scene = scene;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(scene.onSceneUpdate);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentLocalDirection.class, ComponentMotion.class);

		for (Entity entity : entities)
		{
			this.onUpdate(entity, delta);
		}
	}

	protected static final Vector3f _vec = new Vector3f();

	public void onUpdate(Entity entity, double delta)
	{
		ComponentLocalDirection componentLocalDirection = entity.getComponent(ComponentLocalDirection.class);
		ComponentMotion componentMotion = entity.getComponent(ComponentMotion.class);

		Transform3 transform = (Transform3) entity.getComponent(ComponentTransform.class).transform;

		//Motion
		componentMotion.velocity.x += componentMotion.motion.x * delta;
		componentMotion.velocity.y += componentMotion.motion.y * delta;
		componentMotion.velocity.z += componentMotion.motion.z * delta;
		componentMotion.motion.set(0);

		componentMotion.velocity.x = MathUtil.clamp(componentMotion.velocity.x, -componentMotion.maxVelocity.x, componentMotion.maxVelocity.x);
		componentMotion.velocity.y = MathUtil.clamp(componentMotion.velocity.y, -componentMotion.maxVelocity.y, componentMotion.maxVelocity.y);
		componentMotion.velocity.z = MathUtil.clamp(componentMotion.velocity.z, -componentMotion.maxVelocity.z, componentMotion.maxVelocity.z);
		componentMotion.velocity.mul(componentMotion.friction);

		if (Math.abs(componentMotion.velocity.x) <= componentMotion.minVelocity)
			componentMotion.velocity.x = 0;
		if (Math.abs(componentMotion.velocity.y) <= componentMotion.minVelocity)
			componentMotion.velocity.y = 0;
		if (Math.abs(componentMotion.velocity.z) <= componentMotion.minVelocity)
			componentMotion.velocity.z = 0;

		if (componentMotion.velocity.z != 0)
			transform.translate(componentLocalDirection.localDirectionVector.getForward(_vec), componentMotion.velocity.z);
		if (componentMotion.velocity.x != 0)
			transform.translate(componentLocalDirection.localDirectionVector.getRight(_vec), componentMotion.velocity.x);
		if (componentMotion.velocity.y != 0)
			transform.translate(componentLocalDirection.localDirectionVector.getUp(_vec), componentMotion.velocity.y);
	}
}
