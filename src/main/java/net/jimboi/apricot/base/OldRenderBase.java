package net.jimboi.apricot.base;

import net.jimboi.apricot.stage_a.mod.renderer.OldRenderService;
import net.jimboi.boron.base_ab.render.OldRenderEngine;

import org.bstone.camera.Camera;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class OldRenderBase extends OldRenderService
{
	protected OldSceneBase scene;

	private final Camera camera;

	public OldRenderBase(OldRenderEngine renderEngine, Camera camera)
	{
		super(renderEngine);
		this.camera = camera;
	}

	@Override
	public void onRenderLoad(OldRenderEngine renderEngine)
	{
	}

	@Override
	public void onRender(OldRenderEngine renderEngine)
	{
	}

	@Override
	public void onRenderUnload(OldRenderEngine renderEngine)
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
