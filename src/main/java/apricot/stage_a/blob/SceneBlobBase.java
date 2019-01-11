package apricot.stage_a.blob;

import apricot.base.living.OldLiving;
import apricot.base.living.OldLivingManager;
import apricot.stage_a.mod.instance.InstanceHandler;
import apricot.stage_a.mod.renderer.OldRenderService;
import apricot.stage_a.mod.scene.LivingSceneBase;
import apricot.base.render.OldRenderEngine;

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
	protected final void onSceneLoad(OldRenderEngine renderEngine)
	{
		this.renderer.start();
	}

	@Override
	protected final void onSceneUnload(OldRenderEngine renderEngine)
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
