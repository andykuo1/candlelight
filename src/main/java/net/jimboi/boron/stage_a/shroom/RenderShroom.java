package net.jimboi.boron.stage_a.shroom;

import net.jimboi.boron.base.RenderBase;

import org.bstone.input.InputManager;
import org.bstone.mogli.Program;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.camera.PerspectiveCamera;
import org.lwjgl.glfw.GLFW;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.zilar.base.Assets;
import org.zilar.bounding.BoundingRenderer;
import org.zilar.gui.GuiRenderer;
import org.zilar.gui.base.GuiManager;
import org.zilar.renderer.SimpleRenderer;

/**
 * Created by Andy on 7/17/17.
 */
public class RenderShroom extends RenderBase<SceneShroom>
{
	protected final PerspectiveCamera mainCamera;

	protected SimpleRenderer simpleRenderer;
	protected BoundingRenderer boundingRenderer;
	protected GuiRenderer guiRenderer;

	protected GuiManager guiManager;

	private final Assets assets;

	public RenderShroom()
	{
		this.mainCamera = new PerspectiveCamera(640, 480);
		this.assets = Assets.create(Shroom.ENGINE.getAssetManager(), "shroom", new SemanticVersion("0.0.0"));
	}

	@Override
	protected void onLoad(RenderEngine renderEngine)
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

		//SYSTEMS

		this.guiManager = new GuiManager(new OrthographicCamera(640, 480), renderEngine.getWindow().getCurrentViewPort());

		Shroom.ENGINE.getInputEngine().addInputLayer(this.guiManager);

		//ASSETS

		final Asset<Program> simpleProgram = Shroom.ENGINE.getAssetManager().getAsset(Program.class, "simple");
		final Asset<Program> wireframeProgram = Shroom.ENGINE.getAssetManager().getAsset(Program.class, "wireframe");

		//RENDERERS

		this.simpleRenderer = new SimpleRenderer(simpleProgram);
		this.boundingRenderer = new BoundingRenderer(this.getScene().getBoundingManager(), wireframeProgram);
		this.guiRenderer = new GuiRenderer(this.guiManager, simpleProgram);
	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{
		this.guiManager.update();
	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{
		this.assets.unload();

		Shroom.ENGINE.getInputEngine().removeInputLayer(this.guiManager);

		this.guiManager.destroy();
	}

	@Override
	protected Class<SceneShroom> getSceneClass()
	{
		return SceneShroom.class;
	}

	@Override
	public PerspectiveCamera getMainCamera()
	{
		return this.mainCamera;
	}
}
