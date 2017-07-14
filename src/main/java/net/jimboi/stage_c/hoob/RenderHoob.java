package net.jimboi.stage_c.hoob;

import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.util.SemanticVersion;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.asset.Asset;
import org.qsilver.renderer.Renderable;
import org.qsilver.util.iterator.CastIterator;
import org.zilar.BasicSideScrollCameraController;
import org.zilar.base.Assets;
import org.zilar.base.GameEngine;
import org.zilar.base.RenderBase;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.renderer.SimpleRenderer;
import org.zilar.resourceloader.MeshLoader;
import org.zilar.resourceloader.TextureAtlasLoader;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Andy on 6/25/17.
 */
public class RenderHoob extends RenderBase
{
	private SimpleRenderer simpleRenderer;

	private final Assets assets;

	public RenderHoob()
	{
		super(new PerspectiveCamera(640, 480), new BasicSideScrollCameraController());

		this.assets = Assets.create("hoob", new SemanticVersion("0.0.0"));
	}

	@Override
	public void onRenderLoad()
	{
		this.assets.load();

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

		this.simpleRenderer = new SimpleRenderer(GameEngine.ASSETMANAGER.getAsset(Program.class, "simple"));

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		GameEngine.ASSETMANAGER.registerAsset(Mesh.class, "quad",
				new MeshLoader.MeshParameter(mb.bake(false, true)));
		mb.clear();

		for(int i = 0; i < 20; ++i)
		{
			for(int j = 0; j < 20; ++j)
			{
				mb.addPlane(new Vector2f(i, j), new Vector2f(i + 1, j + 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
			}
		}
		GameEngine.ASSETMANAGER.registerAsset(Mesh.class, "ground", new MeshLoader.MeshParameter(mb.bake(true, true)));
		mb.clear();

		Asset<Texture> crate = GameEngine.ASSETMANAGER.getAsset(Texture.class, "crate");
		Asset<Texture> bunny = GameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny");

		final TextureAtlasBuilder tab = new TextureAtlasBuilder();

		tab.addHorizontalStrip(bunny, 0, 0, 48, 48, 0, 3);
		GameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "bunny",
				new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
		tab.clear();

		tab.addNineSheet(crate, 0, 0, 64, 64, 64, 64, 64, 64);
		GameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "button",
				new TextureAtlasLoader.TextureAtlasParameter(tab.bake()));
		tab.clear();
	}

	@Override
	public void onRenderUpdate()
	{
		Collection<EntityComponentRenderable> renderables = this.getScene().getEntityManager().getComponents(new HashSet<>(), EntityComponentRenderable.class);

		Iterator<Renderable> iter = new CastIterator<>(renderables.iterator());
		this.simpleRenderer.render(this.getCamera(), iter);
	}

	@Override
	public void onRenderUnload()
	{

	}

	@Override
	public PerspectiveCamera getCamera()
	{
		return (PerspectiveCamera) super.getCamera();
	}

	public BasicSideScrollCameraController getCameraController()
	{
		//Although bad practice, since it can change anytime, it is useful...
		return (BasicSideScrollCameraController) this.getCamera().getCameraController();
	}
}
