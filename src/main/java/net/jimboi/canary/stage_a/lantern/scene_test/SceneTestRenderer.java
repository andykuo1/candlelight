package net.jimboi.canary.stage_a.lantern.scene_test;

import net.jimboi.canary.stage_a.base.MaterialProperty;
import net.jimboi.canary.stage_a.base.renderer.SimpleRenderer;
import net.jimboi.canary.stage_a.lantern.Lantern;

import org.bstone.asset.Asset;
import org.bstone.asset.AssetManager;
import org.bstone.gameobject.GameObject;
import org.bstone.material.Material;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.scene.SceneRenderer;
import org.bstone.transform.Transform2;
import org.joml.Matrix4f;
import org.qsilver.ResourceLocation;

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
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
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
