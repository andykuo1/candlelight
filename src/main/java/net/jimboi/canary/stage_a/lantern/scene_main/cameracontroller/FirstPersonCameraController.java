package net.jimboi.canary.stage_a.lantern.scene_main.cameracontroller;

import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputContext;
import org.bstone.input.InputListener;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 11/3/17.
 */
public class FirstPersonCameraController extends CameraController implements InputListener
{
	public static final String INPUT_LOOK_X = "mousex";
	public static final String INPUT_LOOK_Y = "mousey";
	public static final String INPUT_LOOK_LOCK = "mouselock";
	public static final String INPUT_FORWARD = "forward";
	public static final String INPUT_UP = "up";
	public static final String INPUT_RIGHT = "right";
	public static final String INPUT_SPRINT = "sprint";

	public static final float MAX_PITCH = Transform.DEG2RAD * 80F;

	protected float sensitivity = 0.01F;

	protected float pitch;
	protected float yaw;

	protected Vector3f velocity = new Vector3f();
	protected float maxSpeed = 0.1F;
	protected float acceleration = 0.1F;
	protected float friction = 0.1F;

	protected Vector3f position = new Vector3f();
	protected float forward;
	protected float up;
	protected float right;
	protected boolean sprint;

	protected float counter;

	public FirstPersonCameraController(PerspectiveCamera camera)
	{
		super(camera);

		this.position.set(camera.transform().position3());
	}

	private static final Vector3f _VEC = new Vector3f();
	private static final Vector3f _RIGHT = new Vector3f();
	private static final Vector3f _FORWARD = new Vector3f();

	@Override
	protected boolean onUpdate(Camera camera, Transform3 cameraTransform)
	{
		//Look
		cameraTransform.setYaw(this.yaw);
		Vector3f right = cameraTransform.getRight(_RIGHT);
		cameraTransform.rotate(this.pitch, right);

		//Movement
		Vector3f forward = Transform3.YAXIS.cross(right, _FORWARD);

		if (this.forward != 0 || this.right != 0)
		{
			forward.normalize().mul(this.forward);
			right.normalize().mul(this.right);
			_VEC.set(0).add(forward).add(right).normalize().mul(this.getMoveSpeed());

			this.velocity.lerp(_VEC, this.acceleration);
		}

		this.velocity.lerp(new Vector3f(), this.friction);
		_VEC.set(this.velocity);

		float dist = _VEC.length();
		if (dist != 0)
		{
			this.position.add(_VEC.normalize().mul(dist));
		}

		if (this.up != 0)
		{
			this.position.add(Transform3.YAXIS.mul(this.up * this.getMoveSpeed() * 1.2F, _VEC));
		}

		if (this.velocity.lengthSquared() != 0)
		{
			this.counter += this.velocity.length() * 5;
			if (this.counter > Transform.PI2) this.counter = 0;
		}

		float wiggleX = 0.03F;
		float wiggleY = 0.03F;
		cameraTransform.position.set(this.position);
		cameraTransform.moveUp((float) Math.abs(Math.cos(this.counter)) * wiggleY - wiggleY / 2F);
		cameraTransform.moveRight((float) Math.cos(this.counter) * wiggleX);

		return true;
	}

	@Override
	public void onInputUpdate(InputContext context)
	{
		//Look
		boolean mouseLocked = context.getInputEngine().getMouse().getCursorMode();

		if (mouseLocked)
		{
			//Update camera rotation
			Vector2fc mouse = new Vector2f(
					context.getRange(INPUT_LOOK_X).getMotion(),
					context.getRange(INPUT_LOOK_Y).getMotion()
			);

			float rotx = mouse.x() * this.sensitivity;
			float roty = mouse.y() * this.sensitivity;

			this.yaw += rotx;
			this.pitch += roty;
			this.pitch = MathUtil.clamp(this.pitch, -MAX_PITCH, MAX_PITCH);
		}

		if (context.getAction(INPUT_LOOK_LOCK).isPressedAndConsume())
		{
			context.getInputEngine().getMouse().setCursorMode(!mouseLocked);
		}

		//Move
		this.forward = context.getRange(INPUT_FORWARD).getRangeAndConsume();
		this.up = context.getRange(INPUT_UP).getRangeAndConsume();
		this.right = context.getRange(INPUT_RIGHT).getRangeAndConsume();

		this.sprint = context.getState(INPUT_SPRINT).isDown();
	}

	public void updateTransform(Transform3 transform)
	{
		this.getCamera().transform().getRotation(transform.rotation);
		transform.rotation.x = 0;
		transform.rotation.z = 0;
		transform.rotation.normalize().invert();

		transform.position.set(this.position);
	}

	public void setLookSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}

	public float getMoveSpeed()
	{
		return this.sprint ? this.maxSpeed * 2 : this.maxSpeed;
	}

	public void setMoveSpeed(float speed)
	{
		this.maxSpeed = speed;
	}
}
