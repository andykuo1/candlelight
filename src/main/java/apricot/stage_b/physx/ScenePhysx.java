package apricot.stage_b.physx;

import apricot.base.OldGameEngine;
import apricot.base.OldSceneBase;
import apricot.base.controller.BasicFirstPersonCameraController;
import apricot.base.input.OldInputManager;
import apricot.base.render.OldModel;
import apricot.base.renderer.property.OldPropertyDiffuse;
import apricot.base.renderer.property.OldPropertyShadow;
import apricot.base.renderer.property.OldPropertySpecular;
import apricot.base.renderer.property.OldPropertyTexture;
import apricot.stage_b.glim.RenderGlim;
import apricot.stage_b.glim.entity.component.EntityComponent2D;
import apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;
import apricot.stage_b.glim.entity.system.EntitySystem2D;
import apricot.stage_b.glim.entity.system.EntitySystemBillboard;

import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Texture;
import apricot.bstone.transform.Transform3;
import apricot.bstone.util.Direction;
import apricot.bstone.window.view.ScreenSpace;

/**
 * Created by Andy on 6/18/17.
 */
public class ScenePhysx extends OldSceneBase
{
	public static void main(String[] args)
	{
		OldGameEngine.init(ScenePhysx.class, args);
		OldGameEngine.run();
	}

	private EntitySystem2D sys_2D;
	private EntitySystemBillboard sys_billboard;

	private Transform3 player;
	private Transform3 transform;

	private ScreenSpace screenSpace;

	public ScenePhysx()
	{
		super(new RenderGlim(OldGameEngine.RENDERENGINE), new BasicFirstPersonCameraController());
	}

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();
	}

	@Override
	protected void onSceneStart()
	{
		this.sys_2D = new EntitySystem2D(this.entityManager);

		this.sys_billboard = new EntitySystemBillboard(this.entityManager, this.getRenderer().getCamera());

		final Transform3 transform = new Transform3();
		this.player = this.spawnEntity(0, 0, 0);
		this.getCameraController().setTarget(transform);

		this.transform = this.spawnEntity(0, 0, 0);
		this.spawnEntity(-2, 0, -2);
		this.spawnEntity(2, 0, -2);
		this.spawnEntity(2, 0, 2);
		this.spawnEntity(-2, 0, 2);
		this.spawnEntity(0, 2, 0);
		this.spawnEntity(0, -2, 0);

		this.sys_2D.start(this);
		this.sys_billboard.start(this);

		this.screenSpace = new ScreenSpace(OldGameEngine.WINDOW.getCurrentViewPort(), this.getRenderer().getCamera(), Direction.CENTER, true, false);
	}

	public Transform3 spawnEntity(float x, float y, float z)
	{
		final Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		this.entityManager.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, new OldModel(
						OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
						this.getMaterialManager().createMaterial(
								new OldPropertyDiffuse(),
								new OldPropertySpecular(),
								new OldPropertyShadow(true, true),
								new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny")).setTransparent(true)),
						"simple")),
				new EntityComponent2D());
		return transform;
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		float mouseX = OldInputManager.getInputAmount("mousex");
		float mouseY = OldInputManager.getInputAmount("mousey");
		this.screenSpace.getPointFromScreen(mouseX, mouseY, 0.99F, this.transform.position);

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		this.sys_2D.stop(this);
		this.sys_billboard.stop(this);
	}

	@Override
	public RenderGlim getRenderer()
	{
		return (RenderGlim) super.getRenderer();
	}

	@Override
	public BasicFirstPersonCameraController getCameraController()
	{
		return (BasicFirstPersonCameraController) super.getCameraController();
	}
}
