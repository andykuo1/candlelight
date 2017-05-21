package net.jimboi.mod3.controller;

import org.bstone.input.InputManager;
import org.bstone.input.Keyboard;
import net.jimboi.mod.transform.Transform3;

import org.bstone.camera.PerspectiveCamera;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 4/30/17.
 */
public class TopDownCameraController extends CameraController
{
	private float speed = 50F;
	private boolean firstUpdate = true;

	public TopDownCameraController(PerspectiveCamera camera)
	{
		super(camera);
	}

	@Override
	public void update(InputManager inputManager, Transform3 transform, double delta)
	{
		if (this.firstUpdate)
		{
			inputManager.registerKey("forward", GLFW.GLFW_KEY_W);
			inputManager.registerKey("backward", GLFW.GLFW_KEY_S);
			inputManager.registerKey("left", GLFW.GLFW_KEY_A);
			inputManager.registerKey("right", GLFW.GLFW_KEY_D);
			inputManager.registerKey("up", GLFW.GLFW_KEY_E);
			inputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
			this.firstUpdate = false;
		}

		this.updateMovement(transform, delta);
	}

	public void updateMovement(Transform3 transform, double delta)
	{
		float magnitude = (float) delta * this.speed;

		Vector3f vec = new Vector3f();
		Vector3f up = this.camera.getTransform().getUp(new Vector3f());
		up.z = 0;
		up.normalize();

		if (Keyboard.isKeyDown("forward")) transform.translate(up, magnitude);
		if (Keyboard.isKeyDown("left")) transform.translate(transform.getRight(vec).negate(), magnitude);
		if (Keyboard.isKeyDown("backward")) transform.translate(up, -magnitude);
		if (Keyboard.isKeyDown("right")) transform.translate(transform.getRight(vec), magnitude);
		if (Keyboard.isKeyDown("down")) transform.translate(Transform3.ZAXIS, -magnitude);
		if (Keyboard.isKeyDown("up")) transform.translate(Transform3.ZAXIS, magnitude);
	}
}
