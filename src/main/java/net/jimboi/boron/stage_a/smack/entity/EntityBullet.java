package net.jimboi.boron.stage_a.smack.entity;

import net.jimboi.boron.stage_a.base.collisionbox.collider.ActiveBoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.collider.BoxCollider;
import net.jimboi.boron.stage_a.base.collisionbox.response.CollisionResponse;
import net.jimboi.boron.stage_a.smack.DamageSource;
import net.jimboi.boron.stage_a.smack.SmackWorld;
import net.jimboi.boron.stage_a.smack.tile.DungeonHandler;

import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.qsilver.util.ColorUtil;

/**
 * Created by Andy on 8/5/17.
 */
public class EntityBullet extends EntityMotion implements ActiveBoxCollider
{
	private float speed = 0.2F;
	private int maxLife = 50;
	private int life;

	public EntityBullet(SmackWorld world, Transform3 transform, int color, float speed)
	{
		super(world, transform, 0.3F, world.createRenderable2D(transform, '!', ColorUtil.getColorWithBrightness(color, (world.getRandom().nextFloat() * 0.5f) + 0.5F)));

		this.getRenderable().getRenderModel().transformation().rotationZ(Transform.HALF_PI).translate(0.25F, 0, 0);

		this.speed = speed;
		this.life = this.maxLife;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		float rad = this.transform.eulerRadians().z();
		float dx = (float) Math.cos(rad);
		float dy = (float) Math.sin(rad);
		this.motion.set(dx, dy).mul(this.speed);

		if (this.life < this.maxLife / 6)
		{
			float scale = (this.life / ((float) this.maxLife / 6)) * 0.5F + 0.5F;
			this.transform.setScale(scale, scale, 1);
		}

		--this.life;
		if (this.life <= 0)
		{
			this.setDead();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		for(int i = 2; i > 0; --i)
		{
			this.fireSpark(0xFFFF00);
		}
	}

	@Override
	protected void onDamageTaken(DamageSource source, int damage)
	{
		super.onDamageTaken(source, damage);

		if (source != null)
		{
			if (source.getEntity() instanceof EntityZombie)
			{
				for (int i = this.world.getRandom().nextInt(4) + 3; i > 0; --i)
				{
					this.fireSpark(0xFF0000);
				}
			}
			else if (source.getEntity() instanceof EntityBoulder)
			{
				for (int i = this.world.getRandom().nextInt(4) + 3; i > 0; --i)
				{
					this.fireSpark(0xFFFF00);
				}
			}
			else if (source.getEntity() instanceof EntitySpawner)
			{
				for (int i = this.world.getRandom().nextInt(4) + 3; i > 0; --i)
				{
					this.fireSpark(0x00FF00);
				}
			}
		}
	}

	public void fireSpark(int color)
	{
		Transform3 transform = new Transform3();
		transform.position.set(this.transform.position3());
		transform.rotation.rotationZ(this.transform.eulerRadians().z() - Transform.PI + (Transform.HALF_PI / 2F) * (this.world.getRandom().nextFloat() - 0.5F));

		this.world.spawn(new EntitySpark(this.world, transform, color, this.speed * 0.6F));
	}

	@Override
	public void onPreCollisionUpdate()
	{

	}

	@Override
	public boolean onCollision(CollisionResponse collision)
	{
		BoxCollider collider = collision.getCollider();
		if (collider instanceof EntityBoulder)
		{
			this.damage(new DamageSource((EntityBoulder) collider), 1);
			this.transform.setPosition(collision.getPoint().x(), collision.getPoint().y(), 0);
			return true;
		}
		else if (collider instanceof DungeonHandler)
		{
			this.damage(new DamageSource(null), 1);
			this.transform.setPosition(collision.getPoint().x(), collision.getPoint().y(), 0);
			return true;
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
		return collider instanceof EntityBoulder || collider instanceof DungeonHandler;
	}
}
