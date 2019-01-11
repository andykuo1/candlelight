package apricot.stage_a.dood.cameracontroller;

import apricot.base.input.OldInputManager;
import apricot.base.scene.Scene;

import apricot.bstone.camera.Camera;
import apricot.bstone.camera.CameraController;
import apricot.bstone.transform.Transform;
import apricot.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import apricot.qsilver.util.MathUtil;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerFirstPerson extends CameraController
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
	}

	private static final Vector3f _VEC = new Vector3f();

	@Override
	public boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta)
	{
		boolean mouseLocked = OldInputManager.getInputEngine().getMouse().getCursorMode();
		if (mouseLocked)
		{
			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					OldInputManager.getInputMotion("mousex"),
					OldInputManager.getInputMotion("mousey"));

			float rotx = -mouse.x() * this.sensitivity;
			float roty = -mouse.y() * this.sensitivity;

			this.yaw += rotx;
			cameraTransform.setYaw(this.yaw);

			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
			Vector3f right = cameraTransform.getRight(_VEC);
			cameraTransform.rotate(this.pitch, right);

			Transform3 transform = (Transform3) this.target;
			transform.rotation.set(transform.rotation);
			transform.rotation.x = 0;
			transform.rotation.z = 0;
			transform.rotation.normalize().invert();
		}

		if (OldInputManager.isInputPressed("mouseleft"))
		{
			OldInputManager.getInputEngine().getMouse().setCursorMode(!mouseLocked);
		}

		this.forward = 0;
		if (OldInputManager.isInputDown("forward")) this.forward += 1F;
		if (OldInputManager.isInputDown("backward")) this.forward -= 1F;

		this.up = 0;
		if (OldInputManager.isInputDown("up")) this.up -= 1F;
		if (OldInputManager.isInputDown("down")) this.up += 1F;

		this.right = 0;
		if (OldInputManager.isInputDown("right")) this.right += 1F;
		if (OldInputManager.isInputDown("left")) this.right -= 1F;


		//Update Position
		this.target.position.x += this.right * this.speed;
		this.target.position.y += this.up * this.speed;
		this.target.position.z += this.forward * this.speed;

		Vector3fc pos = this.target.position3();
		cameraTransform.position.set(pos.x(), pos.y(), pos.z());

		return true;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		this.camera = null;
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
