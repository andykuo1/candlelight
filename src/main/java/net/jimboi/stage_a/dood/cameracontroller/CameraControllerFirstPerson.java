package net.jimboi.stage_a.dood.cameracontroller;

import net.jimboi.stage_a.base.Main;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.input.InputManager;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.scene.Scene;
import org.qsilver.transform.Transform;
import org.qsilver.util.MathUtil;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerFirstPerson implements CameraController, Scene.OnSceneUpdateListener
{
	public static float MAX_PITCH = Transform.DEG2RAD * 80F;

	protected float sensitivity = 0.01F;
	protected float speed = 1F;

	protected Scene scene;
	protected Camera camera;

	protected float pitch;
	protected float yaw;
	protected float forward;
	protected float up;
	protected float right;

	protected Transform3 target;

	public CameraControllerFirstPerson(Transform3 target)
	{
		this.target = target;
	}

	public CameraControllerFirstPerson setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
		return this;
	}

	public CameraControllerFirstPerson setSpeed(float speed)
	{
		this.speed = speed;
		return this;
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		this.camera = camera;
		this.scene = Main.SCENE;
		this.scene.onSceneUpdate.addListener(this);
	}

	private static final Vector3f _VEC = new Vector3f();

	@Override
	public void onCameraUpdate(Camera camera, double delta)
	{
		boolean mouseLocked = InputManager.getInputEngine().getMouse().getCursorMode();
		if (mouseLocked)
		{
			Transform3 cameraTransform = camera.getTransform();

			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					InputManager.getInputMotion("mousex"),
					InputManager.getInputMotion("mousey"));

			float rotx = -mouse.x() * this.sensitivity;
			float roty = -mouse.y() * this.sensitivity;

			this.yaw += rotx;
			cameraTransform.setYaw(this.yaw);

			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
			Vector3f right = cameraTransform.getRight(_VEC);
			cameraTransform.rotate(this.pitch, right);

			Transform3Quat transform = (Transform3Quat) this.target;
			transform.setRotation(transform.rotation);
			transform.rotation.x = 0;
			transform.rotation.z = 0;
			transform.rotation.normalize().invert();
		}

		if (InputManager.isInputPressed("mouseleft"))
		{
			InputManager.getInputEngine().getMouse().setCursorMode(!mouseLocked);
		}

		this.forward = 0;
		if (InputManager.isInputDown("forward")) this.forward += 1F;
		if (InputManager.isInputDown("backward")) this.forward -= 1F;

		this.up = 0;
		if (InputManager.isInputDown("up")) this.up -= 1F;
		if (InputManager.isInputDown("down")) this.up += 1F;

		this.right = 0;
		if (InputManager.isInputDown("right")) this.right += 1F;
		if (InputManager.isInputDown("left")) this.right -= 1F;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		this.camera = null;
		this.scene.onSceneUpdate.deleteListener(this);
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		this.target.position.x += this.right * this.speed;
		this.target.position.y += this.up * this.speed;
		this.target.position.z += this.forward * this.speed;

		Vector3fc pos = this.target.position();
		this.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	public float getSensitivity()
	{
		return this.sensitivity;
	}

	public float getSpeed()
	{
		return this.speed;
	}
}
