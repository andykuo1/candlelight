package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.stage_a.shroom.woot.collision.DynamicCollider;

import org.bstone.input.InputEngine;
import org.bstone.input.InputLayer;
import org.bstone.input.InputManager;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.CameraController;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 7/19/17.
 */
public class CameraControllerShroom extends CameraController implements InputLayer
{
	public static final float MAX_PITCH = Transform.DEG2RAD * 80F;

	protected float sensitivity = 0.01F;

	protected float pitch;
	protected float yaw;

	protected Vector3f velocity = new Vector3f();
	protected float maxSpeed = 0.4F;
	protected float acceleration = 0.4F;
	protected float friction = 0.1F;

	protected float forward;
	protected float up;
	protected float right;
	protected boolean sprint;

	protected float counter;

	protected Transform3 target;
	protected DynamicCollider collider;

	public void setTarget(Transform3 transform, DynamicCollider collider)
	{
		this.target = transform;
		this.collider = collider;
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		InputManager.getInputEngine().addInputLayer(this);
	}

	private static final Vector3f _VEC = new Vector3f();
	private static final Vector3f _RIGHT = new Vector3f();
	private static final Vector3f _FORWARD = new Vector3f();

	@Override
	public boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta)
	{
		if (this.target == null) return false;

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

			this.velocity.lerp(_VEC, (float) delta * this.acceleration);
		}

		this.velocity.lerp(new Vector3f(), this.friction);
		_VEC.set(this.velocity);

		float dist = _VEC.length();
		if (dist != 0)
		{
			//this.target.translate(_VEC.normalize(), dist);
			boolean solid = this.collider != null && cameraTransform.position.y >= 0 && cameraTransform.position.y < 2;

			if (solid)
			{
				this.collider.move(_VEC.x(), _VEC.z());
				Vector2f pos = new Vector2f();
				this.collider.update(this.target.position.x(), this.target.position.z(), pos);
				this.target.position.set(pos.x(), this.target.position.y(), pos.y());
			}
			else
			{
				this.target.translate(_VEC.x(), 0, _VEC.z());
				this.collider.setPosition(this.target.position.x(), this.target.position.z());
			}
		}

		if (this.up != 0)
		{
			this.target.translate(Transform3.YAXIS, (float) delta * this.up * this.getSpeed() * 4);
		}

		if (this.velocity.lengthSquared() != 0)
		{
			this.counter += this.velocity.length() * 5;
			if (this.counter > Transform.PI2) this.counter = 0;
		}

		float wiggleX = 0.02F;
		float wiggleY = 0.02F;
		Vector3fc pos = this.target.position3();
		cameraTransform.position.set(pos.x(), pos.y(), pos.z());
		cameraTransform.moveUp((float) Math.abs(Math.cos(this.counter)) * wiggleY - wiggleY / 2F);
		cameraTransform.moveRight((float) Math.cos(this.counter) * wiggleX);

		return true;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		InputManager.getInputEngine().removeInputLayer(this);
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.target == null) return;

		//Look
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

		if (InputManager.isInputPressed("mouselock"))
		{
			inputEngine.getMouse().setCursorMode(!mouseLocked);
		}

		//Move
		this.forward = 0;
		if (InputManager.isInputDown("forward")) this.forward += 1F;
		if (InputManager.isInputDown("backward")) this.forward -= 1F;

		this.up = 0;
		if (InputManager.isInputDown("up")) this.up -= 1F;
		if (InputManager.isInputDown("down")) this.up += 1F;

		this.right = 0;
		if (InputManager.isInputDown("right")) this.right += 1F;
		if (InputManager.isInputDown("left")) this.right -= 1F;

		this.sprint = InputManager.isInputDown("sprint");
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