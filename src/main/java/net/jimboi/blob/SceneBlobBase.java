package net.jimboi.blob;

import net.jimboi.base.Main;
import net.jimboi.mod.renderer.Renderer;
import net.jimboi.mod.scene.LivingSceneBase;

import org.qsilver.living.Living;
import org.qsilver.living.LivingManager;
import org.qsilver.render.InstanceHandler;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class SceneBlobBase extends LivingSceneBase implements LivingManager.OnLivingAddListener, LivingManager.OnLivingRemoveListener
{
	protected final Renderer renderer;

	public SceneBlobBase(Renderer renderer)
	{
		this.renderer = renderer;

		Main.RENDERENGINE.add(this.renderer);

		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);
	}

	@Override
	protected final void onSceneLoad()
	{
		//this.renderer.onRenderLoad();
	}

	@Override
	public final void onSceneRender()
	{
		//this.renderer.onRenderUpdate();
	}

	@Override
	public final void onSceneUnload()
	{
		//this.renderer.onRenderUnload();
	}

	@Override
	public void onLivingAdd(Living living)
	{
		if (living instanceof InstanceHandler)
		{
			((RendererBlob) this.renderer).getInstanceManager().add((InstanceHandler) living);
		}
	}

	@Override
	public void onLivingRemove(Living living)
	{
	}
}
