package net.jimboi.apricot.stage_a.blob.livings;

import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.stage_a.base.OldMain;
import net.jimboi.apricot.stage_a.blob.RendererBlob;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.bstone.window.camera.Camera;
import org.bstone.window.input.InputEngine;
import org.bstone.window.input.InputLayer;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.util.MathUtil;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class LivingMotionControllerFirstPerson extends LivingMotion implements InputLayer
{
	public static final float MAX_PITCH = Transform.DEG2RAD * 80F;

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
		OldMain.INPUTENGINE.addInputLayer(this);

		return super.onCreate();
	}

	@Override
	public void onUpdate()
	{
		this.motion.x += this.right;
		this.motion.y += this.up;
		this.motion.z += this.forward;

		super.onUpdate();

		Vector3fc pos = this.transform().position3();
		//This is the wrong way... use camera controller instead!
		((Transform3) this.camera.transform()).position.set(pos.x(), pos.y(), pos.z());
		this.camera.markDirty();
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.mouseLock)
		{
			Transform3 cameraTransform = (Transform3) this.camera.transform();
			this.camera.markDirty();

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
			Vector3f right = cameraTransform.getRight(new Vector3f());
			cameraTransform.rotate(this.pitch, right);

			Transform3 transform = this.transform();
			transform.rotation.set(RendererBlob.camera.transform().quaternion());
			transform.rotation.x = 0;
			transform.rotation.z = 0;
			transform.rotation.normalize().invert();
		}

		if (OldInputManager.isInputPressed("mouseleft"))
		{
			inputEngine.getMouse().setCursorMode(this.mouseLock = !this.mouseLock);
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
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		OldMain.INPUTENGINE.removeInputLayer(this);
	}

	@Override
	public Vector3f getRight(Vector3f dst)
	{
		return this.camera.transform().getRight(dst);
	}

	public boolean isMouseLocked()
	{
		return this.mouseLock;
	}
}
