package org.bstone.scene.render;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.scene.Scene;

/**
 * Created by Andy on 10/20/17.
 */
public class RenderScene extends RenderService
{
	protected final Scene scene;

	public RenderScene(RenderEngine renderEngine, Scene scene)
	{
		//TODO: This should not take a renderEngine, it is redundant!
		super(renderEngine);
		this.scene = scene;
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{

	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{

	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{

	}

	public final Scene getScene()
	{
		return this.scene;
	}
}
