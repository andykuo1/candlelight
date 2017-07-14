package net.jimboi.stage_b.physx;

import net.jimboi.stage_b.glim.RenderGlim;
import net.jimboi.stage_b.glim.entity.component.EntityComponent2D;
import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_b.glim.entity.system.EntitySystem2D;
import net.jimboi.stage_b.glim.entity.system.EntitySystemBillboard;

import org.bstone.camera.Camera;
import org.bstone.input.InputManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.zilar.BasicTopDownCameraController;
import org.zilar.base.GameEngine;
import org.zilar.base.SceneBase;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyShadow;
import org.zilar.property.PropertySpecular;
import org.zilar.property.PropertyTexture;

/**
 * Created by Andy on 6/18/17.
 */
public class ScenePhysx extends SceneBase
{
	public static void main(String[] args)
	{
		GameEngine.run(ScenePhysx.class, args);
	}

	private EntitySystem2D sys_2D;
	private EntitySystemBillboard sys_billboard;

	private Transform3 player;
	private Transform3 transform;

	public ScenePhysx()
	{
		super(new RenderGlim(new BasicTopDownCameraController()));
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
		((BasicTopDownCameraController) this.getRenderer().getCamera().getCameraController()).setTarget(transform);

		this.transform = this.spawnEntity(0, 0, 0);
		this.spawnEntity(-2, 0, -2);
		this.spawnEntity(2, 0, -2);
		this.spawnEntity(2, 0, 2);
		this.spawnEntity(-2, 0, 2);
		this.spawnEntity(0, 2, 0);
		this.spawnEntity(0, -2, 0);

		this.sys_2D.start(this);
		this.sys_billboard.start(this);
	}

	public Transform3 spawnEntity(float x, float y, float z)
	{
		final Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		this.entityManager.createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, new Model(
						GameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
						this.getMaterialManager().createMaterial(
								new PropertyDiffuse(),
								new PropertySpecular(),
								new PropertyShadow(true, true),
								new PropertyTexture(GameEngine.ASSETMANAGER.getAsset(Texture.class, "bunny")).setTransparent(true)),
						"simple")),
				new EntityComponent2D());
		return transform;
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		Camera camera = this.getRenderer().getCamera();
		camera.update(delta);

		float mouseX = InputManager.getInputAmount("mousex");
		float mouseY = InputManager.getInputAmount("mousey");
		Camera.getWorldFromScreen(camera, GameEngine.WINDOW.getCurrentViewPort(), mouseX, mouseY, 0.99F, this.transform.position);

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
}
