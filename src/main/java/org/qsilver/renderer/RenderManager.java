package org.qsilver.renderer;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class RenderManager
{
	public interface OnRenderUpdateListener
	{
		void onRenderUpdate();
	}

	public final Listenable<OnRenderUpdateListener> onRenderUpdate = new Listenable<>((listener, objects) -> listener.onRenderUpdate());

	public abstract void onRenderLoad();

	public abstract void onRenderUpdate();

	public abstract void onRenderUnload();
}
