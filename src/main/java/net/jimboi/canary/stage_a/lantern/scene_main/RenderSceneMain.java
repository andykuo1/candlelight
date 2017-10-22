package net.jimboi.canary.stage_a.lantern.scene_main;

import org.bstone.application.scene.render.RenderScene;
import org.bstone.livingentity.LivingEntity;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.transform.Transform2;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.loader.OBJLoader;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 10/20/17.
 */
public class RenderSceneMain extends RenderScene
{
	public AssetManager assetManager;
	public SimpleProgramRenderer renderer;
	public Mesh mesh;
	public Bitmap bitmap;
	public Texture texture;

	public RenderSceneMain(RenderEngine renderEngine, LanternSceneMain scene)
	{
		super(renderEngine, scene);
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		this.renderer = new SimpleProgramRenderer();
		this.mesh = OBJLoader.read(new ResourceLocation("glim:model/sphere.obj"));
		this.bitmap = new Bitmap(new ResourceLocation("glim:texture/wooden_crate.jpg"));
		this.texture = new Texture(this.bitmap, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		final LanternSceneMain scene = (LanternSceneMain) this.scene;
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
	protected void onServiceStop(RenderEngine handler)
	{
		this.texture.close();
		this.bitmap.close();
		this.mesh.close();
		this.renderer.close();
	}
}
