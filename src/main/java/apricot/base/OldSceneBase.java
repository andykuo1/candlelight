package apricot.base;

import apricot.base.animation.AnimationManager;
import apricot.base.entity.EntityManager;
import apricot.base.material.OldMaterialManager;
import apricot.base.scene.Scene;
import apricot.base.render.OldRenderEngine;

import apricot.bstone.camera.CameraController;

/**
 * Created by Andy on 7/5/17.
 */
@Deprecated
public abstract class OldSceneBase extends Scene
{
	private final OldRenderBase renderer;
	private CameraController cameraController;

	protected EntityManager entityManager;
	protected OldMaterialManager materialManager;
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
		this.materialManager = new OldMaterialManager();
		this.animationManager = new AnimationManager();
	}

	@Override
	protected void onSceneLoad(OldRenderEngine renderEngine)
	{
		System.out.println("BOO!!");
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
	protected void onSceneUnload(OldRenderEngine renderEngine)
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

	public OldMaterialManager getMaterialManager()
	{
		return this.materialManager;
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}
}
