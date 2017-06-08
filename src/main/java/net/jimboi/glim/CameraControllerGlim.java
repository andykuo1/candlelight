package net.jimboi.glim;

import net.jimboi.base.Main;
import net.jimboi.glim.component.GameComponentBounding;
import net.jimboi.glim.component.GameComponentTransform;
import net.jimboi.glim.controller.FirstPersonLookController;
import net.jimboi.glim.controller.FirstPersonMoveController;
import net.jimboi.glim.gameentity.GameEntity;
import net.jimboi.mod2.transform.Transform3Q;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.input.InputEngine;

/**
 * Created by Andy on 6/1/17.
 */
public class CameraControllerGlim implements CameraController, InputEngine.OnInputUpdateListener
{
	private final FirstPersonLookController lookController;
	private final FirstPersonMoveController moveController;

	private WorldGlim world;

	public CameraControllerGlim(GameEntity target, WorldGlim world)
	{
		this.lookController = new FirstPersonLookController((Transform3Q) target.getComponent(GameComponentTransform.class).transform);
		this.moveController = new FirstPersonMoveController((Transform3Q) target.getComponent(GameComponentTransform.class).transform, target.getComponent(GameComponentBounding.class).bounding, world.getBoundingManager());
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
