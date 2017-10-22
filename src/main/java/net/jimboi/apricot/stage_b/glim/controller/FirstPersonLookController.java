package net.jimboi.apricot.stage_b.glim.controller;

import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.boron.base_ab.window.input.InputEngine;

import org.bstone.camera.Camera;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
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

	protected final Transform3 target;

	public FirstPersonLookController(Transform3 target)
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
					OldInputManager.getInputMotion("mousex"),
					OldInputManager.getInputMotion("mousey")
			);

			float rotx = -mouse.x() * this.sensitivity;
			float roty = -mouse.y() * this.sensitivity;

			this.yaw += rotx;
			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
		}

		if (OldInputManager.isInputPressed("mouseleft"))
		{
			inputEngine.getMouse().setCursorMode(!mouseLocked);
		}
	}

	private static final Vector3f _VEC = new Vector3f();

	public void update(Camera camera, Transform3 cameraTransform, double delta)
	{
		cameraTransform.setYaw(this.yaw);
		Vector3f right = cameraTransform.getRight(_VEC);
		cameraTransform.rotate(this.pitch, right);

		Transform3 transform = this.target;
		transform.rotation.set(transform.rotation);
		transform.rotation.x = 0;
		transform.rotation.z = 0;
		transform.rotation.normalize().invert();
	}

	public void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}
}
