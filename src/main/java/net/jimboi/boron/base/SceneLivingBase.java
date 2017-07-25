package net.jimboi.boron.base;

import net.jimboi.boron.base.livingentity.EntityComponentTransform;
import net.jimboi.boron.base.livingentity.LivingEntity;

import org.bstone.living.LivingManager;
import org.zilar.entity.Entity;

/**
 * Created by Andy on 7/21/17.
 */
public abstract class SceneLivingBase extends SceneBase implements LivingManager.OnLivingAddListener<LivingEntity>, LivingManager.OnLivingRemoveListener<LivingEntity>
{
	protected LivingManager<LivingEntity> livingManager;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.livingManager = new LivingManager<>();
		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		super.onSceneUpdate(delta);

		this.livingManager.update(delta);
	}

	@Override
	protected void onSceneStop()
	{
		this.livingManager.destroy();

		super.onSceneStop();
	}

	public LivingEntity spawn(LivingEntity living)
	{
		this.livingManager.add(living);
		return living;
	}

	protected void onLivingEntityCreate(LivingEntity living, Entity entity)
	{
		entity.addComponent(new EntityComponentTransform(living.getTransform()));
		if (living.getRenderable() != null)
		{
			entity.addComponent(living.getRenderable());
		}
	}

	protected void onLivingEntityDestroy(LivingEntity living, Entity entity)
	{

	}

	@Override
	public final void onLivingAdd(LivingEntity living)
	{
		Entity entity = this.getEntityManager().createEntity(living);
		this.onLivingEntityCreate(living, entity);
		living.onEntityCreate(entity);
	}

	@Override
	public final void onLivingRemove(LivingEntity living)
	{
		Entity entity = this.getEntityManager().getEntityByComponent(living);
		entity.setDead();
		this.onLivingEntityDestroy(living, entity);
		living.onEntityDestroy(entity);
	}

	public LivingManager<LivingEntity> getLivingManager()
	{
		return this.livingManager;
	}
}