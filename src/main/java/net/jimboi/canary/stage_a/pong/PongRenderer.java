package net.jimboi.canary.stage_a.pong;

import net.jimboi.canary.stage_a.base.Model;
import net.jimboi.canary.stage_a.base.ModelManager;
import net.jimboi.canary.stage_a.base.renderer.SimpleRenderer;

import org.bstone.application.game.GameAssets;
import org.bstone.asset.AssetManager;
import org.bstone.entity.Entity;
import org.bstone.material.Material;
import org.bstone.render.RenderEngine;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneRenderer;
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
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
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
