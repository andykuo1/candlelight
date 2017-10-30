package net.jimboi.canary.stage_a.cuplet.scene_main;

import net.jimboi.apricot.base.gui.base.Gui;
import net.jimboi.apricot.base.gui.base.GuiManager;
import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.stage_a.base.basicobject.ComponentRenderable;
import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxRenderer;
import net.jimboi.canary.stage_a.cuplet.Cuplet;

import org.bstone.camera.Camera;
import org.bstone.camera.OrthographicCamera;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.Renderable;
import org.bstone.render.model.TextModelManager;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.scene.Scene;
import org.bstone.scene.render.RenderScene;
import org.bstone.util.direction.Direction;
import org.bstone.window.Window;
import org.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.FontSheet;
import org.zilar.sprite.SpriteUtil;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 10/29/17.
 */
public class MainRenderer extends RenderScene
{
	private Camera camera;
	private ScreenSpace screenSpace;

	public Mesh mshQuad;
	public Bitmap bmpFont;
	public Texture texFont;
	public TextureAtlas atsFont;

	public FontSheet fontSheet;

	public SimpleProgramRenderer simpleRenderer;
	public CollisionBoxRenderer collisionBoxRenderer;

	private TextModelManager textModelManager;
	private GuiManager guiManager;

	public Set<Renderable> renderables;
	public Set<ComponentRenderable> renderComponents;

	private boolean first = false;

	public MainRenderer(RenderEngine renderEngine, Scene scene)
	{
		super(renderEngine, scene);
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		System.out.println("LOADING");
		super.onServiceStart(handler);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.renderables = new HashSet<>();
		this.renderComponents = new HashSet<>();

		final Window window = Cuplet.getCuplet().getFramework().getWindow();

		//Gui
		this.guiManager = new GuiManager(new OrthographicCamera(window.getWidth(), window.getHeight()), window.getCurrentViewPort());

		//Camera
		this.camera = new OrthographicCamera(window.getWidth(), window.getHeight());
		this.screenSpace = new ScreenSpace(window.getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		//Mesh
		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
		this.mshQuad = ModelUtil.createStaticMesh(mb.bake(false, true));
		mb.clear();

		//Texture
		this.bmpFont = new Bitmap(new ResourceLocation("base:font.png"));
		this.texFont = new Texture(this.bmpFont, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);

		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.texFont), 256, 256);
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.atsFont = SpriteUtil.createTextureAtlas(tab.bake());
		tab.clear();

		this.fontSheet = new FontSheet(Asset.wrap(this.atsFont), 0, (char) 0, (char) 255);

		//Manager
		this.textModelManager = new TextModelManager(this.fontSheet);

		//Renderer
		this.simpleRenderer = new SimpleProgramRenderer();
		this.collisionBoxRenderer = new CollisionBoxRenderer();

		Cuplet.getCuplet().getSceneManager().setSceneLoaded();
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		super.onServiceStop(handler);

		this.simpleRenderer.close();
		this.collisionBoxRenderer.close();

		this.textModelManager.destroy();

		this.bmpFont.close();
		this.texFont.close();
		this.mshQuad.close();
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		super.onRenderUpdate(renderEngine, delta);

		this.guiManager.update();

		this.simpleRenderer.bind(this.camera.view(), this.camera.projection());
		{
			Matrix4f matrix = new Matrix4f();
			Vector4f vec4 = new Vector4f();

			for (Renderable renderable : this.renderables)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						renderable.getRenderModel().getMaterial(),
						renderable.getRenderTransformation(matrix));
			}

			this.renderComponents.clear();

			MainScene scene = (MainScene) Cuplet.getCuplet().getSceneManager().getCurrentScene();
			scene.getWorld().getEntityManager().getEntityManager().getComponents(ComponentRenderable.class, this.renderComponents);

			for (ComponentRenderable renderable : this.renderComponents)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						renderable.getRenderModel().getMaterial(),
						renderable.getRenderTransformation(matrix));
			}

			for(Gui gui : this.guiManager.elements)
			{
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

	public GuiManager getGuiManager()
	{
		return this.guiManager;
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
