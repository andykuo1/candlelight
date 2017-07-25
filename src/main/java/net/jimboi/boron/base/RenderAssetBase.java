package net.jimboi.boron.base;

import org.bstone.input.InputManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.Camera;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.render.RenderEngine;
import org.qsilver.render.Renderable;
import org.qsilver.resource.MeshLoader;
import org.qsilver.resource.TextureAtlasLoader;
import org.zilar.base.Assets;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.renderer.SimpleRenderer;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

import java.util.Iterator;

/**
 * Created by Andy on 7/23/17.
 */
public abstract class RenderAssetBase extends RenderBase
{
	protected SimpleRenderer simpleRenderer;

	private final AssetManager assetManager;
	private final Assets assets;

	public RenderAssetBase(Camera camera, AssetManager assetManager, String domain, SemanticVersion version)
	{
		super(camera);

		this.assetManager = assetManager;
		this.assets = Assets.create(assetManager, domain, version);
	}

	@Override
	protected void onStart(RenderEngine handler)
	{
		this.assets.load();

		//INPUT
		InputManager.registerMousePosX("mousex");
		InputManager.registerMousePosY("mousey");

		InputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		InputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		InputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		InputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		InputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		InputManager.registerKey("up", GLFW.GLFW_KEY_E);
		InputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		InputManager.registerKey("action", GLFW.GLFW_KEY_F);
		InputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		final Asset<Program> simpleProgram = this.assetManager.getAsset(Program.class, "simple");

		//ASSETS
		MeshBuilder mb = new MeshBuilder();
		{
			mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
			this.assetManager.registerAsset(Mesh.class, "2d", new MeshLoader.MeshParameter(mb.bake(false, false)));
			mb.clear();
		}

		TextureAtlasBuilder tab = new TextureAtlasBuilder();
		{
			Asset<Texture> font = this.assetManager.getAsset(Texture.class, "font");

			tab.addTileSheet(font, 0, 0, 16, 16, 0, 0, 16, 16);
			this.assetManager.registerAsset(TextureAtlas.class, "font", new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
			tab.clear();
		}

		//RENDERERS
		this.simpleRenderer = new SimpleRenderer(simpleProgram);
		handler.startService(this.simpleRenderer);

		super.onStart(handler);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	protected void onStop(RenderEngine handler)
	{
		handler.stopService(this.simpleRenderer);

		this.assets.unload();

		super.onStop(handler);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine)
	{
		this.simpleRenderer.render(this.getMainCamera(), this.getSimpleRenderables());

		super.onRenderUpdate(renderEngine);
	}

	protected abstract Iterator<Renderable> getSimpleRenderables();

	protected final AssetManager getAssetManager()
	{
		return this.assetManager;
	}
}
