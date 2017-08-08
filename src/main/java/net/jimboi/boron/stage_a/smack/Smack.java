package net.jimboi.boron.stage_a.smack;

import net.jimboi.apricot.base.renderer.property.PropertyColor;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.smack.collisionbox.CollisionBoxRenderer;

import org.bstone.game.GameEngine;
import org.bstone.game.GameHandler;
import org.bstone.material.Material;
import org.bstone.material.MaterialManager;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.Renderable;
import org.bstone.render.model.Model;
import org.bstone.render.model.TextModel;
import org.bstone.render.model.TextModelManager;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.util.direction.Direction;
import org.bstone.window.Window;
import org.bstone.window.camera.Camera;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.input.InputManager;
import org.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.asset.Asset;
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
 * Created by Andy on 8/4/17.
 */
public class Smack implements GameHandler
{
	public static Smack getSmack()
	{
		return INSTANCE;
	}

	private static Smack INSTANCE;
	private static GameEngine GAMEENGINE;

	private Smack() {}

	public static void main(String[] args)
	{
		INSTANCE = new Smack();
		GAMEENGINE = new GameEngine(INSTANCE);
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

	public MaterialManager materialManager;
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

	public Set<Renderable> renderables = new HashSet<>();
	private Set<EntityComponentRenderable> renderables2 = new HashSet<>();

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
		this.camera = new OrthographicCamera(640, 480);

		this.getInput().registerMousePosX("mousex");
		this.getInput().registerMousePosY("mousey");
		this.getInput().registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		this.getInput().registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
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

		this.materialManager = new MaterialManager();
		this.textModelManager = new TextModelManager(this.materialManager, this.fontSheet, "simple");

		this.simpleRenderer = new SimpleProgramRenderer();
		this.collisionBoxRenderer = new CollisionBoxRenderer();

		this.ammoModel = this.textModelManager.createDynamicText("___");
		this.renderables.add(new Renderable()
		{
			@Override
			public Matrix4f getRenderOffsetTransformation(Matrix4f dst)
			{
				return dst.translation(Smack.this.getWorld().getPlayer().getTransform().position3()).translate(1, 1, 0);
			}

			@Override
			public Model getRenderModel()
			{
				return Smack.this.ammoModel;
			}
		});
	}

	private int prevAmmo;

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
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
			Vector4f vec4 = new Vector4f();

			for (Renderable renderable : this.renderables)
			{
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						renderable.getRenderModel().getMaterial().getComponent(PropertyTexture.class).getSprite(),
						true,
						vec4.set(1, 1, 1, 0),
						renderable.getRenderTransformation(matrix));
			}

			this.renderables2.clear();
			this.world.getSmacks().getEntityManager().getSimilarComponents(EntityComponentRenderable.class, this.renderables2);

			for (EntityComponentRenderable renderable : this.renderables2)
			{
				final Material material = renderable.getRenderModel().getMaterial();
				final PropertyTexture propertyTexture = material.getComponent(PropertyTexture.class);
				final PropertyColor propertyColor = material.getComponent(PropertyColor.class);
				this.simpleRenderer.draw(renderable.getRenderModel().getMesh().getSource(),
						propertyTexture.getSprite(),
						true,
						propertyColor.getColor(),
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
	public void onUnload(RenderEngine renderEngine)
	{
		this.simpleRenderer.close();
		this.collisionBoxRenderer.close();
		this.textModelManager.destroy();
		this.bmpFont.close();
		this.texFont.close();
		this.quad.close();
	}

	public InputManager getInput()
	{
		return GAMEENGINE.getInput();
	}

	public Window getWindow()
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