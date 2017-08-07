package net.jimboi.apricot.base;

import net.jimboi.apricot.stage_a.mod.renderer.OldRenderService;

import org.bstone.render.RenderEngine;
import org.bstone.window.camera.Camera;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class OldRenderBase extends OldRenderService
{
	protected OldSceneBase scene;

	private final Camera camera;

	public OldRenderBase(RenderEngine renderEngine, Camera camera)
	{
		super(renderEngine);
		this.camera = camera;
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
	}

	@Override
	public void onRender(RenderEngine renderEngine)
	{
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
	}

	protected void setScene(OldSceneBase scene)
	{
		this.scene = scene;
	}

	public OldSceneBase getScene()
	{
		return this.scene;
	}

	public Camera getCamera()
	{
		return this.camera;
	}
}
