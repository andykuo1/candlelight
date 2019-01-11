package canary.lantern.scene_main;

import canary.base.MaterialProperty;
import canary.base.TextureAtlasBuilder;
import canary.base.collisionbox.CollisionBoxRenderer;
import canary.base.renderer.SimpleRenderer;
import canary.lantern.Lantern;
import canary.lantern.scene_main.component.ComponentRenderable;

import canary.bstone.asset.AssetManager;
import canary.bstone.camera.Camera;
import canary.bstone.material.Material;
import canary.bstone.render.RenderEngine;
import canary.bstone.scene.SceneRenderer;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import canary.bstone.transform.Transform;
import canary.bstone.transform.Transform3;
import org.joml.Matrix4f;
import canary.qsilver.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andy on 11/2/17.
 */
public class SceneMainRenderer extends SceneRenderer
{
	private CollisionBoxRenderer collisionRenderer;
	private SimpleRenderer simpleRenderer;
	private boolean renderCollision = false;

	private Material material;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		final AssetManager assets = Lantern.getLantern().getAssetManager();

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

		assets.registerResourceLocation("mesh.cube",
				new ResourceLocation("lantern:cube.obj"));
		assets.registerResourceLocation("mesh.quad",
				new ResourceLocation("lantern:quad.obj"));

		assets.registerResourceLocation("bitmap.crate",
				new ResourceLocation("base:wooden_crate.jpg"));
		assets.registerResourceLocation("texture.crate",
				new ResourceLocation("lantern:texture_wooden_crate.res"));

		assets.registerResourceLocation("bitmap.font",
				new ResourceLocation("base:font.png"));

		assets.registerResourceLocation("texture.font",
				new ResourceLocation("lantern:texture_font.res"));

		{
			TextureAtlasBuilder tab = new TextureAtlasBuilder(assets.getAsset("texture", "font"), 256, 256);
			tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
			TextureAtlas textureAtlas = tab.bake();
			assets.cacheResource("texture_atlas", "font", textureAtlas);

			this.material = new Material();
			this.material.setProperty(MaterialProperty.TEXTURE, assets.getAsset("texture", "font"));
		}

		this.simpleRenderer = new SimpleRenderer(
				assets.getAsset("program", "simple"));

		this.collisionRenderer = new CollisionBoxRenderer(
				assets.getAsset("program", "wireframe"),
				assets.getAsset("mesh", "quad"));
	}

	@Override
	protected void onRenderUnload(RenderEngine renderEngine)
	{

	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine)
	{
		final AssetManager assets = Lantern.getLantern().getAssetManager();
		final SceneMain scene = (SceneMain) Lantern.getLantern().getSceneManager().getCurrentScene();
		final Camera camera = scene.getCamera();
		final Matrix4f mat = new Matrix4f();

		this.simpleRenderer.bind(camera.view(), camera.projection());
		{
			Collection<ComponentRenderable> renderables = scene.getEntityManager().getEntityManager().getComponents(ComponentRenderable.class, new ArrayList<>());
			for(ComponentRenderable renderable : renderables)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh(),
						renderable.getRenderModel().material(),
						renderable.getRenderTransformation(mat));
			}

			this.simpleRenderer.draw(assets.getAsset("mesh", "dungeon"), this.material, new Matrix4f());
		}
		this.simpleRenderer.unbind();

		if (this.renderCollision)
		{
			this.collisionRenderer.bind(camera.view().rotate(Transform.HALF_PI, Transform3.XAXIS, mat),
					camera.projection());
			{
				this.collisionRenderer.draw(scene.getColliders(), 0x00FF00);
			}
			this.collisionRenderer.unbind();
		}

	}
}
