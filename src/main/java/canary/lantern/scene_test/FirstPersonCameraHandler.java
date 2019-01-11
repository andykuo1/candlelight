package canary.lantern.scene_test;

import canary.lantern.Lantern;

import canary.bstone.camera.Camera;
import canary.bstone.input.InputEngine;
import canary.bstone.transform.Transform;
import canary.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import canary.qsilver.util.MathUtil;

/**
 * Created by Andy on 10/20/17.
 */
public class FirstPersonCameraHandler
{
	public static final float MAX_PITCH = Transform.DEG2RAD * 80F;

	private final Camera camera;

	protected float sensitivity = 0.01F;

	protected float pitch;
	protected float yaw;

	protected Vector3f velocity = new Vector3f();
	protected float maxSpeed = 0.1F;
	protected float acceleration = 0.1F;
	protected float friction = 0.1F;

	protected float forward;
	protected float up;
	protected float right;
	protected boolean sprint;

	protected float counter;

	protected Transform3 target;

	public FirstPersonCameraHandler(Camera camera)
	{
		this.camera = camera;
	}

	public void setTarget(Transform3 target)
	{
		this.target = target;
	}

	public boolean hasTarget()
	{
		return this.target != null;
	}

	private static final Vector3f _VEC = new Vector3f();
	private static final Vector3f _RIGHT = new Vector3f();
	private static final Vector3f _FORWARD = new Vector3f();

	public void update(InputEngine input)
	{
		this.updateInput(input);

		if (this.target == null) return;

		Transform3 cameraTransform = (Transform3) this.camera.transform();

		//Look
		cameraTransform.setYaw(this.yaw);
		Vector3f right = cameraTransform.getRight(_RIGHT);
		cameraTransform.rotate(this.pitch, right);

		Transform3 transform = this.target;
		transform.rotation.set(transform.rotation);
		transform.rotation.x = 0;
		transform.rotation.z = 0;
		transform.rotation.normalize().invert();

		//Movement
		Vector3f forward = Transform3.YAXIS.cross(right, _FORWARD);

		if (this.forward != 0 || this.right != 0)
		{
			forward.normalize().mul(this.forward);
			right.normalize().mul(this.right);
			_VEC.set(0).add(forward).add(right).normalize().mul(this.getSpeed());

			this.velocity.lerp(_VEC, this.acceleration);
		}

		this.velocity.lerp(new Vector3f(), this.friction);
		_VEC.set(this.velocity);

		float dist = _VEC.length();
		if (dist != 0)
		{
			this.target.translate(_VEC.normalize(), dist);
		}

		if (this.up != 0)
		{
			this.target.translate(Transform3.YAXIS, this.up * this.getSpeed() * 2);
		}

		if (this.velocity.lengthSquared() != 0)
		{
			this.counter += this.velocity.length() * 5;
			if (this.counter > Transform.PI2) this.counter = 0;
		}

		float wiggleX = 0.03F;
		float wiggleY = 0.03F;
		Vector3fc pos = this.target.position3();
		cameraTransform.position.set(pos.x(), pos.y(), pos.z());
		cameraTransform.moveUp((float) Math.abs(Math.cos(this.counter)) * wiggleY - wiggleY / 2F);
		cameraTransform.moveRight((float) Math.cos(this.counter) * wiggleX);

		//Make sure camera is aware of changes!
		this.camera.markDirty();
	}

	public void updateInput(InputEngine input)
	{
		if (this.target == null) return;

		//Look
		boolean mouseLocked = Lantern.getLantern().getInputEngine().getMouse().getCursorMode();

		if (mouseLocked)
		{
			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					input.getRange("lookx"),
					input.getRange("looky")
			);

			float rotx = mouse.x() * this.sensitivity;
			float roty = mouse.y() * this.sensitivity;

			this.yaw += rotx;
			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
		}

		if (input.getAction("mouselock"))
		{
			Lantern.getLantern().getInputEngine().getMouse().setCursorMode(!mouseLocked);
		}

		//Move
		this.forward = 0;
		this.forward += input.getRange("forward");

		this.up = 0;
		this.up += input.getRange("up");

		this.right = 0;
		this.right += input.getRange("right");

		this.sprint = input.getState("sprint");
	}

	public void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}

	public float getSpeed()
	{
		return this.sprint ? this.maxSpeed * 2 : this.maxSpeed;
	}

	public void setSpeed(float speed)
	{
		this.maxSpeed = speed;
	}
}
