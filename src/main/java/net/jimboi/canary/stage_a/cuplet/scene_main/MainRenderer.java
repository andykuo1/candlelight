package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.canary.stage_a.cuplet.Cuplet;
import net.jimboi.canary.stage_a.cuplet.TextureAtlasBuilder;
import net.jimboi.canary.stage_a.cuplet.basicobject.ComponentRenderable;
import net.jimboi.canary.stage_a.cuplet.collisionbox.CollisionBoxRenderer;
import net.jimboi.canary.stage_a.cuplet.renderer.SimpleRenderer;

import org.bstone.asset.AssetManager;
import org.bstone.camera.Camera;
import org.bstone.camera.OrthographicCamera;
import org.bstone.mogli.Mesh;
import org.bstone.render.RenderEngine;
import org.bstone.scene.SceneRenderer;
import org.bstone.sprite.textureatlas.TextureAtlas;
import org.bstone.util.direction.Direction;
import org.bstone.window.Window;
import org.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.resource.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 10/29/17.
 */
public class MainRenderer extends SceneRenderer
{
	private Camera camera;
	private ScreenSpace screenSpace;

	public FontSheet fontSheet;

	public SimpleRenderer simpleRenderer;
	public CollisionBoxRenderer collisionBoxRenderer;

	public Set<RenderableBase> renderables;
	public Set<ComponentRenderable> renderComponents;

	private boolean first = false;

	@Override
	protected void onRenderLoad(RenderEngine renderEngine)
	{
		System.out.println("LOADING");

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.renderables = new HashSet<>();
		this.renderComponents = new HashSet<>();

		final Window window = Cuplet.getCuplet().getFramework().getWindow();
		final AssetManager assets = Cuplet.getCuplet().getFramework().getAssetManager();

		//Camera
		this.camera = new OrthographicCamera(window.getWidth(), window.getHeight());
		this.screenSpace = new ScreenSpace(window.getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		//Assets
		assets.registerResourceLocation("bitmap.font", new ResourceLocation("base:font.png"));
		assets.registerResourceLocation("texture.font", new ResourceLocation("cuplet:texture_font.res"));
		assets.registerResourceLocation("texture_atlas.font", new ResourceLocation("cuplet:texture_atlas_font.res"));

		assets.registerResourceLocation("program.simple", new ResourceLocation("cuplet:program_simple.res"));
		assets.registerResourceLocation("vertex_shader.simple", new ResourceLocation("base:simple.vsh"));
		assets.registerResourceLocation("fragment_shader.simple", new ResourceLocation("base:simple.fsh"));

		assets.registerResourceLocation("program.wireframe", new ResourceLocation("cuplet:program_wireframe.res"));
		assets.registerResourceLocation("vertex_shader.wireframe", new ResourceLocation("base:wireframe.vsh"));
		assets.registerResourceLocation("fragment_shader.wireframe", new ResourceLocation("base:wireframe.fsh"));

		//Models
		//???
		//Materials
		//???

		//Mesh
		{
			MeshBuilder mb = new MeshBuilder();
			mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
			Mesh mesh = ModelUtil.createStaticMesh(mb.bake(false, true));
			mb.clear();

			assets.cacheResource("mesh", "quad", mesh);
		}

		//Texture Atlas
		{
			TextureAtlasBuilder tab = new TextureAtlasBuilder(assets.getAsset("texture", "font"), 256, 256);
			tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
			TextureAtlas atlas = tab.bake();
			tab.clear();

			assets.cacheResource("texture_atlas", "font", atlas);
		}

		this.fontSheet = new FontSheet(assets.getAsset("texture_atlas", "font"), 0, (char) 0, (char) 255);

		//Renderer
		this.simpleRenderer = new SimpleRenderer(
				assets.getAsset("program", "simple"));

		this.collisionBoxRenderer = new CollisionBoxRenderer(
				assets.getAsset("program", "wireframe"),
				assets.getAsset("mesh", "quad"));
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{

		this.simpleRenderer.bind(this.camera.view(), this.camera.projection());
		{
			Matrix4f matrix = new Matrix4f();
			Vector4f vec4 = new Vector4f();

			for (RenderableBase renderable : this.renderables)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh(),
						renderable.getRenderModel().material(),
						renderable.getRenderTransformation(matrix));
			}

			this.renderComponents.clear();

			MainScene scene = (MainScene) Cuplet.getCuplet().getSceneManager().getCurrentScene();
			scene.getWorld().getEntityManager().getEntityManager().getComponents(ComponentRenderable.class, this.renderComponents);

			for (ComponentRenderable renderable : this.renderComponents)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh(),
						renderable.getRenderModel().material(),
						renderable.getRenderTransformation(matrix));
			}
		}
		this.simpleRenderer.unbind();

		if (Cuplet.isDebugMode())
		{
			this.collisionBoxRenderer.bind(this.camera.view(), this.camera.projection());
			{
				MainScene scene = (MainScene) Cuplet.getCuplet().getSceneManager().getCurrentScene();
				this.collisionBoxRenderer.draw(scene.getWorld().getBoundingManager().getColliders(), 0x00FF00);
			}
			this.collisionBoxRenderer.unbind();
		}
	}

	@Override
	protected void onRenderUnload(RenderEngine renderEngine)
	{

	}

	public Camera getCamera()
	{
		return this.camera;
	}

	public ScreenSpace getScreenSpace()
	{
		return this.screenSpace;
	}
}
