package org.zilar;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.joml.Vector3fc;
import org.qsilver.transform.Transform;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 6/18/17.
 */
public class BasicTopDownCameraController implements CameraController, InputEngine.OnInputUpdateListener
{
	protected float speed = 0.1F;

	protected float dist;
	protected float minDistToTarget;
	protected float distToTarget;
	protected float maxDistToTarget;
	protected float dampDistToTarget = 0.8F;

	protected float forward;
	protected float up;
	protected float right;
	protected boolean sprint;

	protected Transform3Quat target;

	public BasicTopDownCameraController()
	{
		this.setDistanceToTarget(1.5F);
	}

	public BasicTopDownCameraController setTarget(Transform3Quat target)
	{
		this.target = target;
		return this;
	}

	public BasicTopDownCameraController setSpeed(float speed)
	{
		this.speed = speed;
		return this;
	}

	public BasicTopDownCameraController setDistanceToTarget(float dist)
	{
		this.distToTarget = dist;
		this.minDistToTarget = -(this.distToTarget - (dist / 12F));
		this.maxDistToTarget = this.distToTarget * 8F;
		return this;
	}

	public BasicTopDownCameraController setMaxDistanceToTarget(float maxDist)
	{
		this.maxDistToTarget = maxDist;
		return this;
	}

	public BasicTopDownCameraController setMinDistanceToTarget(float minDist)
	{
		this.minDistToTarget = -(this.distToTarget - minDist);
		return this;
	}

	@Override
	public void onCameraStart(Camera camera)
	{
		InputManager.getInputEngine().onInputUpdate.addListener(this);
	}

	@Override
	public void onCameraUpdate(Camera camera, double delta)
	{
		if (this.target == null) return;

		Transform3 cameraTransform = camera.getTransform();
		cameraTransform.setPitch(Transform.HALF_PI);

		this.dist += this.up / 3F;
		if (this.dist > this.maxDistToTarget) this.dist = this.maxDistToTarget;
		if (this.dist < this.minDistToTarget) this.dist = this.minDistToTarget;
		this.dist *= this.dampDistToTarget;

		float speed = this.sprint ? 2 * this.speed : this.speed;
		this.target.position.x += this.right * speed;
		this.target.position.y = 0;
		this.target.position.z += this.forward * speed;

		Vector3fc pos = this.target.position();
		cameraTransform.setPosition(pos.x(), this.distToTarget + this.dist, pos.z());
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		InputManager.getInputEngine().onInputUpdate.deleteListener(this);
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.target == null) return;

		this.forward = 0;
		if (InputManager.isInputDown("forward")) this.forward -= 1F;
		if (InputManager.isInputDown("backward")) this.forward += 1F;

		this.right = 0;
		if (InputManager.isInputDown("right")) this.right += 1F;
		if (InputManager.isInputDown("left")) this.right -= 1F;

		this.up = 0;
		if (InputManager.isInputDown("up")) this.up -= 1F;
		if (InputManager.isInputDown("down")) this.up += 1F;

		this.sprint = InputManager.isInputDown("sprint");
	}

	public float getSpeed()
	{
		return this.speed;
	}

	public float getDistanceToTarget()
	{
		return this.dist;
	}
}
