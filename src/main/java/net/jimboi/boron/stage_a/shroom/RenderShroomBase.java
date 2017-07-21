package net.jimboi.boron.stage_a.shroom;

import net.jimboi.apricot.stage_c.hoob.collision.CollisionRenderer;
import net.jimboi.boron.base.RenderBase;
import net.jimboi.boron.stage_a.shroom.component.EntityComponentRenderable;

import org.bstone.input.InputManager;
import org.bstone.mogli.Program;
import org.bstone.transform.Transform;
import org.bstone.util.SemanticVersion;
import org.bstone.window.camera.OrthographicCamera;
import org.bstone.window.camera.PerspectiveCamera;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.qsilver.util.iterator.CastIterator;
import org.zilar.base.Assets;
import org.zilar.gui.GuiRenderer;
import org.zilar.gui.base.GuiManager;
import org.zilar.renderer.SimpleRenderer;

import java.util.HashSet;

/**
 * Created by Andy on 7/17/17.
 */
public abstract class RenderShroomBase<S extends SceneShroomBase> extends RenderBase<S>
{
	protected final PerspectiveCamera mainCamera;

	protected SimpleRenderer simpleRenderer;
	protected CollisionRenderer collisionRenderer;
	private Matrix4f boundingOffsetViewMatrix = new Matrix4f().rotateX(Transform.HALF_PI);
	protected GuiRenderer guiRenderer;

	protected GuiManager guiManager;

	private final Assets assets;

	public RenderShroomBase()
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
		this.collisionRenderer = new CollisionRenderer(this.getScene().getCollisionManager(), wireframeProgram);
		this.guiRenderer = new GuiRenderer(this.guiManager, simpleProgram);

		renderEngine.startService(this.simpleRenderer);
		renderEngine.startService(this.collisionRenderer);
		renderEngine.startService(this.guiRenderer);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{
		this.guiManager.update();

		Iterable<EntityComponentRenderable> renderables = this.getScene().getEntityManager().getSimilarComponents(EntityComponentRenderable.class, new HashSet<>());

		this.simpleRenderer.render(this.getMainCamera(), new CastIterator<>(renderables.iterator()));
		this.collisionRenderer.render(this.getMainCamera(), this.boundingOffsetViewMatrix);
		this.guiRenderer.render();
	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{
		renderEngine.stopService(this.simpleRenderer);
		renderEngine.stopService(this.collisionRenderer);
		renderEngine.stopService(this.guiRenderer);

		this.assets.unload();

		Shroom.ENGINE.getInputEngine().removeInputLayer(this.guiManager);

		this.guiManager.destroy();
	}

	@Override
	public PerspectiveCamera getMainCamera()
	{
		return this.mainCamera;
	}
}
