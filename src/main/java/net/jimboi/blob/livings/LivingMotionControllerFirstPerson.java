package net.jimboi.blob.livings;

import net.jimboi.base.Main;
import net.jimboi.mod.Renderer;
import net.jimboi.mod.transform.Transform;
import net.jimboi.mod.transform.Transform3;
import net.jimboi.mod.transform.Transform3Q;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.bstone.util.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class LivingMotionControllerFirstPerson extends LivingMotion implements InputEngine.OnInputUpdateListener
{
	public static final float MAX_PITCH = Transform.ANG2RAD * 80F;

	protected final Camera camera;

	private float pitch;
	private float yaw;

	private float sensitivity = 0.01F;
	private boolean mouseLock = false;

	private float forward;
	private float up;
	private float right;

	public LivingMotionControllerFirstPerson(float x, float y, float z, Camera camera)
	{
		super(x, y, z);

		this.camera = camera;
	}

	@Override
	public boolean onCreate()
	{
		Main.INPUTENGINE.onInputUpdate.addListener(this);

		return super.onCreate();
	}

	@Override
	public void onUpdate(double delta)
	{
		this.motion.x += this.right;
		this.motion.y += this.up;
		this.motion.z += this.forward;

		super.onUpdate(delta);

		Vector3fc pos = this.transform().position();
		this.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.mouseLock)
		{
			Transform3 cameraTransform = this.camera.getTransform();

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
			Vector3f right = cameraTransform.getRight(new Vector3f());
			cameraTransform.rotate(this.pitch, right);

			Transform3Q transform = (Transform3Q) this.transform();
			transform.setRotation(Renderer.camera.getTransform().rotation);
			transform.rotation.x = 0;
			transform.rotation.z = 0;
			transform.rotation.normalize().invert();
		}

		if (InputManager.isInputPressed("mouseleft"))
		{
			inputEngine.getMouse().setCursorMode(this.mouseLock = !this.mouseLock);
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
	public void onDestroy()
	{
		super.onDestroy();

		Main.INPUTENGINE.onInputUpdate.deleteListener(this);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.camera.getTransform().getRight(dst);
	}

	public boolean isMouseLocked()
	{
		return this.mouseLock;
	}
}
