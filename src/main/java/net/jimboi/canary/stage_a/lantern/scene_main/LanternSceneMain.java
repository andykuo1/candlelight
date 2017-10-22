package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.boron.stage_a.base.collisionbox.CollisionBoxManager;
import net.jimboi.canary.stage_a.lantern.Lantern;
import net.jimboi.canary.stage_a.lantern.scene_main.entity.EntityCrate;
import net.jimboi.canary.stage_a.lantern.scene_main.entity.EntityPlayer;

import org.bstone.application.scene.Scene;
import org.bstone.application.scene.SceneManager;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.input.InputEngine;
import org.bstone.input.context.InputContext;
import org.bstone.livingentity.LivingEntityManager;
import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.view.ScreenSpace;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 10/20/17.
 */
public class LanternSceneMain extends Scene
{
	public final LivingEntityManager livings;
	public final CollisionBoxManager collisionManager;

	public final PerspectiveCamera camera;

	public InputContext input;
	public FirstPersonCameraHandler controller;

	public ScreenSpace screenSpace;

	public LanternSceneMain()
	{
		this.livings = new LivingEntityManager();
		this.collisionManager = new CollisionBoxManager();

		this.camera = new PerspectiveCamera(0, 0, 0, 640, 480);
	}

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		InputEngine inputEngine = Lantern.getLantern().getFramework().getInputEngine();
		this.input = Lantern.getLantern().getFramework().getInputContext();

		this.screenSpace = new ScreenSpace(Lantern.getLantern().getFramework().getWindow().getCurrentViewPort(),
				this.camera, Direction.CENTER, Direction.NORTHEAST);

		this.input.getMapping().registerInputMapping("mousex",
				inputEngine.getMouse().getCursorX());
		this.input.getMapping().registerInputMapping("mousey",
				inputEngine.getMouse().getCursorY());
		this.input.getMapping().registerInputMapping("mouselock",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_P));

		this.input.getMapping().registerInputMapping("forward",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_W));
		this.input.getMapping().registerInputMapping("backward",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_S));
		this.input.getMapping().registerInputMapping("up",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE));
		this.input.getMapping().registerInputMapping("down",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_E));
		this.input.getMapping().registerInputMapping("left",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_A));
		this.input.getMapping().registerInputMapping("right",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_D));
		this.input.getMapping().registerInputMapping("sprint",
				inputEngine.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT_SHIFT));

		this.controller = new FirstPersonCameraHandler(this.camera);

		EntityPlayer player = new EntityPlayer(new Transform3());
		this.livings.addLivingEntity(player);
		this.livings.addLivingEntity(new EntityCrate(new Transform3().setPosition(1, 0, 5)));

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
		this.input.removeListener(this.controller);
		this.livings.clear();

		Lantern.getLantern().getFramework().getInputEngine().destroyContext(this.input);
	}

	public PerspectiveCamera getCamera()
	{
		return this.camera;
	}

	public LivingEntityManager getLivings()
	{
		return this.livings;
	}

	public CollisionBoxManager getCollisionManager()
	{
		return this.collisionManager;
	}
}
