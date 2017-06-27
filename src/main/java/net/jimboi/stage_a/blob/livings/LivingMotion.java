package net.jimboi.stage_a.blob.livings;

import org.joml.Vector3f;
import org.qsilver.util.MathUtil;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class LivingMotion extends Living3
{
	public final Vector3f motion = new Vector3f();
	public final Vector3f velocity = new Vector3f();
	public float friction = 0.88F;

	public final Vector3f maxVelocity = new Vector3f(1F, 1F, 1F);
	public float minVelocity = 0.01F;

	public LivingMotion(float x, float y, float z)
	{
		super(x, y, z);
	}

	protected static final Vector3f _vec = new Vector3f();

	@Override
	public void onUpdate(double delta)
	{
		Transform3 transform = this.transform();

		//Motion
		this.velocity.x += this.motion.x * delta;
		this.velocity.y += this.motion.y * delta;
		this.velocity.z += this.motion.z * delta;
		this.motion.set(0);

		this.velocity.x = MathUtil.clamp(this.velocity.x, -this.maxVelocity.x, this.maxVelocity.x);
		this.velocity.y = MathUtil.clamp(this.velocity.y, -this.maxVelocity.y, this.maxVelocity.y);
		this.velocity.z = MathUtil.clamp(this.velocity.z, -this.maxVelocity.z, this.maxVelocity.z);
		this.velocity.mul(this.friction);

		if (Math.abs(this.velocity.x) <= this.minVelocity) this.velocity.x = 0;
		if (Math.abs(this.velocity.y) <= this.minVelocity) this.velocity.y = 0;
		if (Math.abs(this.velocity.z) <= this.minVelocity) this.velocity.z = 0;

		if (this.velocity.z != 0) transform.translate(this.getForward(_vec), this.velocity.z);
		if (this.velocity.x != 0) transform.translate(this.getRight(_vec), this.velocity.x);
		if (this.velocity.y != 0) transform.translate(this.getUp(_vec), this.velocity.y);
	}

	public Vector3f getForward(Vector3f dst)
	{
		return Transform3.YAXIS.cross(this.getRight(dst), dst);
	}

	public Vector3f getRight(Vector3f dst)
	{
		return this.transform().getRight(dst);
	}

	public Vector3f getUp(Vector3f dst)
	{
		return dst.set(Transform3.YAXIS);
	}
}
