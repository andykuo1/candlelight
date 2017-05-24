package net.jimboi.dood.system;

import net.jimboi.base.Main;
import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentControllerSideScroll;
import net.jimboi.dood.component.ComponentTransform;

import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.jbox2d.common.Vec2;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/23/17.
 */
public class SystemControllerSideScrollBox2D extends EntitySystem implements Scene.OnSceneUpdateListener, InputEngine.OnInputUpdateListener
{
	protected final Scene scene;
	public float distanceFromTarget = 15;

	public SystemControllerSideScrollBox2D(EntityManager entityManager, Scene scene)
	{
		super(entityManager);

		this.scene = scene;
	}

	@Override
	public void onStart()
	{
		this.registerListenable(scene.onSceneUpdate);
		this.registerListenable(Main.INPUTENGINE.onInputUpdate);
	}

	@Override
	public void onStop()
	{
		this.clear();
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentControllerSideScroll.class, ComponentBox2DBody.class);
		for (Entity entity : entities)
		{
			ComponentTransform componentTransform = this.entityManager.getComponentByEntity(ComponentTransform.class, entity);
			ComponentControllerSideScroll componentControllerSideScroll = this.entityManager.getComponentByEntity(ComponentControllerSideScroll.class, entity);
			ComponentBox2DBody componentBox2DBody = this.entityManager.getComponentByEntity(ComponentBox2DBody.class, entity);

			float mag = 14F;
			componentBox2DBody.handler.getBody().applyForceToCenter(new Vec2(componentControllerSideScroll.right * mag, componentControllerSideScroll.up * mag));

			Vector3fc pos = componentTransform.transform.position();
			componentControllerSideScroll.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z() + this.distanceFromTarget);
		}
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentControllerSideScroll.class);
		for (Entity entity : entities)
		{
			ComponentControllerSideScroll componentControllerSideScroll = entity.getComponent(ComponentControllerSideScroll.class);

			componentControllerSideScroll.up = 0;
			if (InputManager.isInputDown("forward")) componentControllerSideScroll.up += 1F;
			if (InputManager.isInputDown("backward")) componentControllerSideScroll.up -= 1F;

			componentControllerSideScroll.right = 0;
			if (InputManager.isInputDown("right")) componentControllerSideScroll.right += 1F;
			if (InputManager.isInputDown("left")) componentControllerSideScroll.right -= 1F;
		}
	}
}
