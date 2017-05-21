package net.jimboi.mod3;

import net.jimboi.mod.entity.Component;
import net.jimboi.mod.entity.ComponentHandler;
import net.jimboi.mod.entity.Entity;
import net.jimboi.mod.transform.Transform3;

import org.bstone.util.MathUtil;
import org.joml.Vector3f;

/**
 * Created by Andy on 5/19/17.
 */
public class ComponentMotion extends Component
{
	public final Vector3f motion = new Vector3f();
	public final Vector3f velocity = new Vector3f();
	public float friction = 0.88F;

	public final Vector3f maxVelocity = new Vector3f(1F, 1F, 1F);
	public float minVelocity = 0.01F;

	public ComponentMotion(Entity3D entity)
	{
		super(entity);
	}

	@Override
	public void onComponentSetup(ComponentHandler componentHandler)
	{
		super.onComponentSetup(componentHandler);
	}

	@Override
	public boolean onEntityCreate(Entity entity)
	{
		return super.onEntityCreate(entity);
	}

	protected static final Vector3f _vec = new Vector3f();

	@Override
	public void onEntityUpdate(double delta)
	{
		super.onEntityUpdate(delta);

		Transform3 transform = this.getEntity().transform();

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

	@Override
	public void onEntityDestroy()
	{
		super.onEntityDestroy();
	}

	@Override
	public Entity3D getEntity()
	{
		return (Entity3D) super.getEntity();
	}

	public Vector3f getForward(Vector3f dst)
	{
		return Transform3.YAXIS.cross(this.getRight(dst), dst);
	}

	public Vector3f getRight(Vector3f dst)
	{
		return this.entity.transform().getRight(dst);
	}

	public Vector3f getUp(Vector3f dst)
	{
		return dst.set(Transform3.YAXIS);
	}
}
