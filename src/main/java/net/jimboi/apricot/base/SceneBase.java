package net.jimboi.apricot.base;

import org.bstone.material.MaterialManager;
import org.bstone.window.camera.CameraController;
import org.qsilver.render.RenderEngine;
import org.qsilver.scene.Scene;
import org.zilar.animation.AnimationManager;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class SceneBase extends Scene
{
	private final RenderBase renderer;
	private CameraController cameraController;

	protected EntityManager entityManager;
	protected MaterialManager materialManager;
	protected AnimationManager animationManager;

	public SceneBase(RenderBase renderer, CameraController cameraController)
	{
		this.renderer = renderer;
		this.renderer.setScene(this);

		this.cameraController = cameraController;
	}

	@Override
	protected void onSceneCreate()
	{
		this.entityManager = new EntityManager();
		this.materialManager = new MaterialManager();
		this.animationManager = new AnimationManager();
	}

	@Override
	protected void onSceneLoad(RenderEngine renderEngine)
	{
		renderEngine.startService(this.renderer);
		this.cameraController.start(this.renderer.getCamera());
	}

	@Override
	protected abstract void onSceneStart();

	@Override
	protected void onSceneUpdate(double delta)
	{
		this.cameraController.update(delta);
		this.animationManager.update(delta);
		this.entityManager.update();
	}

	@Override
	protected void onSceneStop()
	{
		this.entityManager.clear();
	}

	@Override
	protected void onSceneUnload(RenderEngine renderEngine)
	{
		renderEngine.stopService(this.renderer);
		this.cameraController.stop();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.materialManager.clear();
	}

	public RenderBase getRenderer()
	{
		return this.renderer;
	}

	public CameraController getCameraController()
	{
		return this.cameraController;
	}

	public AnimationManager getAnimationManager()
	{
		return this.animationManager;
	}

	public MaterialManager getMaterialManager()
	{
		return this.materialManager;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
