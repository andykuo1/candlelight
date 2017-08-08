package net.jimboi.apricot.base;

import org.bstone.material.MaterialManager;
import org.bstone.render.RenderEngine;
import org.bstone.window.camera.CameraController;
import org.qsilver.scene.Scene;
import org.zilar.animation.AnimationManager;
import org.zilar.entity.EntityManager;

/**
 * Created by Andy on 7/5/17.
 */
public abstract class OldSceneBase extends Scene
{
	private final OldRenderBase renderer;
	private CameraController cameraController;

	protected EntityManager entityManager;
	protected MaterialManager materialManager;
	protected AnimationManager animationManager;

	public OldSceneBase(OldRenderBase renderer, CameraController cameraController)
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
		this.renderer.start();
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
		this.renderer.stop();
		this.cameraController.stop();
	}

	@Override
	protected void onSceneDestroy()
	{
		this.materialManager.clear();
	}

	public OldRenderBase getRenderer()
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