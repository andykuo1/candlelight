package boron.stage_a.goblet;

import boron.base.asset.Asset;
import boron.base.gui.base.Gui;
import boron.base.gui.base.GuiManager;
import boron.base.render.OldRenderEngine;
import boron.base.render.Renderable;
import boron.base.render.model.TextModelManager;
import boron.base.render.renderer.SimpleProgramRenderer;
import boron.base.sprite.FontSheet;
import boron.base.sprite.SpriteUtil;
import boron.base.sprite.TextureAtlas;
import boron.base.sprite.TextureAtlasBuilder;
import boron.base.window.input.InputManager;
import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.collisionbox.CollisionBoxRenderer;

import boron.bstone.camera.Camera;
import boron.bstone.camera.OrthographicCamera;
import boron.bstone.mogli.Bitmap;
import boron.bstone.mogli.Mesh;
import boron.bstone.mogli.Texture;
import boron.bstone.util.Direction;
import boron.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import boron.qsilver.ResourceLocation;
import boron.zilar.meshbuilder.MeshBuilder;
import boron.zilar.meshbuilder.ModelUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/9/17.
 */
public class GobletRenderer
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

	public void load(OldRenderEngine renderEngine)
	{
		this.renderables = new HashSet<>();
		this.renderComponents = new HashSet<>();

		//Gui
		this.guiManager = new GuiManager(new OrthographicCamera(Goblet.getGoblet().getWindow().getWidth(), Goblet.getGoblet().getWindow().getHeight()), Goblet.getGoblet().getWindow().getCurrentViewPort());

		//Camera
		this.camera = new OrthographicCamera(Goblet.getGoblet().getWindow().getWidth(), Goblet.getGoblet().getWindow().getHeight());
		this.screenSpace = new ScreenSpace(Goblet.getGoblet().getWindow().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		//Input
		final InputManager input = Goblet.getGoblet().getInput();
		input.registerMousePosX("mousex");
		input.registerMousePosY("mousey");
		input.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		input.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
		input.registerMouseScrollY("mousescroll");
		input.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		input.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		input.registerKey("up", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		input.registerKey("down", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		input.registerKey("action", GLFW.GLFW_KEY_F);

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
	}

	public void unload(OldRenderEngine renderEngine)
	{
		try
		{
			this.simpleRenderer.close();
			this.collisionBoxRenderer.close();

			this.textModelManager.destroy();

			this.bmpFont.close();
			this.texFont.close();
			this.mshQuad.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void update(OldRenderEngine renderEngine, double delta)
	{
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
			Goblet.getGoblet().getWorld().getEntityManager().getEntityManager().getComponents(ComponentRenderable.class, this.renderComponents);

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

		if (Goblet.isDebugMode())
		{
			this.collisionBoxRenderer.bind(this.camera.view(), this.camera.projection());
			{
				this.collisionBoxRenderer.draw(Goblet.getGoblet().getWorld().getBoundingManager().getColliders(), 0x00FF00);
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
