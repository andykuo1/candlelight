package net.jimboi.glim;

import net.jimboi.base.Main;
import net.jimboi.glim.component.EntityComponentBounding;
import net.jimboi.glim.component.EntityComponentTransform;
import net.jimboi.glim.controller.FirstPersonLookController;
import net.jimboi.glim.controller.FirstPersonMoveController;
import net.jimboi.mod.transform.Transform3Q;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/1/17.
 */
public class CameraControllerGlim implements CameraController, InputEngine.OnInputUpdateListener
{
	private final FirstPersonLookController lookController;
	private final FirstPersonMoveController moveController;

	private WorldGlim world;

	public CameraControllerGlim(Entity target, WorldGlim world)
	{
		this.lookController = new FirstPersonLookController((Transform3Q) target.getComponent(EntityComponentTransform.class).transform);
		this.moveController = new FirstPersonMoveController((Transform3Q) target.getComponent(EntityComponentTransform.class).transform, target.getComponent(EntityComponentBounding.class).bounding, world.getBoundingManager());
		this.world = world;
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		Main.INPUTENGINE.onInputUpdate.addListener(this);
	}

	@Override
	public void onCameraUpdate(Camera camera, double delta)
	{
		this.lookController.update(camera, delta);

		if (InputManager.isInputDown("sprint"))
		{
			this.moveController.setSpeed(6F);
		}
		else
		{
			this.moveController.setSpeed(2F);
		}

		this.moveController.update(camera, delta);
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		Main.INPUTENGINE.onInputUpdate.deleteListener(this);
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		this.lookController.poll(inputEngine);
		this.moveController.poll(inputEngine);
	}
}
