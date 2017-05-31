package net.jimboi.dood;

import net.jimboi.mod.renderer.Renderer;

import org.bstone.camera.PerspectiveCamera;
import org.qsilver.render.Instance;
import org.qsilver.render.InstanceHandler;
import org.qsilver.render.InstanceManager;
import org.qsilver.render.Render;
import org.qsilver.render.RenderManager;

/**
 * Created by Andy on 5/30/17.
 */
public class RendererDood extends Renderer implements InstanceManager.OnInstanceAddListener, InstanceManager.OnInstanceRemoveListener
{
	protected final RenderManager renderManager;
	protected final InstanceManager instanceManager;

	public RendererDood()
	{
		this.renderManager = new RenderManager();

		this.instanceManager = new InstanceManager((inst) ->
		{
			Render render = this.renderManager.get(inst.getMaterial().getRenderID());
			if (render != null) render.onRender(inst);
			return null;
		});
		this.instanceManager.onInstanceAdd.addListener(this);
		this.instanceManager.onInstanceRemove.addListener(this);

		ResourcesDood.INSTANCE.camera = new PerspectiveCamera(640, 480);
	}

	@Override
	public void onRenderLoad()
	{
		ResourcesDood.INSTANCE.load(this.renderManager);
	}

	@Override
	public void onRenderUpdate()
	{
		this.instanceManager.update();
	}

	@Override
	public void onRenderUnload()
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

	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}
}
