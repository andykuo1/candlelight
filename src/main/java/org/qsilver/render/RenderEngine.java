package org.qsilver.render;

import org.bstone.service.ServiceManager;
import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 5/30/17.
 */
public class RenderEngine extends ServiceManager<RenderEngine>
{
	public interface OnRenderUpdateListener
	{
		void onRenderUpdate(RenderEngine renderEngine);
	}

	public final Listenable<OnRenderUpdateListener> onRenderUpdate = new Listenable<>((listener, objects) -> listener.onRenderUpdate((RenderEngine) objects[0]));

	public RenderEngine()
	{
	}

	public void start()
	{
	}

	public void stop()
	{
		this.clearServices();
	}

	public void update()
	{
		this.beginServiceBlock();

		this.onRenderUpdate.notifyListeners(this);

		this.endServiceBlock();
	}

	@Override
	protected final RenderEngine getServiceHandler()
	{
		return this;
	}
}
