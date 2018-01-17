package org.bstone.render;

import org.bstone.service.ServiceManager;

/**
 * Created by Andy on 1/17/18.
 */
public class RenderServiceManager extends ServiceManager<RenderEngine, RenderService> implements Renderable
{
	public RenderServiceManager(RenderEngine handler)
	{
		super(handler);
	}

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
