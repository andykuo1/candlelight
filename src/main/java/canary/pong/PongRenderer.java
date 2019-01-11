package canary.pong;

import canary.base.Model;
import canary.base.ModelManager;
import canary.base.renderer.SimpleRenderer;
import canary.pong.component.ComponentRenderable;
import canary.pong.component.ComponentTransform;

import canary.bstone.application.game.GameAssets;
import canary.bstone.asset.AssetManager;
import canary.bstone.entity.Entity;
import canary.bstone.material.Material;
import canary.bstone.render.RenderEngine;
import canary.bstone.scene.Scene;
import canary.bstone.scene.SceneRenderer;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Andy on 12/1/17.
 */
public class PongRenderer extends SceneRenderer
{
	protected ModelManager modelManager = new ModelManager();
	protected SimpleRenderer renderer;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		final AssetManager assets = Pong.ENGINE.getAssetManager();

		GameAssets.loadBaseAssets(assets);

		this.modelManager.registerModel("ball", new Model(
				assets.getAsset("mesh", "sphere"),
				new Material()));

		this.modelManager.registerModel("paddle", new Model(
				assets.getAsset("mesh", "quad"),
				new Material()));

		this.renderer = new SimpleRenderer(assets.getAsset("program", "simple"));
	}

	@Override
	protected void onRenderUnload(RenderEngine renderEngine)
	{

	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine)
	{
		Scene scene = Pong.ENGINE.getSceneManager().getCurrentScene();
		Pong pong = (Pong) scene;

		this.renderer.bind(pong.getCamera().view(), pong.getCamera().projection());
		{
			Collection<Entity> entities = pong.getEntityManager().getEntitiesWith(new HashSet<>(), ComponentRenderable.class);
			final Matrix4f transform = new Matrix4f();
			for (Entity entity : entities)
			{
				ComponentRenderable componentRenderable = entity.getComponent(ComponentRenderable.class);
				Model model = componentRenderable.customModel;
				if (model == null)
				{
					model = this.modelManager.getModel(componentRenderable.modelName);
				}

				ComponentTransform componentTransform = entity.getComponent(ComponentTransform.class);
				if (componentTransform != null)
				{
					componentTransform.transform.getTransformation(transform);
				}
				transform.mul(model.transformation());

				this.renderer.draw(model.getMesh(), model.material(), transform);
			}
		}
		this.renderer.unbind();
	}

	public ModelManager getModelManager()
	{
		return this.modelManager;
	}
}
