package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.boron.stage_a.base.livingentity.LivingEntity;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.MotionHelper;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.smack.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.smack.collisionbox.response.CollisionResponse;

import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.joml.Vector2f;
import org.qsilver.util.ColorUtil;

import java.util.Iterator;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityZombie extends EntityMotion implements ActiveBoxCollider
{
	protected float hurt;

	protected int color;

	protected Transform3c target;
	protected float speed = 0.06F;

	public EntityZombie(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 1, world.createRenderable2D(transform, 'Z', 0x00FF00));

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
				MotionHelper.getDirectionTowards(this.transform, this.target.position3().x(), this.target.position3().y(), this.motion).mul(this.speed);
				//this.transform.translate(this.velocity.x(), this.velocity.y(), 0);
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

	@Override
	public void onPreCollisionUpdate()
	{
		this.boundingBox.setCenter(this.transform.position3().x(), this.transform.position3().y());
	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		BoxCollider other = collision.getCollider();

		if (other instanceof EntityBullet)
		{
			EntityBullet bullet = (EntityBullet) other;
			bullet.damage(new DamageSource(this), 1);
			this.damage(null, 1);

			Vector2f vec = MotionHelper.getDirectionTowards(this.transform, bullet.getTransform().position3().x(), bullet.getTransform().position3().y(), new Vector2f()).negate().mul(0.2F);
			this.move(vec.x(), vec.y());
		}
		else if (other instanceof EntityBoulder)
		{
			this.move(collision.getDelta().x(), collision.getDelta().y());
		}

		return false;
	}

	@Override
	public void onPostCollisionUpdate()
	{
	}

	@Override
	public boolean canCollideWith(BoxCollider collider)
	{
		return collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityBullet || collider instanceof EntityBoulder);
	}
}