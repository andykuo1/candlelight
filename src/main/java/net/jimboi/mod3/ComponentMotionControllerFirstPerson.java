package net.jimboi.mod3;

import net.jimboi.base.Main;
import net.jimboi.mod.entity.ComponentHandler;
import net.jimboi.mod.entity.Entity;
import net.jimboi.mod.transform.Transform;
import net.jimboi.mod.transform.Transform3;
import net.jimboi.mod.transform.Transform3Q;
import net.jimboi.mod3.blob.Renderer;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.bstone.util.MathUtil;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/19/17.
 */
public class ComponentMotionControllerFirstPerson extends ComponentMotion implements InputEngine.Listener
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

	public ComponentMotionControllerFirstPerson(Entity3D entity, Camera camera)
	{
		super(entity);

		this.camera = camera;
	}

	@Override
	public void onComponentSetup(ComponentHandler componentHandler)
	{
		super.onComponentSetup(componentHandler);
	}

	@Override
	public boolean onEntityCreate(Entity entity)
	{
		Main.INPUTENGINE.addListener(this);

		return super.onEntityCreate(entity);
	}

	@Override
	public void onEntityUpdate(double delta)
	{
		this.motion.x += this.right;
		this.motion.y += this.up;
		this.motion.z += this.forward;

		super.onEntityUpdate(delta);

		Vector3fc pos = this.entity.transform().position();
		this.camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.mouseLock)
		{
			Transform3 cameraTransform = (Transform3) this.camera.getTransform();

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

			Transform3Q transform = (Transform3Q) this.getEntity().transform();
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
	public void onEntityDestroy()
	{
		super.onEntityDestroy();

		Main.INPUTENGINE.removeListener(this);
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