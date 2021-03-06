package boron.stage_a.smack;

import boron.base.asset.Asset;
import boron.base.game.OldishGameEngine;
import boron.base.game.OldishGameHandler;
import boron.base.render.OldRenderEngine;
import boron.base.render.Renderable;
import boron.base.render.material.Material;
import boron.base.render.material.PropertyColor;
import boron.base.render.material.PropertyTexture;
import boron.base.render.model.Model;
import boron.base.render.model.TextModel;
import boron.base.render.model.TextModelManager;
import boron.base.render.renderer.SimpleProgramRenderer;
import boron.base.sprite.FontSheet;
import boron.base.sprite.SpriteUtil;
import boron.base.sprite.TextureAtlas;
import boron.base.sprite.TextureAtlasBuilder;
import boron.base.window.OldWindow;
import boron.base.window.input.InputManager;
import boron.stage_a.base.basicobject.ComponentRenderable;
import boron.stage_a.base.collisionbox.CollisionBoxRenderer;

import boron.bstone.camera.Camera;
import boron.bstone.camera.PerspectiveCamera;
import boron.bstone.mogli.Bitmap;
import boron.bstone.mogli.Mesh;
import boron.bstone.mogli.Texture;
import boron.bstone.util.Direction;
import boron.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import boron.qsilver.ResourceLocation;
import boron.zilar.meshbuilder.MeshBuilder;
import boron.zilar.meshbuilder.ModelUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/4/17.
 */
public class Smack implements OldishGameHandler
{
	public static Smack getSmack()
	{
		return INSTANCE;
	}

	private static Smack INSTANCE;
	private static OldishGameEngine GAMEENGINE;

	private Smack() {}

	public static void main(String[] args)
	{
		INSTANCE = new Smack();
		GAMEENGINE = new OldishGameEngine(INSTANCE);
		GAMEENGINE.start();
	}

	private ScreenSpace screenSpace;
	private SmackWorld world;

	private boolean debugMode = false;

	@Override
	public void onFirstUpdate()
	{
		this.screenSpace = new ScreenSpace(this.getWindow().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		this.world = new SmackWorld();
		this.world.onCreate();
	}

	@Override
	public void onPreUpdate()
	{

	}

	@Override
	public void onUpdate()
	{
		this.world.onUpdate();

		if (this.getInput().isInputReleased("debug"))
		{
			this.debugMode = !this.debugMode;
		}
	}

	@Override
	public void onLastUpdate()
	{
		this.world.onDestroy();
	}

	public TextModelManager textModelManager;
	public Camera camera;

	public Mesh quad;

	public Bitmap bmpFont;
	public Texture texFont;
	public TextureAtlas atsFont;
	public FontSheet fontSheet;
	public SimpleProgramRenderer simpleRenderer;
	public CollisionBoxRenderer collisionBoxRenderer;

	public TextModel ammoModel;
	public Model shadowModel;

	public Set<Renderable> renderables = new HashSet<>();
	private Set<ComponentRenderable> renderables2 = new HashSet<>();

	@Override
	public void onLoad(OldRenderEngine renderEngine)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		this.camera = new PerspectiveCamera(640, 480);
		//((OrthographicCamera)this.camera).setClippingBound(-5, 5, 5, -5);

		this.getInput().registerMousePosX("mousex");
		this.getInput().registerMousePosY("mousey");
		this.getInput().registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		this.getInput().registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
		this.getInput().registerMouseScrollY("zoom");
		this.getInput().registerKey("debug", GLFW.GLFW_KEY_P);

		MeshBuilder mb = new MeshBuilder();
		{
			//Create 2dc
			mb.addPlane(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F), 0.0F, new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));
			this.quad = ModelUtil.createStaticMesh(mb.bake(false, true));
		}
		mb.clear();

		this.bmpFont = new Bitmap(new ResourceLocation("smack:font.png"));
		this.texFont = new Texture(this.bmpFont, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);

		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.texFont), 256, 256);
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.atsFont = SpriteUtil.createTextureAtlas(tab.bake());
		tab.clear();

		this.fontSheet = new FontSheet(Asset.wrap(this.atsFont), 0, (char) 0, (char) 255);

		this.textModelManager = new TextModelManager(this.fontSheet);

		this.simpleRenderer = new SimpleProgramRenderer();
		this.collisionBoxRenderer = new CollisionBoxRenderer();

		this.ammoModel = this.textModelManager.createDynamicText("___");
		this.ammoModel.getMaterial().setProperty(PropertyTexture.TRANSPARENCY_NAME, true);
		PropertyColor.PROPERTY.bind(this.ammoModel.getMaterial())
				.setColor(0x007BFF)
				.unbind();

		this.shadowModel = new Model(this.ammoModel.getMesh(), this.ammoModel.getMaterial().derive(new Material()));
		this.shadowModel.transformation().set(this.ammoModel.transformation());
		PropertyColor.PROPERTY.bind(this.shadowModel.getMaterial())
				.setColor(0x29424A)
				.unbind();

		this.renderables.add(new Renderable()
		{
			@Override
			public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
			{
				//TODO: ATTENTION! THIS HAS Z OFFSET!
				return dst.translation(Smack.this.getWorld().getPlayer().getTransform().position3()).translate(1, 1, 5.1F);
			}

			@Override
			public Model getRenderModel()
			{
				return Smack.this.ammoModel;
			}
		});

		this.renderables.add(new Renderable()
		{
			@Override
			public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
			{
				//TODO: ATTENTION! THIS HAS Z OFFSET!
				return dst.translation(Smack.this.getWorld().getPlayer().getTransform().position3()).translate(1 + 0.08F, 1 - 0.08F, 5);
			}

			@Override
			public Model getRenderModel()
			{
				return Smack.this.shadowModel;
			}
		});
	}

	private int prevAmmo;

	@Override
	public void onRender(OldRenderEngine renderEngine, double delta)
	{
		if (this.prevAmmo != this.getWorld().getPlayer().getAmmo())
		{
			StringBuilder sb = new StringBuilder();
			if (this.getWorld().getPlayer().getAmmo() < 100) sb.append('_');
			if (this.getWorld().getPlayer().getAmmo() < 10) sb.append('_');
			sb.append(this.getWorld().getPlayer().getAmmo());
			if (sb.length() > 3) sb.setLength(3);
			this.ammoModel.setText(sb.toString());
			this.prevAmmo = this.getWorld().getPlayer().getAmmo();
		}

		this.simpleRenderer.bind(this.camera.view(), this.camera.projection());
		{
			Matrix4f matrix = new Matrix4f();

			for (Renderable renderable : this.renderables)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						renderable.getRenderModel().getMaterial(),
						renderable.getRenderTransformation(matrix));
			}

			this.renderables2.clear();
			this.world.getSmacks().getEntityManager().getComponents(ComponentRenderable.class, this.renderables2);

			for (ComponentRenderable renderable : this.renderables2)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						renderable.getRenderModel().getMaterial(),
						renderable.getRenderTransformation(matrix));
			}
		}
		this.simpleRenderer.unbind();

		if (this.debugMode)
		{
			this.collisionBoxRenderer.bind(this.camera.view(), this.camera.projection());
			{
				this.collisionBoxRenderer.draw(this.world.getSmacks().getBoundingManager().getColliders(), 0x00FF00);
			}
			this.collisionBoxRenderer.unbind();
		}
	}

	@Override
	public void onUnload(OldRenderEngine renderEngine)
	{
		try
		{
			this.simpleRenderer.close();
			this.collisionBoxRenderer.close();
			this.textModelManager.destroy();
			this.bmpFont.close();
			this.texFont.close();
			this.quad.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public InputManager getInput()
	{
		return GAMEENGINE.getInput();
	}

	public OldWindow getWindow()
	{
		return GAMEENGINE.getWindow();
	}

	public ScreenSpace getScreenSpace()
	{
		return this.screenSpace;
	}

	public SmackWorld getWorld()
	{
		return this.world;
	}
}
