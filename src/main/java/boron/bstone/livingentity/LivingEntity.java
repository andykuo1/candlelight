package boron.bstone.livingentity;

import boron.bstone.entity.EntityManager;
import boron.bstone.entity.IEntity;
import boron.bstone.living.ILiving;
import boron.bstone.living.LivingManager;

/**
 * Created by Andy on 8/12/17.
 */
public abstract class LivingEntity implements ILiving, IEntity
{
	private EntityManager<LivingEntity> entityManager;
	private LivingManager<LivingEntity> livingManager;
	private int entityID = -1;
	private int livingID = -1;
	private boolean dead;

	@Override
	public abstract void onEntityCreate(EntityManager entityManager);

	@Override
	public abstract void onLivingCreate(LivingManager livingManager);

	@Override
	public abstract void onLivingUpdate();

	@Override
	public abstract void onLivingLateUpdate();

	@Override
	public abstract void onLivingDestroy();

	@Override
	public abstract void onEntityDestroy();

	@Override
	public final void setDead()
	{
		this.dead = true;
	}

	@Override
	public final boolean isDead()
	{
		return this.dead;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void setEntityManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	@Override
	public final void setEntityID(int id)
	{
		this.entityID = id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void setLivingManager(LivingManager livingManager)
	{
		this.livingManager = livingManager;
	}

	@Override
	public final void setLivingID(int id)
	{
		this.livingID = id;
	}

	@Override
	public final EntityManager<LivingEntity> getEntityManager()
	{
		return this.entityManager;
	}

	@Override
	public final int getEntityID()
	{
		return this.entityID;
	}


	@Override
	public final LivingManager<LivingEntity> getLivingManager()
	{
		return this.livingManager;
	}

	@Override
	public final int getLivingID()
	{
		return this.livingID;
	}


}
