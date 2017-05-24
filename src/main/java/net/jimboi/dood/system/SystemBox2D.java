package net.jimboi.dood.system;

import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.mod.transform.Transform;
import net.jimboi.mod.transform.Transform3;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/22/17.
 */
public class SystemBox2D extends EntitySystem implements Scene.OnSceneUpdateListener, EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
{
	protected final Scene scene;
	private final World world;

	public SystemBox2D(EntityManager entityManager, Scene scene)
	{
		super(entityManager);

		this.scene = scene;
		this.world = new World(new Vec2(0, -14F));
	}

	@Override
	public void onStart()
	{
		this.registerListenable(this.scene.onSceneUpdate);
		this.registerListenable(this.entityManager.onEntityAdd);
		this.registerListenable(this.entityManager.onEntityRemove);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		this.world.step((float) delta, 10, 10);

		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentBox2DBody.class);
		for (Entity entity : entities)
		{
			ComponentTransform componentTransform = entity.getComponent(ComponentTransform.class);
			ComponentBox2DBody componentBox2DBody = entity.getComponent(ComponentBox2DBody.class);
			Transform3 transform = ((Transform3) componentTransform.transform);
			Vec2 pos = componentBox2DBody.handler.getBody().getPosition();
			float rad = componentBox2DBody.handler.getBody().getAngle();
			transform.position.x = pos.x;
			transform.position.y = pos.y;
			transform.position.z = 0;
			transform.setRoll(rad);
		}
	}

	@Override
	public void onEntityAdd(Entity entity)
	{
		if (entity.hasComponent(ComponentBox2DBody.class))
		{
			ComponentBox2DBody componentBox2DBody = entity.getComponent(ComponentBox2DBody.class);
			if (entity.hasComponent(ComponentTransform.class))
			{
				ComponentTransform componentTransform = entity.getComponent(ComponentTransform.class);
				Transform transform = componentTransform.transform;
				Vector3fc pos = transform.position();
				componentBox2DBody.handler.getBodyDef().position.x = pos.x();
				componentBox2DBody.handler.getBodyDef().position.y = pos.y();
				componentBox2DBody.handler.getBodyDef().angle = transform.eulerRadians().z();
			}
			componentBox2DBody.handler.create(this.world);
		}
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
		if (entity.hasComponent(ComponentBox2DBody.class))
		{
			ComponentBox2DBody componentBox2DBody = entity.getComponent(ComponentBox2DBody.class);
			componentBox2DBody.handler.destroy(this.world);
		}
	}

	public World getWorld()
	{
		return this.world;
	}
}
