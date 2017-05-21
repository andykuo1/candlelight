package net.jimboi.mod3.controller;

import org.bstone.input.InputManager;
import org.bstone.input.Keyboard;
import org.bstone.input.Mouse;
import net.jimboi.mod.transform.Transform3;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.util.MathUtil;
import org.joml.Vector2dc;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 4/30/17.
 */
public class FirstPersonCameraController extends CameraController
{
	public static final float MAX_PITCH = (float) Math.toRadians(80F);

	private float pitch;
	private float yaw;

	private float sensitivity = 0.01F;
	private boolean mouseLock = false;
	private float speed = 10F;
	private boolean firstUpdate = true;

	public FirstPersonCameraController(PerspectiveCamera camera)
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

			this.yaw += rotx;
			transform.setYaw(this.yaw);

			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
			Vector3f right = transform.getRight(new Vector3f());
			transform.rotate(this.pitch, right);
		}

		if (Mouse.isLeftMousePressed())
		{
			Mouse.getInputManager().getInputEngine().setCursorMode(this.mouseLock = !this.mouseLock);
		}

		this.updateMovement(transform, delta);
	}

	private void updateMovement(Transform3 transform, double delta)
	{
		float magnitude = (float) delta * this.speed;

		Vector3f vec = new Vector3f();
		Vector3f forward = Transform3.YAXIS.cross(transform.getRight(vec), new Vector3f());

		if (Keyboard.isKeyDown("forward")) transform.translate(forward, magnitude);
		if (Keyboard.isKeyDown("left")) transform.translate(transform.getRight(vec).negate(), magnitude);
		if (Keyboard.isKeyDown("backward")) transform.translate(forward, -magnitude);
		if (Keyboard.isKeyDown("right")) transform.translate(transform.getRight(vec), magnitude);
		if (Keyboard.isKeyDown("down")) transform.translate(Transform3.YAXIS, magnitude);
		if (Keyboard.isKeyDown("up")) transform.translate(Transform3.YAXIS, -magnitude);
	}

	public boolean isMouseLocked()
	{
		return this.mouseLock;
	}
}
