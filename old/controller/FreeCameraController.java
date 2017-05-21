package net.jimboi.mod3.controller;

import org.bstone.input.InputManager;
import org.bstone.input.Keyboard;
import org.bstone.input.Mouse;
import net.jimboi.mod.transform.Transform3;

import org.bstone.camera.PerspectiveCamera;
import org.joml.Vector2dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 4/30/17.
 */
public class FreeCameraController extends CameraController
{
	public static final float MAX_PITCH = 80F;

	private float sensitivity = 0.01F;
	private boolean mouseLock = false;
	private float speed = 50F;
	private boolean firstUpdate = true;

	public FreeCameraController(PerspectiveCamera camera)
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

		if (this.mouseLock)
		{
			Vector2dc mouse = inputManager.getMouseMotion();

			float rotx = (float) -mouse.x() * this.sensitivity;
			float roty = (float) -mouse.y() * this.sensitivity;

			Vector3fc euler = transform.eulerRadians();
			if (euler.x() > MAX_PITCH)
			{
				if (roty > 0) roty = rotx = 0;
			}

			if (euler.x() < -MAX_PITCH)
			{
				if (roty < 0) roty = rotx = 0;
			}

			transform.rotate(roty, transform.getRight(new Vector3f()));
			transform.rotate(rotx, Transform3.YAXIS);
		}

		if (Mouse.isLeftMousePressed())
		{
			Mouse.getInputManager().getInputEngine().setCursorMode(this.mouseLock = true);
		}

		this.updateMovement(transform, delta);
	}

	public void updateMovement(Transform3 transform, double delta)
	{
		float magnitude = (float) delta * this.speed;

		Vector3f vec = new Vector3f();
		Vector3f forward = transform.getForward(vec);

		if (Keyboard.isKeyDown("forward")) transform.translate(forward, magnitude);
		if (Keyboard.isKeyDown("left")) transform.translate(transform.getRight(vec).negate(), magnitude);
		if (Keyboard.isKeyDown("backward")) transform.translate(forward, -magnitude);
		if (Keyboard.isKeyDown("right")) transform.translate(transform.getRight(vec), magnitude);
		if (Keyboard.isKeyDown("down")) transform.translate(Transform3.YAXIS, -magnitude);
		if (Keyboard.isKeyDown("up")) transform.translate(Transform3.YAXIS, magnitude);
	}

	public boolean isMouseLocked()
	{
		return this.mouseLock;
	}
}
