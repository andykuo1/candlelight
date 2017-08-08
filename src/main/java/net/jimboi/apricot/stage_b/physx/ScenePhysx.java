package net.jimboi.apricot.stage_b.physx;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.OldSceneBase;
import net.jimboi.apricot.base.input.OldInputManager;
import net.jimboi.apricot.base.renderer.property.PropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.PropertyShadow;
import net.jimboi.apricot.base.renderer.property.PropertySpecular;
import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.apricot.stage_b.glim.RenderGlim;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponent2D;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.apricot.stage_b.glim.entity.system.EntitySystem2D;
import net.jimboi.apricot.stage_b.glim.entity.system.EntitySystemBillboard;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.model.Model;
import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.view.ScreenSpace;
import org.zilar.BasicFirstPersonCameraController;

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
				new EntityComponentRenderable(transform, new Model(
						OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
						this.getMaterialManager().createMaterial(
								new PropertyDiffuse(),
								new PropertySpecular(),
								new PropertyShadow(true, true),
								new PropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny")).setTransparent(true)),
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
