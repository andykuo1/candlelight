package net.jimboi.canary.stage_a.lantern.scene_test;

import net.jimboi.canary.stage_a.lantern.Lantern;
import net.jimboi.canary.stage_a.lantern.scene_test.entity.EntityCrate;
import net.jimboi.canary.stage_a.lantern.scene_test.entity.EntityPlayer;

import org.bstone.camera.PerspectiveCamera;
import org.bstone.gameobject.GameObjectManager;
import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.adapter.InputAdapter;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;
import org.bstone.transform.Transform3;
import org.bstone.util.Direction;
import org.bstone.window.view.ScreenSpace;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneTest extends Scene
{
	public final GameObjectManager livings;

	public final PerspectiveCamera camera;

	public InputContext input;
	public FirstPersonCameraHandler controller;

	public ScreenSpace screenSpace;

	public SceneTest()
	{
		this.livings = new GameObjectManager();

		this.camera = new PerspectiveCamera(0, 0, 0, 640, 480);
	}

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		System.out.println("CREATED!");

		final InputEngine inputEngine = Lantern.getLantern().getInputEngine();
		this.input = Lantern.getLantern().getInputEngine().getDefaultContext();

		this.screenSpace = new ScreenSpace(Lantern.getLantern().getWindow().getCurrentViewPort(),
				this.camera, Direction.CENTER, Direction.NORTHEAST);

		this.input.registerEvent("mousex",
				inputEngine.getMouse().getCursorX()::getRange);
		this.input.registerEvent("mousey",
				inputEngine.getMouse().getCursorY()::getRange);
		this.input.registerEvent("mouselock",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_P)::getAction);

		this.input.registerEvent("forward",
				InputAdapter.asRange(
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_W)::getState,
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_S)::getState
				),
				InputAdapter.asRange(
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_UP)::getState,
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)::getState
				));

		this.input.registerEvent("up",
				InputAdapter.asRange(
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE)::getState,
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_E)::getState
				));

		this.input.registerEvent("right",
				InputAdapter.asRange(
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_D)::getState,
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_A)::getState
				),
				InputAdapter.asRange(
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)::getState,
						inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)::getState
				));

		this.input.registerEvent("sprint",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT_SHIFT)::getState);

		this.controller = new FirstPersonCameraHandler(this.camera);

		EntityPlayer player = new EntityPlayer(new Transform3());
		this.livings.addGameObject(player);
		this.livings.addGameObject(new EntityCrate(new Transform3().setPosition(1, 0, 5)));

		this.controller.setTarget(player.getTransform());
		this.input.addListener(0, this.controller);
	}

	@Override
	protected void onSceneUpdate()
	{
		this.livings.update();
		this.controller.update();
	}

	@Override
	protected void onSceneDestroy()
	{
		System.out.println("DESTROYED!");
		this.input.removeListener(this.controller);
		this.livings.clear();

		Lantern.getLantern().getInputEngine().destroyContext(this.input);
	}

	public PerspectiveCamera getCamera()
	{
		return this.camera;
	}

	public GameObjectManager getLivings()
	{
		return this.livings;
	}
}
