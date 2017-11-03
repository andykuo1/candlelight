package net.jimboi.canary.stage_a.lantern.scene_main;

import net.jimboi.canary.stage_a.cuplet.collisionbox.CollisionBoxManager;
import net.jimboi.canary.stage_a.lantern.Lantern;

import org.bstone.input.InputContext;
import org.bstone.input.InputEngine;
import org.bstone.input.adapter.InputAdapter;
import org.bstone.livingentity.LivingEntityManager;
import org.bstone.scene.Scene;
import org.bstone.scene.SceneManager;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 11/2/17.
 */
public class SceneMain extends Scene
{
	private CollisionBoxManager collisionManager;
	private LivingEntityManager entityManager;

	@Override
	protected void onSceneCreate(SceneManager sceneManager)
	{
		this.collisionManager = new CollisionBoxManager();
		this.entityManager = new LivingEntityManager();

		final InputEngine inputs = Lantern.getLantern().getFramework().getInputEngine();
		final InputContext context = inputs.getDefaultContext();

		context.registerEvent("mousex", inputs.getMouse().getCursorX()::getRange);
		context.registerEvent("mousey", inputs.getMouse().getCursorY()::getRange);

		context.registerEvent("mouseleft", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_LEFT)::getAction);
		context.registerEvent("mouseright", inputs.getMouse().getButton(GLFW.GLFW_MOUSE_BUTTON_RIGHT)::getAction);

		context.registerEvent("exit", inputs.getKeyboard().getButton(GLFW.GLFW_KEY_ESCAPE)::getState);

		context.registerEvent("forward",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_W)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_S)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_UP)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_DOWN)::getState
				));

		context.registerEvent("up",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_SPACE)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_E)::getState
				));

		context.registerEvent("right",
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_D)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_A)::getState
				),
				InputAdapter.asRange(
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_RIGHT)::getState,
						inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT)::getState
				));

		context.registerEvent("sprint",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_LEFT_SHIFT)::getState);

		context.registerEvent("action",
				inputs.getKeyboard().getButton(GLFW.GLFW_KEY_F)::getState);

		this.entityManager.addLivingEntity(new EntityPlayer());
	}

	@Override
	protected void onSceneUpdate()
	{
		this.collisionManager.update();
		this.entityManager.update();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.entityManager.clear();
		this.collisionManager.clear();
	}

	public LivingEntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public CollisionBoxManager getCollisionManager()
	{
		return this.collisionManager;
	}
}
