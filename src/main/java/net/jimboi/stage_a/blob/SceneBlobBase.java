package net.jimboi.stage_a.blob;

import net.jimboi.base.Main;
import net.jimboi.stage_a.mod.instance.InstanceHandler;
import net.jimboi.stage_a.mod.scene.LivingSceneBase;

import org.qsilver.living.Living;
import org.qsilver.living.LivingManager;
import org.qsilver.renderer.Renderer;

/**
 * Created by Andy on 4/30/17.
 */
public abstract class SceneBlobBase extends LivingSceneBase implements LivingManager.OnLivingAddListener, LivingManager.OnLivingRemoveListener
{
	protected final Renderer renderer;

	public SceneBlobBase(Renderer renderer)
	{
		this.renderer = renderer;

		this.livingManager.onLivingAdd.addListener(this);
		this.livingManager.onLivingRemove.addListener(this);
	}

	@Override
	protected final void onSceneLoad()
	{
		Main.RENDERENGINE.add(this.renderer);
	}

	@Override
	protected final void onSceneUnload()
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
