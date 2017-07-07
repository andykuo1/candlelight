package net.jimboi.stage_a.blob;

import net.jimboi.stage_a.base.Main;
import net.jimboi.stage_a.mod.instance.InstanceHandler;
import net.jimboi.stage_a.mod.scene.LivingSceneBase;

import org.qsilver.living.Living;
import org.qsilver.living.LivingManager;
import org.qsilver.renderer.RenderEngine;
import org.qsilver.renderer.RenderManager;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class SceneBlobBase extends LivingSceneBase implements LivingManager.OnLivingAddListener, LivingManager.OnLivingRemoveListener
{
	protected final RenderManager renderer;

	public SceneBlobBase(RenderManager renderer)
	{
		this.renderer = renderer;

		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);
	}

	@Override
	protected final void onSceneLoad(RenderEngine renderManager)
	{
		Main.RENDERENGINE.add(this.renderer);
	}

	@Override
	protected final void onSceneUnload(RenderEngine renderManager)
	{
		Main.RENDERENGINE.remove(this.renderer);
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
