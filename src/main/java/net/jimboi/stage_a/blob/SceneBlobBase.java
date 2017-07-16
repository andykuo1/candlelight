package net.jimboi.stage_a.blob;

import net.jimboi.stage_a.mod.instance.InstanceHandler;
import net.jimboi.stage_a.mod.renderer.OldRenderService;
import net.jimboi.stage_a.mod.scene.LivingSceneBase;

import org.bstone.living.Living;
import org.bstone.living.LivingManager;
import org.qsilver.render.RenderEngine;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class SceneBlobBase extends LivingSceneBase implements LivingManager.OnLivingAddListener, LivingManager.OnLivingRemoveListener
{
	protected final OldRenderService renderer;

	public SceneBlobBase(OldRenderService renderer)
	{
		this.renderer = renderer;

		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderManager)
	{
		renderManager.startService(this.renderer);
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderManager)
	{
		renderManager.stopService(this.renderer);
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
