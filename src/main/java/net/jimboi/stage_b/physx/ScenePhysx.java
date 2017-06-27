package net.jimboi.stage_b.physx;

import net.jimboi.base.Main;
import net.jimboi.stage_b.glim.RendererGlim;
import net.jimboi.stage_b.glim.SceneGlimBase;
import net.jimboi.stage_b.glim.gameentity.component.GameComponent2D;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentInstance;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystem2D;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemBase;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemBillboard;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemInstance;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.qsilver.view.MousePicker;
import org.zilar.BasicTopDownCameraController;
import org.zilar.material.property.PropertyDiffuse;
import org.zilar.material.property.PropertyShadow;
import org.zilar.material.property.PropertySpecular;
import org.zilar.material.property.PropertyTexture;
import org.zilar.model.Model;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 6/18/17.
 */
public class ScenePhysx extends SceneGlimBase
{
	private EntitySystemInstance sys_instance;
	private EntitySystem2D sys_2D;
	private EntitySystemBillboard sys_billboard;

	private Transform3Quat player;
	private Transform3 transform;
	private MousePicker picker;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();
	}

	@Override
	protected void onSceneStart()
	{
		this.picker = new MousePicker(Main.WINDOW, RendererGlim.CAMERA);

		this.sys_instance = new EntitySystemInstance(this.entityManager, this.renderer.getInstanceManager());
		this.sys_2D = new EntitySystem2D(this.entityManager, this);

		this.sys_billboard = new EntitySystemBillboard(this.entityManager, this, RendererGlim.CAMERA);

		final Transform3Quat transform = (Transform3Quat) Transform3.create();
		this.player = this.spawnEntity(0, 0, 0);
		RendererGlim.CAMERA.setCameraController(new BasicTopDownCameraController(transform));

		this.transform = this.spawnEntity(0, 0, 0);
		this.spawnEntity(-2, 0, -2);
		this.spawnEntity(2, 0, -2);
		this.spawnEntity(2, 0, 2);
		this.spawnEntity(-2, 0, 2);
		this.spawnEntity(0, 2, 0);
		this.spawnEntity(0, -2, 0);
	}

	public Transform3Quat spawnEntity(float x, float y, float z)
	{
		final Transform3Quat transform = (Transform3Quat) Transform3.create();
		transform.setPosition(x, y, z);
		this.entityManager.createEntity(
				new GameComponentTransform(transform),
				new GameComponentInstance(transform, new Model(
						RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "plane"),
						RendererGlim.INSTANCE.getMaterialManager().createMaterial(
								new PropertyDiffuse(),
								new PropertySpecular(),
								new PropertyShadow(true, true),
								new PropertyTexture(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "bunny")).setTransparent(true)),
						"diffuse")),
				new GameComponent2D());
		return transform;
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		RendererGlim.CAMERA.update(delta);

		this.picker.update();
		this.transform.position.set(RendererGlim.CAMERA.getTransform().position);
		this.transform.position.add(this.picker.getMouseRay());

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		EntitySystemBase.stopAll();
	}
}
