package net.jimboi.dood;

import net.jimboi.dood.component.ComponentBox2DBody;
import net.jimboi.dood.component.ComponentTransform;
import net.jimboi.mod.cameracontroller.CameraControllerBox2D;
import net.jimboi.mod2.transform.Transform3;

import org.bstone.util.MathUtil;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Created by Andy on 5/24/17.
 */
public class CameraControllerDood extends CameraControllerBox2D
{
	private float motionX;
	private float motionY;
	private float motionMax = 40F;
	private float motionDamping = 0.6F;

	public CameraControllerDood(ComponentTransform componentTransform, ComponentBox2DBody componentBox2DBody)
	{
		super((Transform3) componentTransform.transform, componentBox2DBody);
	}

	@Override
	protected void updateTargetPosition(double delta)
	{
		Body body = this.targetBody.getBody();

		this.motionX += this.right * this.speed;
		this.motionY += this.up * this.speed;

		this.motionX = MathUtil.clamp(this.motionX, -this.motionMax, this.motionMax);
		this.motionY = MathUtil.clamp(this.motionY, -this.motionMax, this.motionMax);

		this.motionX *= this.motionDamping;
		this.motionY *= this.motionDamping;

		if (body != null)
		{
			body.applyForceToCenter(new Vec2(this.motionX, this.motionY));
		}
	}
}
