package net.jimboi.stage_a.dood.system;

import net.jimboi.stage_a.dood.Box2DHandler;
import net.jimboi.stage_a.dood.component.ComponentBox2DBody;
import net.jimboi.stage_a.dood.component.ComponentTransform;
import net.jimboi.stage_a.dood.entity.Component;
import net.jimboi.stage_a.dood.entity.Entity;
import net.jimboi.stage_a.dood.entity.EntityManager;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.joml.Vector3fc;
import org.qsilver.scene.Scene;
import org.qsilver.transform.Transform;
import org.zilar.transform.Transform3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 5/22/17.
 */
public class SystemBox2D extends EntitySystem implements Scene.OnSceneUpdateListener, EntityManager.OnEntityAddListener, EntityManager.OnEntityRemoveListener
{
	private Set<Box2DHandler> handlers = new HashSet<>();

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
			Vec2 pos = componentBox2DBody.getBody().getPosition();
			float rad = componentBox2DBody.getBody().getAngle();
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
				componentBox2DBody.getBodyDef().position.x = pos.x();
				componentBox2DBody.getBodyDef().position.y = pos.y();
				componentBox2DBody.getBodyDef().angle = transform.eulerRadians().z();
			}
			componentBox2DBody.create(this.world);
			this.handlers.add(componentBox2DBody);
		}
	}

	@Override
	public void onEntityRemove(Entity entity)
	{
		if (entity.hasComponent(ComponentBox2DBody.class))
		{
			ComponentBox2DBody componentBox2DBody = entity.getComponent(ComponentBox2DBody.class);
			componentBox2DBody.destroy(this.world);
			this.handlers.remove(componentBox2DBody);
		}
	}

	public Set<Box2DHandler> getHandlers()
	{
		return this.handlers;
	}

	public Entity getEntityFromBody(Body body)
	{
		for (Box2DHandler handler : this.handlers)
		{
			if (handler.getBody() == body)
			{
				return this.entityManager.getEntityByComponent((Component) handler);
			}
		}
		return null;
	}

	public World getWorld()
	{
		return this.world;
	}
}
