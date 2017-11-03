package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.MotionHelper;
import net.jimboi.boron.stage_a.smack.SmackEntity;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.tile.DungeonHandler;

import org.bstone.livingentity.LivingEntity;
import org.bstone.transform.Transform3;
import org.bstone.transform.Transform3c;
import org.bstone.util.ColorUtil;
import org.joml.Vector2f;
import org.zilar.render.material.PropertyColor;

/**
 * Created by Andy on 8/6/17.
 */
public class EntityZombie extends EntityMotion
{
	protected float hurt;

	protected int color;

	protected Transform3c target;
	protected float speed = 0.06F;

	public EntityZombie(SmackWorld world, Transform3 transform)
	{
		super(world, transform, 1, world.createRenderable2D(transform, 'Z', 0x00FF00));

		this.health = 2;

		this.color = PropertyColor.PROPERTY.getColor(this.getRenderable().getRenderModel().getMaterial());
	}

	@Override
	public void onLivingUpdate()
	{
		if (this.hurt > 0)
		{
			this.hurt -= 0.2F;
			PropertyColor.PROPERTY.bind(this.getRenderable().getRenderModel().getMaterial())
					.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt))
					.unbind();
		}

		super.onLivingUpdate();

		if (this.target == null)
		{
			if (this.world.getRandom().nextInt(10) != 0) return;

			for(LivingEntity living : this.world.getSmacks().getLivingEntities())
			{
				if (living instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) living;
					if (MotionHelper.isWithinDistanceSquared(this.transform, entityPlayer.getTransform().position3().x(), entityPlayer.getTransform().position3().y(), 32))
					{
						this.target = entityPlayer.getTransform();
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
	public void onLivingDestroy()
	{
		super.onLivingDestroy();

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
			PropertyColor.PROPERTY.bind(this.getRenderable().getRenderModel().getMaterial())
					.setColor(ColorUtil.getColorMix(this.color, 0xFF0000, this.hurt))
					.unbind();
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
		else if (other instanceof EntityBoulder || other instanceof DungeonHandler)
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
		return (collider instanceof SmackEntity && !((SmackEntity) collider).isDead() && (collider instanceof EntityBullet || collider instanceof EntityBoulder)) || collider instanceof DungeonHandler;
	}

	@Override
	public boolean isColliderActive()
	{
		return true;
	}
}
