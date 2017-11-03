package net.jimboi.canary.stage_a.lantern.newglim;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.boron.base_ab.window.input.InputEngine;
import net.jimboi.boron.base_ab.window.input.InputLayer;
import net.jimboi.canary.stage_a.lantern.newglim.controller.FirstPersonLookController;
import net.jimboi.canary.stage_a.lantern.newglim.controller.FirstPersonMoveController;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentBounding;
import net.jimboi.canary.stage_a.lantern.newglim.entity.component.EntityComponentTransform;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.transform.Transform3;
import org.zilar.entity.Entity;

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
