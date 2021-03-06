package apricot.stage_a.dood.cameracontroller;

import apricot.base.input.OldInputManager;

import apricot.bstone.camera.Camera;
import apricot.bstone.camera.CameraController;
import apricot.bstone.transform.Transform3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerSideScroll extends CameraController
{
	protected float speed = 50F;
	protected float distToTarget = 16F;

	protected Camera camera;

	protected float maxForward = 15F;
	protected float dampForward = 0.9F;
	protected float speedForward = 1F;

	protected float forward;
	protected float up;
	protected float right;

	protected Transform3 target;

	public CameraControllerSideScroll(Transform3 target)
	{
		this.target = target;
	}

	public CameraControllerSideScroll setSpeed(float speed)
	{
		this.speed = speed;
		return this;
	}

	public CameraControllerSideScroll setDistanceToTarget(float dist)
	{
		this.distToTarget = dist;
		this.maxForward = this.distToTarget - 1F;
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
		if (OldInputManager.isInputDown("up")) this.forward -= this.speedForward;
		if (OldInputManager.isInputDown("down")) this.forward += this.speedForward;

		this.up = 0;
		if (OldInputManager.isInputDown("forward")) this.up += 1F;
		if (OldInputManager.isInputDown("backward")) this.up -= 1F;

		this.right = 0;
		if (OldInputManager.isInputDown("right")) this.right += 1F;
		if (OldInputManager.isInputDown("left")) this.right -= 1F;

		this.updateForward(delta);
		this.updateTargetPosition(delta);
		this.updateCameraPosition(cameraTransform, delta);

		return true;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		this.camera = null;
	}

	protected void updateForward(double delta)
	{
		if (this.forward > this.maxForward)
		{
			this.forward = this.maxForward;
		}
		if (this.forward < -this.maxForward)
		{
			this.forward = -this.maxForward;
		}
		this.forward *= this.dampForward;
	}

	protected void updateTargetPosition(double delta)
	{
		this.target.position.x += this.right * this.speed;
		this.target.position.y += this.up * this.speed;
		this.target.position.z = this.distToTarget + this.forward;
	}

	protected void updateCameraPosition(Transform3 cameraTransform, double delta)
	{
		Vector3fc pos = this.target.position3();
		cameraTransform.position.set(pos.x(), pos.y(), this.distToTarget + this.forward);
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public float getDistanceToTarget()
	{
		return this.distToTarget;
	}
}
