package canary.lantern.scene_test;

import canary.lantern.Lantern;
import canary.lantern.scene_test.entity.EntityCrate;
import canary.lantern.scene_test.entity.EntityPlayer;

import canary.bstone.application.game.GameInputs;
import canary.bstone.camera.PerspectiveCamera;
import canary.bstone.gameobject.GameObjectManager;
import canary.bstone.scene.Scene;
import canary.bstone.scene.SceneManager;
import canary.bstone.transform.Transform3;
import canary.bstone.window.view.ScreenSpace;

/**
 * Created by Andy on 10/20/17.
 */
public class SceneTest extends Scene
{
	public final GameObjectManager livings;

	public final PerspectiveCamera camera;

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

		GameInputs.loadBaseInputs(Lantern.getLantern().getInputEngine());
		this.controller = new FirstPersonCameraHandler(this.camera);

		EntityPlayer player = new EntityPlayer(new Transform3());
		this.livings.addGameObject(player);
		this.livings.addGameObject(new EntityCrate(new Transform3().setPosition(1, 0, 5)));

		this.controller.setTarget(player.getTransform());
	}

	@Override
	protected void onSceneUpdate()
	{
		this.livings.update();
		this.controller.update(Lantern.getLantern().getInputEngine());
	}

	@Override
	protected void onSceneDestroy()
	{
		System.out.println("DESTROYED!");
		this.livings.clear();
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
