package net.jimboi.boron.base;

import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.base.renderer.SimpleRenderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.Renderable;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.Camera;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.asset.AssetManager;
import org.qsilver.resource.MeshLoader;
import org.qsilver.resource.TextureAtlasLoader;
import org.zilar.base.Assets;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

import java.util.Iterator;

/**
 * Created by Andy on 7/23/17.
 */
public abstract class RendererAssetBase extends RendererSceneBase
{
	protected SimpleRenderer simpleRenderer;

	private final AssetManager assetManager;
	private final Assets assets;

	public RendererAssetBase(RenderEngine renderEngine, Camera camera, AssetManager assetManager, String domain, SemanticVersion version)
	{
		super(renderEngine, camera);

		this.assetManager = assetManager;
		this.assets = Assets.create(assetManager, domain, version);
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		this.assets.load();

		//INPUT
		OldInputManager.registerMousePosX("mousex");
		OldInputManager.registerMousePosY("mousey");

		OldInputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
		OldInputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		OldInputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		OldInputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		OldInputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		OldInputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		OldInputManager.registerKey("up", GLFW.GLFW_KEY_E);
		OldInputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		OldInputManager.registerKey("action", GLFW.GLFW_KEY_F);
		OldInputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		final Asset<Program> simpleProgram = this.assetManager.getAsset(Program.class, "simple");

		//ASSETS
		MeshBuilder mb = new MeshBuilder();
		{
			mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
			this.assetManager.registerAsset(Mesh.class, "2d", new MeshLoader.MeshParameter(mb.bake(false, false)));
			mb.clear();
		}

		Asset<Texture> font = this.assetManager.getAsset(Texture.class, "font");
		TextureAtlasBuilder tab = new TextureAtlasBuilder(font, 256, 256);
		{
			tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
			this.assetManager.registerAsset(TextureAtlas.class, "font", new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
			tab.clear();
		}

		//RENDERERS
		this.simpleRenderer = new SimpleRenderer(handler, simpleProgram, this.getMainCamera(), this::getSimpleRenderables);
		this.simpleRenderer.start();

		super.onServiceStart(handler);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		this.simpleRenderer.stop();

		this.assets.unload();

		super.onServiceStop(handler);
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.simpleRenderer.render(this.getMainCamera(), this.getSimpleRenderables());
		super.onRenderUpdate(renderEngine, delta);
	}

	@Override
	protected void onLoad(RenderEngine renderEngine)
	{

	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{

	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{

	}

	protected abstract Iterator<Renderable> getSimpleRenderables();

	protected final AssetManager getAssetManager()
	{
		return this.assetManager;
	}
}
