package net.jimboi.glim.controller;

import net.jimboi.glim.bounding.Bounding;
import net.jimboi.glim.bounding.BoundingManager;
import net.jimboi.glim.bounding.IntersectionData;
import net.jimboi.mod.transform.Transform3;
import net.jimboi.mod.transform.Transform3Q;

import org.bstone.camera.Camera;
import org.bstone.input.InputEngine;
import org.bstone.input.InputManager;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 6/4/17.
 */
public class FirstPersonMoveController
{
	protected float speed = 1F;

	protected float forward;
	protected float up;
	protected float right;

	protected final Transform3Q target;
	protected final Bounding bounding;
	protected final BoundingManager boundingManager;

	public FirstPersonMoveController(Transform3Q target, Bounding bounding, BoundingManager boundingManager)
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
	}

	private static final Vector3f _VEC = new Vector3f();

	public void update(Camera camera, double delta)
	{
		float magnitude = (float) delta * this.speed;

		Transform3 cameraTransform = camera.getTransform();
		Vector3f vec = new Vector3f();
		Vector3f right = cameraTransform.getRight(new Vector3f());
		Vector3f forward = Transform3.YAXIS.cross(right, new Vector3f());

		if (this.forward != 0 || this.right != 0)
		{
			forward.normalize().mul(magnitude * this.forward);
			right.normalize().mul(magnitude * this.right);
			vec.add(forward).add(right);

			boolean solid = cameraTransform.position.y >= 0 && cameraTransform.position.y < 1;

			IntersectionData data = solid ? this.boundingManager.checkIntersection(this.bounding, vec) : null;

			float dist = vec.length();
			if (dist != 0)
			{
				this.target.translate(vec.normalize(), dist);
			}

			if (data != null)
			{
				this.target.translate(data.delta.x(), data.delta.y(), data.delta.z());
			}
		}

		if (this.up != 0)
		{
			this.target.translate(Transform3.YAXIS, magnitude * this.up);
		}

		Vector3fc pos = this.target.position();
		camera.getTransform().setPosition(pos.x(), pos.y(), pos.z());
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
}
