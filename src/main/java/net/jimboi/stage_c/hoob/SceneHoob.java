package net.jimboi.stage_c.hoob;

import org.qsilver.entity.EntityManager;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 6/25/17.
 */
public class SceneHoob extends Scene
{
	protected EntityManager livingManager;
	protected RendererHoob renderer;

	@Override
	protected void onSceneCreate()
	{
		this.livingManager = new EntityManager();
	}

	@Override
	protected void onSceneLoad(RenderEngine renderManager)
	{
		renderManager.add(this.renderer = new RendererHoob());
	}

	@Override
	protected void onSceneStart()
	{

	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.livingManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.livingManager.clear();
	}

	@Override
	protected void onSceneUnload(RenderEngine renderManager)
	{
		renderManager.remove(this.renderer);
	}

	@Override
	protected void onSceneDestroy()
	{

	}

	public final EntityManager getLivingManager()
	{
		return this.livingManager;
	}

	public final RendererHoob getRenderer()
	{
		return this.renderer;
	}
}
