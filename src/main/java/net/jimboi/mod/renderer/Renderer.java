package net.jimboi.mod.renderer;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 5/30/17.
 */
public abstract class Renderer
{
	public interface OnRenderUpdateListener
	{
		void onRenderUpdate();
	}

	public final Listenable<OnRenderUpdateListener> onRenderUpdate = new Listenable<>((listener, objects) -> listener.onRenderUpdate());

	protected abstract void onRenderLoad();

	protected abstract void onRenderUpdate();

	protected abstract void onRenderUnload();
}
