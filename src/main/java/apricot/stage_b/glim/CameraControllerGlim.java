package apricot.stage_b.glim;

import apricot.base.OldGameEngine;
import apricot.base.entity.Entity;
import apricot.stage_b.glim.controller.FirstPersonLookController;
import apricot.stage_b.glim.controller.FirstPersonMoveController;
import apricot.stage_b.glim.entity.component.EntityComponentBounding;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;
import apricot.base.window.input.InputEngine;
import apricot.base.window.input.InputLayer;

import apricot.bstone.camera.Camera;
import apricot.bstone.camera.CameraController;
import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 6/1/17.
 */
public class CameraControllerGlim extends CameraController implements InputLayer
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
		OldGameEngine.INPUTENGINE.addInputLayer(this);
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
		OldGameEngine.INPUTENGINE.removeInputLayer(this);
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
