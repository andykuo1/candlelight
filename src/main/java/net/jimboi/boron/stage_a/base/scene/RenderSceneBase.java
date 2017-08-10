package net.jimboi.boron.stage_a.base.scene;

import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.qsilver.scene.Scene;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class RenderSceneBase extends RenderService
{
	Scene scene;

	public RenderSceneBase(RenderEngine renderEngine)
	{
		super(renderEngine);
	}

	public Scene getScene()
	{
		return this.scene;
	}
}
