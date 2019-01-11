package boron.bstone.livingentity;

import boron.bstone.entity.Component;
import boron.bstone.entity.EntityManager;
import boron.bstone.living.LivingManager;

/**
 * Created by Andy on 8/12/17.
 */
public class LivingEntityManager implements
		EntityManager.OnEntityCreateListener<LivingEntity>,
		EntityManager.OnEntityDestroyListener<LivingEntity>,
		LivingManager.OnLivingCreateListener<LivingEntity>,
		LivingManager.OnLivingDestroyListener<LivingEntity>,
		EntityManager.OnEntityComponentAddListener<LivingEntity>,
		EntityManager.OnEntityComponentRemoveListener<LivingEntity>
{
	protected final EntityManager<LivingEntity> entityManager;
	protected final LivingManager<LivingEntity> livingManager;

	public LivingEntityManager()
	{
		this.entityManager = new EntityManager<>();
		this.livingManager = new LivingManager<>();

		this.entityManager.onEntityCreate.addListener(this);
		this.entityManager.onEntityDestroy.addListener(this);
		this.entityManager.onEntityComponentAdd.addListener(this);
		this.entityManager.onEntityComponentRemove.addListener(this);
		this.livingManager.onLivingCreate.addListener(this);
		this.livingManager.onLivingDestroy.addListener(this);
	}

	public void update()
	{
		this.livingManager.update();
	}

	public void clear()
	{
		this.livingManager.onLivingCreate.deleteListener(this);
		this.livingManager.onLivingDestroy.deleteListener(this);
		this.entityManager.onEntityComponentAdd.deleteListener(this);
		this.entityManager.onEntityComponentRemove.deleteListener(this);
		this.entityManager.onEntityCreate.deleteListener(this);
		this.entityManager.onEntityDestroy.deleteListener(this);
		this.livingManager.clear();
		this.entityManager.clear();
	}

	public final <T extends LivingEntity> T addLivingEntity(T livingEntity)
	{
		this.entityManager.addEntity(livingEntity);
		return livingEntity;
	}

	@Override
	public void onEntityCreate(LivingEntity entity)
	{
		if (entity.getLivingManager() == null)
		{
			this.livingManager.addLiving(entity);
		}
	}

	@Override
	public void onEntityDestroy(LivingEntity entity)
	{
		if (entity.getLivingManager() != null)
		{
			entity.setDead();
		}
	}

	@Override
	public void onEntityComponentAdd(LivingEntity entity, Component component)
	{

	}

	@Override
	public void onEntityComponentRemove(LivingEntity entity, Component component)
	{

	}

	@Override
	public void onLivingCreate(LivingEntity living)
	{
		if (living.getEntityManager() == null)
		{
			this.entityManager.addEntity(living);
		}
	}

	@Override
	public void onLivingDestroy(LivingEntity living)
	{
		if (living.getEntityManager() != null)
		{
			this.entityManager.removeEntity(living);
		}
	}

	public final Iterable<LivingEntity> getLivingEntities()
	{
		return this.livingManager.getLivings();
	}

	public final EntityManager<LivingEntity> getEntityManager()
	{
		return this.entityManager;
	}

	public final LivingManager<LivingEntity> getLivingManager()
	{
		return this.livingManager;
	}
}
