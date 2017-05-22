package net.jimboi.dood.system;

import net.jimboi.base.Main;
import net.jimboi.dood.ComponentControllerFirstPerson;
import net.jimboi.dood.ComponentLocalDirection;
import net.jimboi.dood.ComponentMotion;
import net.jimboi.dood.ComponentTransform;
import net.jimboi.mod.Renderer;
import net.jimboi.mod.transform.Transform3;
import net.jimboi.mod.transform.Transform3Q;

import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.bstone.util.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.entity.Entity;
import org.qsilver.entity.EntityManager;

import java.util.Collection;

/**
 * Created by Andy on 5/22/17.
 */
public class ControllerFirstPersonSystem extends EntitySystem implements InputEngine.OnInputUpdateListener
{
	public ControllerFirstPersonSystem(EntityManager entityManager)
	{
		super(entityManager);
	}

	public void onCreate()
	{
		Main.INPUTENGINE.onInputUpdate.addListener(this);
	}

	public void onUpdate(double delta)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentLocalDirection.class, ComponentMotion.class, ComponentControllerFirstPerson.class);
		for (Entity entity : entities)
		{
			this.onUpdate(entity, delta);
		}
	}

	public void onUpdate(Entity entity, double delta)
	{
		ComponentTransform componentTransform = this.entityManager.getComponentByEntity(ComponentTransform.class, entity);
		ComponentMotion componentMotion = this.entityManager.getComponentByEntity(ComponentMotion.class, entity);
		ComponentControllerFirstPerson componentControllerFirstPerson = this.entityManager.getComponentByEntity(ComponentControllerFirstPerson.class, entity);

		componentMotion.motion.x += componentControllerFirstPerson.right;
		componentMotion.motion.y += componentControllerFirstPerson.up;
		componentMotion.motion.z += componentControllerFirstPerson.forward;

		Vector3fc pos = componentTransform.transform.position();
		componentControllerFirstPerson.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		Collection<Entity> entities = this.entityManager.getEntitiesWithComponent(ComponentTransform.class, ComponentMotion.class, ComponentControllerFirstPerson.class);
		for (Entity entity : entities)
		{
			this.onInputUpdate(entity, inputEngine);
		}
	}

	public void onInputUpdate(Entity entity, InputEngine inputEngine)
	{
		ComponentTransform componentTransform = entity.getComponent(ComponentTransform.class);
		ComponentControllerFirstPerson componentControllerFirstPerson = entity.getComponent(ComponentControllerFirstPerson.class);

		if (componentControllerFirstPerson.mouseLock)
		{
			Transform3 cameraTransform = (Transform3) componentControllerFirstPerson.camera.getTransform();

			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					InputManager.getInputMotion("mousex"),
					InputManager.getInputMotion("mousey"));

			float rotx = -mouse.x() * componentControllerFirstPerson.sensitivity;
			float roty = -mouse.y() * componentControllerFirstPerson.sensitivity;

			componentControllerFirstPerson.yaw += rotx;
			cameraTransform.setYaw(componentControllerFirstPerson.yaw);

			componentControllerFirstPerson.pitch += roty;
			componentControllerFirstPerson.pitch = MathUtil.clamp(componentControllerFirstPerson.pitch, -ComponentControllerFirstPerson.MAX_PITCH, ComponentControllerFirstPerson.MAX_PITCH);
			Vector3f right = cameraTransform.getRight(new Vector3f());
			cameraTransform.rotate(componentControllerFirstPerson.pitch, right);

			Transform3Q transform = (Transform3Q) componentTransform.transform;
			transform.setRotation(Renderer.camera.getTransform().rotation);
			transform.rotation.x = 0;
			transform.rotation.z = 0;
			transform.rotation.normalize().invert();
		}

		if (InputManager.isInputPressed("mouseleft"))
		{
			inputEngine.getMouse().setCursorMode(componentControllerFirstPerson.mouseLock = !componentControllerFirstPerson.mouseLock);
		}

		componentControllerFirstPerson.forward = 0;
		if (InputManager.isInputDown("forward")) componentControllerFirstPerson.forward += 1F;
		if (InputManager.isInputDown("backward")) componentControllerFirstPerson.forward -= 1F;

		componentControllerFirstPerson.up = 0;
		if (InputManager.isInputDown("up")) componentControllerFirstPerson.up -= 1F;
		if (InputManager.isInputDown("down")) componentControllerFirstPerson.up += 1F;

		componentControllerFirstPerson.right = 0;
		if (InputManager.isInputDown("right")) componentControllerFirstPerson.right += 1F;
		if (InputManager.isInputDown("left")) componentControllerFirstPerson.right -= 1F;
	}

	public void onDestroy()
	{
		Main.INPUTENGINE.onInputUpdate.deleteListener(this);
	}
}
