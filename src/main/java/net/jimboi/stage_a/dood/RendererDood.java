package net.jimboi.stage_a.dood;

import net.jimboi.stage_a.base.Main;
import net.jimboi.stage_a.mod.instance.Instance;
import net.jimboi.stage_a.mod.instance.InstanceHandler;
import net.jimboi.stage_a.mod.instance.InstanceManager;
import net.jimboi.stage_a.mod.render.Render;
import net.jimboi.stage_a.mod.renderer.OldRenderService;

import org.bstone.camera.PerspectiveCamera;
import org.qsilver.renderer.RenderEngine;

/**
 * Created by Andy on 5/30/17.
 */
public class RendererDood extends OldRenderService implements InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	protected final net.jimboi.stage_a.mod.render.RenderManager renderManager;
	protected final InstanceManager instanceManager;

	public RendererDood()
	{
		this.renderManager = new net.jimboi.stage_a.mod.render.RenderManager();

		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getRenderType());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);

		ResourcesDood.INSTANCE.camera = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		ResourcesDood.INSTANCE.load(this.renderManager);
	}

	@Override
	public void onRender(RenderEngine renderEngine)
	{
		//This is a hack to stop crashing, when using OldRenderServices...
		if (!Main.SCENEMANAGER.isSetupMode())
		{
			this.instanceManager.update();
		}
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.instanceManager.destroyAll();
	}

	@Override
	public void onInstanceAdd(InstanceHandler parent, Instance instance)
	{

	}

	@Override
	public void onInstanceRemove(InstanceHandler parent, Instance instance)
	{

	}

	public InstanceManager getInstanceManager()
	{
		return this.instanceManager;
	}

	public net.jimboi.stage_a.mod.render.RenderManager getRenderManager()
	{
		return this.renderManager;
	}
}
