package net.jimboi.apricot.stage_a.blob;

import net.jimboi.apricot.base.living.OldLiving;
import net.jimboi.apricot.base.living.OldLivingManager;
import net.jimboi.apricot.stage_a.mod.instance.InstanceHandler;
import net.jimboi.apricot.stage_a.mod.renderer.OldRenderService;
import net.jimboi.apricot.stage_a.mod.scene.LivingSceneBase;

import org.bstone.render.RenderEngine;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class SceneBlobBase extends LivingSceneBase implements OldLivingManager.OnLivingCreateListener, OldLivingManager.OnLivingDestroyListener
{
	protected final OldRenderService renderer;

	public SceneBlobBase(OldRenderService renderer)
	{
		this.renderer = renderer;

		this.livingManager.onLivingCreate.addListener(this);
		this.livingManager.onLivingDestroy.addListener(this);
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderEngine)
	{
		this.renderer.start();
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderEngine)
	{
		this.renderer.stop();
	}

	@Override
	public void onLivingCreate(OldLiving living)
	{
		if (living instanceof InstanceHandler)
		{
			((RendererBlob) this.renderer).getInstanceManager().add((InstanceHandler) living);
		}
	}

	@Override
	public void onLivingDestroy(OldLiving living)
	{
	}
}
