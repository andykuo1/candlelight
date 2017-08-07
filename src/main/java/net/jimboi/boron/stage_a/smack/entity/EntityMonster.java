package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.MotionHelper;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxActiveCollider;
import net.jimboi.boron.stage_a.smack.aabb.BoundingBoxCollider;
import net.jimboi.boron.stage_a.smack.aabb.IntersectionData;

import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.joml.Vector2f;
import org.qsilver.util.ColorUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityMonster extends SmackEntity implements BoundingBoxActiveCollider
{
	protected float hurt;

	protected int color;

	protected Transform3c target;
	protected Vector2f velocity;
	protected float speed = 0.06F;

	public EntityMonster(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 1, world.createRenderable2D(transform, 'Z', 0x00FF00));

		this.velocity = new Vector2f();

		this.health = 2;

		this.color = ColorUtil.getColor(this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class).getColor());
	}

	@Override
	public void onUpdate()
	{
		if (this.hurt > 0)
		{
			this.hurt -= 0.2F;
			PropertyColor propertyColor = this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class);
			propertyColor.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt));
		}

		super.onUpdate();

		if (this.target == null)
		{
			if (this.world.getRandom().nextInt(10) != 0) return;

			Iterator<LivingEntity> livings = this.world.getSmacks().getLivingManager().getLivingIterator();
			while (livings.hasNext())
			{
				LivingEntity living = livings.next();
				if (living instanceof EntityPlayer)
				{
					if (MotionHelper.isWithinDistanceSquared(this.transform, living.getTransform().position3().x(), living.getTransform().position3().y(), 64))
					{
						this.target = living.getTransform();
						break;
					}
				}
			}
		}
		else
		{
			if (!MotionHelper.isWithinDistanceSquared(this.transform, this.target, 0.1F))
			{
				MotionHelper.getDirectionTowards(this.transform, this.target.position3().x(), this.target.position3().y(), this.velocity).mul(this.speed);
				this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
			}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for(int i = 4; i > 0; --i)
		{
			this.fireMeat();
		}
	}

	@Override
	public void onCheckIntersection(List<IntersectionData> intersections)
	{
		if (!intersections.isEmpty())
		{
			SmackEntity other = (SmackEntity) intersections.get(0).getCollider();

			if (other instanceof EntityBullet)
			{
				other.damage(new DamageSource(this), 1);

				this.damage(null, 1);

				Vector2f vec = MotionHelper.getDirectionTowards(this.transform, other.getTransform().position3().x(), other.getTransform().position3().y(), new Vector2f()).negate().mul(0.2F);
				this.transform.position.add(vec.x(), vec.y(), 0);
			}

			this.velocity.set(0);
			for(IntersectionData intersection : intersections)
			{
				if (intersection.getCollider() instanceof EntityBoulder)
				{
					this.velocity.sub(intersection.getDelta().x(), intersection.getDelta().y());
				}
			}
			this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
			this.velocity.set(0);
		}
	}

	@Override
	public boolean canCollideWith(BoundingBoxCollider collider)
	{
		return collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityBullet || collider instanceof EntityBoulder);
	}

	@Override
	protected void onDamageTaken(DamageSource source, int damage)
	{
		super.onDamageTaken(source, damage);

		this.hurt += damage;

		if (this.hurt > 0)
		{
			PropertyColor propertyColor = this.getRenderable().getRenderModel().getMaterial().getComponent(PropertyColor.class);
			propertyColor.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt));
		}
	}

	@Override
	public void damage(DamageSource source, int damage)
	{
		super.damage(source, damage);

		if (this.isDead())
		{
			if (this.world.getRandom().nextInt(4) == 0)
			{
				for(int i = this.world.getRandom().nextInt(3) + 1; i > 0; --i)
				{
					Transform3 transform = new Transform3();
					transform.position.set(this.transform.position3());
					this.world.spawn(new EntityAmmo(this.world, transform));
				}
			}
		}
	}

	public void fireMeat()
	{
		Transform3 transform = new Transform3();
		transform.position.set(this.transform.position3());

		this.world.spawn(new EntityMeat(this.world, transform, 0xFF0000));
	}
}
