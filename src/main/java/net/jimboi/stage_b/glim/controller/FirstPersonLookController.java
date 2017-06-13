package net.jimboi.stage_b.glim.controller;

import net.jimboi.stage_b.gnome.transform.Transform;
import net.jimboi.stage_b.gnome.transform.Transform3;
import net.jimboi.stage_b.gnome.transform.Transform3Q;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 6/1/17.
 */
public class FirstPersonLookController
{
	public static final float MAX_PITCH = Transform.DEG2RAD * 80F;

	protected float sensitivity = 0.01F;

	protected float pitch;
	protected float yaw;

	protected final Transform3Q target;

	public FirstPersonLookController(Transform3Q target)
	{
		this.target = target;
	}

	public void poll(InputEngine inputEngine)
	{
		boolean mouseLocked = inputEngine.getMouse().getCursorMode();

		if (mouseLocked)
		{
			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					InputManager.getInputMotion("mousex"),
					InputManager.getInputMotion("mousey")
			);

			float rotx = -mouse.x() * this.sensitivity;
			float roty = -mouse.y() * this.sensitivity;

			this.yaw += rotx;
			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
		}

		if (InputManager.isInputPressed("mouseleft"))
		{
			inputEngine.getMouse().setCursorMode(!mouseLocked);
		}
	}

	private static final Vector3f _VEC = new Vector3f();

	public void update(Camera camera, double delta)
	{
		Transform3 cameraTransform = camera.getTransform();
		cameraTransform.setYaw(this.yaw);
		Vector3f right = cameraTransform.getRight(_VEC);
		cameraTransform.rotate(this.pitch, right);

		Transform3Q transform = this.target;
		transform.setRotation(transform.rotation);
		transform.rotation.x = 0;
		transform.rotation.z = 0;
		transform.rotation.normalize().invert();
	}

	public void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}
}
