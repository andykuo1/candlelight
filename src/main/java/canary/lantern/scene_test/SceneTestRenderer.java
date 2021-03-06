package canary.lantern.scene_test;

import canary.base.MaterialProperty;
import canary.base.renderer.SimpleRenderer;
import canary.lantern.Lantern;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.gameobject.GameObject;
import canary.bstone.material.Material;
import canary.bstone.mogli.Bitmap;
import canary.bstone.mogli.Mesh;
import canary.bstone.mogli.Program;
import canary.bstone.mogli.Texture;
import canary.bstone.render.RenderEngine;
import canary.bstone.scene.SceneRenderer;
import canary.bstone.transform.Transform2;
import org.joml.Matrix4f;
import canary.qsilver.ResourceLocation;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneTestRenderer extends SceneRenderer
{
	public AssetManager assetManager;
	public SimpleRenderer renderer;
	public Asset<Program> program;
	public Asset<Mesh> mesh;
	public Asset<Bitmap> bitmap;
	public Asset<Texture> texture;
	public Material material;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		System.out.println("LOADED!");

		final AssetManager assets = Lantern.getLantern().getAssetManager();

		assets.registerResourceLocation("texture.crate",
				new ResourceLocation("lantern:texture_wooden_crate.res"));
		assets.registerResourceLocation("bitmap.crate",
				new ResourceLocation("base:wooden_crate.jpg"));
		assets.registerResourceLocation("mesh.sphere",
				new ResourceLocation("base:sphere.obj"));
		assets.registerResourceLocation("program.simple",
				new ResourceLocation("lantern:program_simple.res"));
		assets.registerResourceLocation("vertex_shader.simple",
				new ResourceLocation("base:simple.vsh"));
		assets.registerResourceLocation("fragment_shader.simple",
				new ResourceLocation("base:simple.fsh"));

		this.mesh = assets.getAsset("mesh", "sphere");
		this.bitmap = assets.getAsset("bitmap", "crate");
		this.texture = assets.getAsset("texture", "crate");
		this.program = assets.getAsset("program", "simple");

		this.renderer = new SimpleRenderer(this.program);
		this.material = new Material();
		this.material.setProperty(MaterialProperty.TEXTURE, this.texture);
		this.material.setProperty(MaterialProperty.SPRITE_OFFSET, Transform2.ZERO);
		this.material.setProperty(MaterialProperty.SPRITE_SCALE, Transform2.IDENTITY);
		this.material.setProperty(MaterialProperty.TRANSPARENCY, false);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine)
	{
		final SceneTest scene = (SceneTest) Lantern.getLantern().getSceneManager().getCurrentScene();
		this.renderer.bind(scene.getCamera().view(), scene.getCamera().projection());
		for(GameObject entity : scene.getLivings().getGameObjects())
		{
			if (entity instanceof EntityBase)
			{
				EntityBase entityBase = (EntityBase) entity;
				this.renderer.draw(this.mesh, this.material, entityBase.getTransform().getTransformation(new Matrix4f()).translate(2, 0, -5));
			}
		}
		this.renderer.unbind();
	}

	@Override
	protected void onRenderUnload(RenderEngine renderEngine)
	{
		System.out.println("UNLOADED!");
	}
}
