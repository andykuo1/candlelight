package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.controller.FirstPersonLookController;
import net.jimboi.stage_b.glim.controller.FirstPersonMoveController;
import net.jimboi.stage_b.glim.entity.component.EntityComponentBounding;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;

import org.bstone.input.InputEngine;
import org.bstone.transform.Transform3;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.CameraController;
import org.qsilver.entity.Entity;
import org.zilar.base.GameEngine;

/**
 * Created by Andy on 6/1/17.
 */
public class CameraControllerGlim implements CameraController, InputEngine.OnInputUpdateListener
{
	private FirstPersonLookController lookController;
	private FirstPersonMoveController moveController;

	public void setTarget(Entity target, WorldGlim world)
	{
		if (target == null)
		{
			this.lookController = null;
			this.moveController = null;
		}
		else
		{
			this.lookController = new FirstPersonLookController((Transform3) target.getComponent(EntityComponentTransform.class).transform);
			this.moveController = new FirstPersonMoveController((Transform3) target.getComponent(EntityComponentTransform.class).transform, target.getComponent(EntityComponentBounding.class).bounding, world.getBoundingManager());
		}
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		GameEngine.INPUTENGINE.onInputUpdate.addListener(this);
	}

	@Override
	public boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta)
	{
		if (this.lookController != null)
		{
			this.lookController.update(camera, cameraTransform, delta);
		}

		if (this.moveController != null)
		{
			this.moveController.update(camera, cameraTransform, delta);
		}

		return true;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		GameEngine.INPUTENGINE.onInputUpdate.deleteListener(this);
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.lookController != null)
		{
			this.lookController.poll(inputEngine);
		}

		if (this.moveController != null)
		{
			this.moveController.poll(inputEngine);
		}
	}
}
