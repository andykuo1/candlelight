package net.jimboi.stage_b.glim.controller;

import net.jimboi.stage_b.glim.bounding.Bounding;
import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.bounding.IntersectionData;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.qsilver.transform.Transform;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 6/4/17.
 */
public class FirstPersonMoveController
{
	protected Vector3f velocity = new Vector3f();
	protected float maxSpeed = 0.4F;
	protected float acceleration = 0.4F;
	protected float friction = 0.1F;

	protected float forward;
	protected float up;
	protected float right;
	protected boolean sprint;

	protected float counter;

	protected final Transform3Quat target;
	protected final Bounding bounding;
	protected final BoundingManager boundingManager;

	public FirstPersonMoveController(Transform3Quat target, Bounding bounding, BoundingManager boundingManager)
	{
		this.target = target;
		this.bounding = bounding;
		this.boundingManager = boundingManager;
	}

	public void poll(InputEngine inputEngine)
	{
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

	private static final Vector3f _VEC = new Vector3f();

	public void update(Camera camera, Transform3 cameraTransform, double delta)
	{
		Vector3f vec = new Vector3f();
		Vector3f right = cameraTransform.getRight(new Vector3f());
		Vector3f forward = Transform3.YAXIS.cross(right, new Vector3f());

		if (this.forward != 0 || this.right != 0)
		{
			forward.normalize().mul(this.forward);
			right.normalize().mul(this.right);
			vec.add(forward).add(right).normalize().mul(this.getSpeed());

			this.velocity.lerp(vec, (float) delta * this.acceleration);
		}
		this.velocity.lerp(new Vector3f(), this.friction);
		vec.set(this.velocity);

		float dist = vec.length();
		if (dist != 0)
		{
			boolean solid = cameraTransform.position.y >= 0 && cameraTransform.position.y < 1;

			IntersectionData data = solid ? this.boundingManager.checkIntersection(this.bounding, vec) : null;

			this.target.translate(vec.normalize(), dist);

			if (data != null)
			{
				this.target.translate(data.delta.x(), data.delta.y(), data.delta.z());
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
		Vector3fc pos = this.target.position();
		cameraTransform.setPosition(pos.x(), pos.y(), pos.z());
		cameraTransform.moveUp((float) Math.abs(Math.cos(this.counter)) * wiggleY - wiggleY / 2F);
		cameraTransform.moveRight((float) Math.cos(this.counter) * wiggleX);
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
