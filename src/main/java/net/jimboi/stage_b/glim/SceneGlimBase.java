package net.jimboi.stage_b.glim;

import org.qsilver.entity.EntityManager;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 6/1/17.
 */
public abstract class SceneGlimBase extends Scene
{
	protected RendererGlim renderer;

	protected EntityManager entityManager;

	public SceneGlimBase()
	{
	}

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderManager)
	{
		this.renderer = new RendererGlim();
		renderManager.add(this.renderer);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.entityManager.update();
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderManager)
	{
		renderManager.remove(this.renderer);
		this.renderer = null;
	}

	@Override
	protected void onSceneDestroy()
	{
		this.entityManager.clear();
	}

	public final EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public final RendererGlim getRenderer()
	{
		return this.renderer;
	}
}
