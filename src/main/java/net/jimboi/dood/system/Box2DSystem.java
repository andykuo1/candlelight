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
public class Box2DSystem extends EntitySystem implements Scene.OnSceneUpdateListener, EntityManager.OnEntityAddListener
{
	protected final Scene scene;
	private final World world;

	public Box2DSystem(EntityManager entityManager, Scene scene)
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
			Vec2 pos = componentBox2DBody.body.getPosition();
			float rad = componentBox2DBody.body.getAngle();
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
				componentBox2DBody.bodyDef.position.x = pos.x();
				componentBox2DBody.bodyDef.position.y = pos.y();
				componentBox2DBody.bodyDef.angle = transform.eulerRadians().z();
			}
			componentBox2DBody.body = this.world.createBody(componentBox2DBody.bodyDef);
			componentBox2DBody.body.createFixture(componentBox2DBody.fixtureDef);
		}
	}

	public World getWorld()
	{
		return this.world;
	}
}
