package net.jimboi.boron.stage_a.base.scene;

import net.jimboi.apricot.base.material.MaterialManager;

import org.zilar.animation.AnimationManager;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class SceneEntityBase extends SceneBase
{
	protected EntityManager entityManager;
	protected MaterialManager materialManager;
	protected AnimationManager animationManager;

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
		this.materialManager = new MaterialManager();
		this.animationManager = new AnimationManager();
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.animationManager.update(delta);
		this.entityManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.materialManager.clear();
	}

	public AnimationManager getAnimationManager()
	{
		return this.animationManager;
	}

	public MaterialManager getMaterialManager()
	{
		return this.materialManager;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
