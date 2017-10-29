package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.asset.AssetManager;
import org.bstone.livingentity.LivingEntity;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.resource.BitmapLoader;
import org.bstone.resource.MeshLoader;
import org.bstone.resource.ProgramLoader;
import org.bstone.resource.ShaderLoader;
import org.bstone.resource.TextureLoader;
import org.bstone.scene.render.RenderScene;
import org.bstone.transform.Transform2;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.zilar.resource.ResourceLocation;

/**
 * Created by Andy on 10/20/17.
 */
public class RenderSceneMain extends RenderScene
{
	public AssetManager assetManager;
	public SimpleProgramRenderer renderer;
	public Program program;
	public Mesh mesh;
	public Bitmap bitmap;
	public Texture texture;

	public RenderSceneMain(RenderEngine renderEngine, LanternSceneMain scene)
	{
		super(renderEngine, scene);
		this.assetManager = new AssetManager(new ResourceLocation("lantern:lantern.assets"));
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		this.assetManager.registerLoader("bitmap", new BitmapLoader());
		this.assetManager.registerLoader("texture", new TextureLoader(this.assetManager));
		this.assetManager.registerLoader("mesh", new MeshLoader());
		this.assetManager.registerLoader("program", new ProgramLoader(this.assetManager));
		this.assetManager.registerLoader("vertex_shader", new ShaderLoader(GL20.GL_VERTEX_SHADER));
		this.assetManager.registerLoader("fragment_shader", new ShaderLoader(GL20.GL_FRAGMENT_SHADER));

		this.mesh = this.assetManager.<Mesh>getAsset("mesh", "sphere").get();
		this.bitmap = this.assetManager.<Bitmap>getAsset("bitmap", "crate").get();
		this.texture = this.assetManager.<Texture>getAsset("texture", "crate").get();
		this.program = this.assetManager.<Program>getAsset("program", "simple").get();

		this.renderer = new SimpleProgramRenderer(this.program);
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

		this.assetManager.update();
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		this.assetManager.destroy();
	}
}
