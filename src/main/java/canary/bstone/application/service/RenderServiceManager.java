package canary.bstone.application.service;

import canary.bstone.render.RenderEngine;
import canary.bstone.render.RenderFramework;
import canary.bstone.service.ServiceManager;

/**
 * Created by Andy on 1/17/18.
 */
public class RenderServiceManager extends ServiceManager<RenderEngine, RenderService> implements RenderFramework
{
	@Override
	public void load()
	{
		this.beginServices();
		this.endServices();
	}

	@Override
	public void unload()
	{
		this.beginServices();
		this.endServices();
	}

	@Override
	public void render()
	{
		this.beginServices();
		this.forEach(renderService -> renderService.onRenderUpdate(this.getHandler()));
		this.endServices();
	}
}
