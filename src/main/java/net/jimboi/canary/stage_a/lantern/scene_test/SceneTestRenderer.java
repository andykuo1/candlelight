package net.jimboi.canary.stage_a.lantern.scene_test;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.canary.stage_a.lantern.Lantern;

import org.bstone.asset.AssetManager;
import org.bstone.livingentity.LivingEntity;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.scene.SceneRenderer;
import org.bstone.transform.Transform2;
import org.joml.Matrix4f;
import org.qsilver.ResourceLocation;
import org.zilar.render.renderer.SimpleProgramRenderer;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneTestRenderer extends SceneRenderer
{
	public AssetManager assetManager;
	public SimpleProgramRenderer renderer;
	public Program program;
	public Mesh mesh;
	public Bitmap bitmap;
	public Texture texture;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		System.out.println("LOADED!");

		final AssetManager assets = Lantern.getLantern().getFramework().getAssetManager();

		assets.registerResourceLocation("texture.crate",
				new ResourceLocation("lantern:texture_wooden_crate.res"));
		assets.registerResourceLocation("bitmap.crate",
				new ResourceLocation("glim:texture/wooden_crate.jpg"));
		assets.registerResourceLocation("mesh.sphere",
				new ResourceLocation("glim:model/sphere.obj"));
		assets.registerResourceLocation("program.simple",
				new ResourceLocation("lantern:program_simple.res"));
		assets.registerResourceLocation("vertex_shader.simple",
				new ResourceLocation("base:simple.vsh"));
		assets.registerResourceLocation("fragment_shader.simple",
				new ResourceLocation("base:simple.fsh"));

		this.mesh = assets.<Mesh>getAsset("mesh", "sphere").get();
		this.bitmap = assets.<Bitmap>getAsset("bitmap", "crate").get();
		this.texture = assets.<Texture>getAsset("texture", "crate").get();
		this.program = assets.<Program>getAsset("program", "simple").get();

		this.renderer = new SimpleProgramRenderer(this.program);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		final SceneTest scene = (SceneTest) Lantern.getLantern().getSceneManager().getCurrentScene();
		this.renderer.bind(scene.getCamera().view(), scene.getCamera().projection());
		for(LivingEntity entity : scene.getLivings().getLivingEntities())
		{
			if (entity instanceof EntityBase)
			{
				EntityBase entityBase = (EntityBase) entity;
				this.renderer.draw(this.mesh, Asset.wrap(this.texture),
						Transform2.ZERO, Transform2.IDENTITY, false,
						null, entityBase.getTransform().getTransformation(new Matrix4f()).translate(2, 0, -5));
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
