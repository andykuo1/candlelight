package net.jimboi.canary.stage_a.pong;

import net.jimboi.canary.stage_a.lantern.scene_main.cameracontroller.FirstPersonCameraController;
import net.jimboi.canary.stage_a.pong.component.ComponentTransform;
import net.jimboi.canary.stage_a.pong.entity.Ball;
import net.jimboi.canary.stage_a.pong.entity.Paddle;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.application.game.GameInputs;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.entity.EntityManager;
import org.bstone.input.InputEngine;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;

/**
 * Created by Andy on 12/1/17.
 */
public class Pong extends Scene
{
	public static final int WINDOW_WIDTH = 480;
	public static final int WINDOW_HEIGHT = 480;

	protected EntityManager entityManager = new EntityManager();
	protected PerspectiveCamera camera;
	private FirstPersonCameraController controller;

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		ENGINE.getWindow().setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		ENGINE.getWindow().makeWindowCentered();

		final InputEngine inputs = ENGINE.getInputEngine();

		GameInputs.loadBaseInputs(inputs);

		this.camera = new PerspectiveCamera(0, 0, 10, WINDOW_WIDTH, WINDOW_HEIGHT);

		this.entityManager.addEntity(new Ball());
		this.entityManager.addEntity(new Paddle()).getComponent(ComponentTransform.class).transform.setPosition(5, 0, 0);
		this.entityManager.addEntity(new Paddle()).getComponent(ComponentTransform.class).transform.setPosition(-5, 0, 0);

		this.controller = new FirstPersonCameraController(this.camera);
		inputs.getDefaultContext().addListener(0, this.controller);
	}

	@Override
	protected void onSceneUpdate()
	{
		this.controller.update();
	}

	@Override
	protected void onSceneDestroy()
	{
		ENGINE.getInputEngine().getDefaultContext().removeListener(this.controller);
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public PerspectiveCamera getCamera()
	{
		return this.camera;
	}

	public static Application APPLICATION;
	public static GameEngine ENGINE;

	public static void main(String[] args)
	{
		APPLICATION = new Application();
		APPLICATION.setFramework(ENGINE = new GameEngine());
		ENGINE.getSceneManager().registerScene("init", Pong.class, PongRenderer.class);
		ENGINE.getSceneManager().setNextScene("init");
		APPLICATION.startOnCurrentThread();
		System.exit(0);
	}
}
