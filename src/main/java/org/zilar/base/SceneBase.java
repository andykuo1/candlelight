package org.zilar.base;

import org.bstone.material.MaterialManager;
import org.qsilver.entity.EntityManager;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.scene.Scene;
import org.zilar.animation.AnimationManager;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class SceneBase extends Scene
{
	private final RenderBase renderer;

	protected EntityManager entityManager;
	protected MaterialManager materialManager;
	protected AnimationManager animationManager;

	public SceneBase(RenderBase renderer)
	{
		this.renderer = renderer;
		this.renderer.setScene(this);
	}

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
		this.materialManager = new MaterialManager();
		this.animationManager = new AnimationManager();
	}

	@Override
	protected void onSceneLoad(RenderEngine renderManager)
	{
		renderManager.add(this.renderer);
	}

	@Override
	protected abstract void onSceneStart();

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.renderer.getCamera().update(delta);
		this.animationManager.update(delta);
		this.entityManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	@Override
	protected void onSceneUnload(RenderEngine renderManager)
	{
		renderManager.remove(this.renderer);
	}

	@Override
	protected void onSceneDestroy()
	{
		this.materialManager.clear();
	}

	public RenderBase getRenderer()
	{
		return this.renderer;
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
