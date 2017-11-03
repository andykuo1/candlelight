package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.canary.stage_a.cuplet.collisionbox.CollisionBoxManager;
import net.jimboi.canary.stage_a.cuplet.collisionbox.CollisionBoxRenderer;
import net.jimboi.canary.stage_a.lantern.Lantern;

import org.bstone.asset.AssetManager;
import org.bstone.camera.Camera;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.render.RenderEngine;
import org.bstone.scene.SceneRenderer;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 11/2/17.
 */
public class SceneMainRenderer extends SceneRenderer
{
	private Camera camera;
	private CollisionBoxRenderer collisionRenderer;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		final AssetManager assets = Lantern.getLantern().getFramework().getAssetManager();

		assets.registerResourceLocation("program.simple",
				new ResourceLocation("lantern:program_simple.res"));
		assets.registerResourceLocation("vertex_shader.simple",
				new ResourceLocation("base:simple.vsh"));
		assets.registerResourceLocation("fragment_shader.simple",
				new ResourceLocation("base:simple.fsh"));

		assets.registerResourceLocation("program.wireframe",
				new ResourceLocation("lantern:program_wireframe.res"));
		assets.registerResourceLocation("vertex_shader.wireframe",
				new ResourceLocation("base:wireframe.vsh"));
		assets.registerResourceLocation("fragment_shader.wireframe",
				new ResourceLocation("base:wireframe.fsh"));

		assets.registerResourceLocation("mesh.quad",
				new ResourceLocation("lantern:quad.obj"));

		this.collisionRenderer = new CollisionBoxRenderer(
				assets.getAsset("program", "wireframe"),
				assets.getAsset("mesh", "quad"));

		this.camera = new PerspectiveCamera(0, 0, 10, 640, 480);
	}

	@Override
	protected void onRenderUnload(RenderEngine renderEngine)
	{

	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		CollisionBoxManager collisionManager = ((SceneMain) Lantern.getLantern().getSceneManager().getCurrentScene()).getCollisionManager();

		this.collisionRenderer.bind(this.camera.view(), this.camera.projection());
		{
			this.collisionRenderer.draw(collisionManager.getColliders(), 0x00FF00);
		}
		this.collisionRenderer.unbind();
	}
}
