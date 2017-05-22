package net.jimboi.dood.system;

import net.jimboi.base.Main;
import net.jimboi.dood.component.ComponentControllerSideScroll;
import net.jimboi.dood.component.ComponentMotion;
import net.jimboi.dood.component.ComponentTransform;

import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;
import org.qsilver.scene.Scene;

import java.util.Collection;

/**
 * Created by Andy on 5/22/17.
 */
public class ControllerSideScrollerSystem extends EntitySystem implements Scene.OnSceneCreateListener, Scene.OnSceneDestroyListener, Scene.OnSceneUpdateListener, InputEngine.OnInputUpdateListener
{
	public ControllerSideScrollerSystem(EntityManager entityManager, Scene scene)
	{
		super(entityManager);

		scene.onSceneCreate.addListener(this);
		scene.onSceneUpdate.addListener(this);
		scene.onSceneDestroy.addListener(this);
	}

	@Override
	public void onSceneCreate()
	{
		Main.INPUTENGINE.onInputUpdate.addListener(this);
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentMotion.class, ComponentControllerSideScroll.class);
		for (Entity entity : entities)
		{
			this.onUpdate(entity, delta);
		}
	}

	public void onUpdate(Entity entity, double delta)
	{
		ComponentTransform componentTransform = this.entityManager.getComponentByEntity(ComponentTransform.class, entity);
		ComponentMotion componentMotion = this.entityManager.getComponentByEntity(ComponentMotion.class, entity);
		ComponentControllerSideScroll componentControllerSideScroll = this.entityManager.getComponentByEntity(ComponentControllerSideScroll.class, entity);

		componentMotion.motion.x += componentControllerSideScroll.right;
		componentMotion.motion.y += componentControllerSideScroll.up;

		Vector3fc pos = componentTransform.transform.position();
		componentControllerSideScroll.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentControllerSideScroll.class);
		for (Entity entity : entities)
		{
			this.onInputUpdate(entity, inputEngine);
		}
	}

	public void onInputUpdate(Entity entity, InputEngine inputEngine)
	{
		ComponentControllerSideScroll componentControllerSideScroll = entity.getComponent(ComponentControllerSideScroll.class);

		componentControllerSideScroll.up = 0;
		if (InputManager.isInputDown("forward")) componentControllerSideScroll.up += 1F;
		if (InputManager.isInputDown("backward")) componentControllerSideScroll.up -= 1F;

		componentControllerSideScroll.right = 0;
		if (InputManager.isInputDown("right")) componentControllerSideScroll.right += 1F;
		if (InputManager.isInputDown("left")) componentControllerSideScroll.right -= 1F;
	}

	@Override
	public void onSceneDestroy()
	{
		Main.INPUTENGINE.onInputUpdate.deleteListener(this);
	}
}
