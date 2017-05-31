package net.jimboi.blob;

import net.jimboi.blob.livings.LivingPlayer;
import net.jimboi.blob.livings.LivingZombie;
import net.jimboi.mod.Light;

import org.bstone.input.InputManager;
import org.bstone.poma.Poma;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Andy on 4/19/17.
 */
public class SceneBlob extends SceneBlobBase
{
	private LivingPlayer player;

	public SceneBlob()
	{
		super(new RendererBlob());
	}

	@Override
	public void onSceneCreate()
	{
		this.player = new LivingPlayer(0, 0, 0);
		this.livingManager.add(this.player);
	}

	@Override
	public void onSceneStart()
	{
		InputManager.registerMousePosX("mousex");
		InputManager.registerMousePosY("mousey");
		InputManager.registerMouseScrollY("scrolly");

		InputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		InputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);

		InputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		InputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		InputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		InputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		InputManager.registerKey("up", GLFW.GLFW_KEY_E);
		InputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);

		this.livingManager.add(new LivingZombie(1, 1, 1));
	}

	@Override
	public void onSceneUpdate(double delta)
	{
		Light light = RendererBlob.lights.get(0);
		light.position = new Vector4f(RendererBlob.camera.getTransform().position, 1);
		light.coneDirection = RendererBlob.camera.getTransform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	@Override
	public void onSceneDestroy()
	{
		Poma.OUT("Destroying Scene . . .");
	}
}
