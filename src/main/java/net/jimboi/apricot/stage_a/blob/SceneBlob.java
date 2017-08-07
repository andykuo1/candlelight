package net.jimboi.apricot.stage_a.blob;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.stage_a.blob.livings.LivingPlayer;
import net.jimboi.apricot.stage_a.blob.livings.LivingZombie;
import net.jimboi.apricot.stage_a.mod.ModLight;

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
		super(new RendererBlob(OldGameEngine.RENDERENGINE));
	}

	@Override
	protected void onSceneCreate()
	{
		this.player = new LivingPlayer(0, 0, 0);
		this.livingManager.add(this.player);
	}

	@Override
	protected void onSceneStart()
	{
		OldInputManager.registerMousePosX("mousex");
		OldInputManager.registerMousePosY("mousey");
		OldInputManager.registerMouseScrollY("scrolly");

		OldInputManager.registerMouse("mouseleft", GLFW.GLFW_MOUSE_BUTTON_LEFT);
		OldInputManager.registerMouse("mouseright", GLFW.GLFW_MOUSE_BUTTON_RIGHT);

		OldInputManager.registerKey("forward", GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_UP);
		OldInputManager.registerKey("backward", GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_DOWN);
		OldInputManager.registerKey("left", GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_LEFT);
		OldInputManager.registerKey("right", GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_RIGHT);
		OldInputManager.registerKey("up", GLFW.GLFW_KEY_E);
		OldInputManager.registerKey("down", GLFW.GLFW_KEY_SPACE);

		this.livingManager.add(new LivingZombie(1, 1, 1));
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		ModLight light = RendererBlob.lights.get(0);
		light.position = new Vector4f(RendererBlob.camera.transform().position3(), 1);
		light.coneDirection = RendererBlob.camera.transform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneDestroy()
	{

	}
}
