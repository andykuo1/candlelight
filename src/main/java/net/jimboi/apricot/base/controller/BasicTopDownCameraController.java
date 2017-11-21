package net.jimboi.apricot.base.controller;

import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.boron.base_ab.window.input.InputEngine;
import net.jimboi.boron.base_ab.window.input.InputLayer;

import org.bstone.camera.Camera;
import org.bstone.camera.CameraController;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.joml.Vector3fc;

/**
 * Created by Andy on 6/18/17.
 */
public class BasicTopDownCameraController extends CameraController implements InputLayer
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

	public BasicTopDownCameraController()
	{
		this.setDistanceToTarget(1.5F);
	}

	@Override
	public BasicTopDownCameraController setTarget(Transform3 target)
	{
		return (BasicTopDownCameraController) super.setTarget(target);
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
		OldInputManager.getInputEngine().addInputLayer(this);
	}

	@Override
	public boolean onCameraUpdate(Camera camera, Transform3 cameraTransform, double delta)
	{
		if (this.target == null) return false;

		cameraTransform.setPitch(Transform.HALF_PI);

		this.dist += this.up / 3F;
		if (this.dist > this.maxDistToTarget) this.dist = this.maxDistToTarget;
		if (this.dist < this.minDistToTarget) this.dist = this.minDistToTarget;
		this.dist *= this.dampDistToTarget;

		float speed = this.sprint ? 2 * this.speed : this.speed;
		this.target.position.x += this.right * speed;
		this.target.position.y = 0;
		this.target.position.z += this.forward * speed;

		Vector3fc pos = this.target.position3();
		cameraTransform.position.set(pos.x(), this.distToTarget + this.dist, pos.z());

		return true;
	}

	@Override
	public void onCameraStop(Camera camera)
	{
		OldInputManager.getInputEngine().removeInputLayer(this);
	}

	@Override
	public void onInputUpdate(InputEngine inputEngine)
	{
		if (this.target == null) return;

		this.forward = 0;
		if (OldInputManager.isInputDown("forward")) this.forward -= 1F;
		if (OldInputManager.isInputDown("backward")) this.forward += 1F;

		this.right = 0;
		if (OldInputManager.isInputDown("right")) this.right += 1F;
		if (OldInputManager.isInputDown("left")) this.right -= 1F;

		this.up = 0;
		if (OldInputManager.isInputDown("up")) this.up -= 1F;
		if (OldInputManager.isInputDown("down")) this.up += 1F;

		this.sprint = OldInputManager.isInputDown("sprint");
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
