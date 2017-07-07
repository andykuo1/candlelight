package net.jimboi.stage_b.physx;

import net.jimboi.stage_b.glim.RenderManagerGlim;
import net.jimboi.stage_b.glim.entity.component.EntityComponent2D;
import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_b.glim.entity.system.EntitySystem2D;
import net.jimboi.stage_b.glim.entity.system.EntitySystemBillboard;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.qsilver.view.MousePicker;
import org.zilar.BasicTopDownCameraController;
import org.zilar.base.GameEngine;
import org.zilar.base.SceneBase;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyShadow;
import org.zilar.property.PropertySpecular;
import org.zilar.property.PropertyTexture;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

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

	private Transform3Quat player;
	private Transform3 transform;
	private MousePicker picker;

	public ScenePhysx()
	{
		super(new RenderManagerGlim(new BasicTopDownCameraController()));
	}

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();
	}

	@Override
	protected void onSceneStart()
	{
		this.picker = new MousePicker(GameEngine.WINDOW, this.getRenderer().getCamera());

		this.sys_2D = new EntitySystem2D(this.entityManager);

		this.sys_billboard = new EntitySystemBillboard(this.entityManager, this.getRenderer().getCamera());

		final Transform3Quat transform = (Transform3Quat) Transform3.create();
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

	public Transform3Quat spawnEntity(float x, float y, float z)
	{
		final Transform3Quat transform = (Transform3Quat) Transform3.create();
		transform.setPosition(x, y, z);
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
		this.getRenderer().getCamera().update(delta);

		this.picker.update();
		this.transform.position.set(this.getRenderer().getCamera().getTransform().position);
		this.transform.position.add(this.picker.getMouseRay());

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		this.sys_2D.stop(this);
		this.sys_billboard.stop(this);
	}

	@Override
	public RenderManagerGlim getRenderer()
	{
		return (RenderManagerGlim) super.getRenderer();
	}
}
