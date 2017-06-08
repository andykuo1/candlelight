package net.jimboi.mod.cameracontroller;

import net.jimboi.mod.Box2DHandler;
import net.jimboi.mod2.transform.Transform3;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerBox2D extends CameraControllerSideScroll
{
	protected Box2DHandler targetBody;

	public CameraControllerBox2D(Transform3 target, Box2DHandler body)
	{
		super(target);
		this.targetBody = body;
	}

	@Override
	protected void updateTargetPosition(double delta)
	{
		Body body = this.targetBody.getBody();
		if (body != null)
		{
			body.applyForceToCenter(new Vec2(this.right * this.speed, this.up * this.speed));
		}
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
