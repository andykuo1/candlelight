package net.jimboi.boron.stage_a.base;

import org.bstone.render.RenderEngine;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.window.camera.Camera;
import org.bstone.window.input.InputManager;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 8/5/17.
 */
public abstract class RenderSceneSimpleBase extends RenderSceneBase
{
	private Camera mainCamera;

	private SimpleProgramRenderer simpleProgramRenderer;

	private final InputManager inputManager;

	public RenderSceneSimpleBase(RenderEngine renderEngine, Camera camera, InputManager inputManager)
	{
		super(renderEngine);

		this.mainCamera = camera;
		this.inputManager = inputManager;
	}

	@Override
	protected void onServiceStart(RenderEngine handler)
	{
		//INPUT
		this.inputManager.registerMousePosX("mousex");
		this.inputManager.registerMousePosY("mousey");

		this.inputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		this.inputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);
		this.inputManager.registerKey("mouselock", GLFW.GLFW_KEY_P);

		this.inputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		this.inputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		this.inputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		this.inputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		this.inputManager.registerKey("up", GLFW.GLFW_KEY_E);
		this.inputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);
		this.inputManager.registerKey("action", GLFW.GLFW_KEY_F);
		this.inputManager.registerKey("sprint", GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

		this.simpleProgramRenderer = new SimpleProgramRenderer();

		this.load(handler);
	}

	@Override
	protected void onServiceStop(RenderEngine handler)
	{
		this.unload(handler);
		this.simpleProgramRenderer.close();
	}

	@Override
	protected void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.simpleProgramRenderer.bind(this.mainCamera.view(), this.mainCamera.projection());
		{
			this.renderSimple(renderEngine, this.simpleProgramRenderer, delta);
		}
		this.simpleProgramRenderer.unbind();
	}

	protected abstract void load(RenderEngine renderEngine);

	protected abstract void unload(RenderEngine renderEngine);

	protected abstract void renderSimple(RenderEngine renderEngine, SimpleProgramRenderer renderer, double delta);

	public Camera getMainCamera()
	{
		return this.mainCamera;
	}
}
