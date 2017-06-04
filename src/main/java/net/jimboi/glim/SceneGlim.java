package net.jimboi.glim;

import net.jimboi.dood.system.EntitySystem;
import net.jimboi.glim.bounding.BoundingManager;
import net.jimboi.glim.entity.EntityCrate;
import net.jimboi.glim.entity.EntityGlim;
import net.jimboi.glim.entity.EntityPlayer;
import net.jimboi.glim.system.EntitySystemBounding;
import net.jimboi.glim.system.EntitySystemInstance;
import net.jimboi.mod.Light;

import org.bstone.mogli.Mesh;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.entity.Entity;
import org.qsilver.render.Instance;
import org.qsilver.render.Model;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends SceneGlimBase
{
	private EntitySystemInstance sys_instance;
	private EntitySystemBounding sys_bounding;

	private BoundingManager boundingManager;

	private WorldGlim world;
	private Entity player;

	@Override
	protected void onSceneStart()
	{
		this.boundingManager = new BoundingManager();

		this.sys_instance = new EntitySystemInstance(this.entityManager, this.renderer.getInstanceManager());
		this.sys_bounding = new EntitySystemBounding(this.entityManager, this, this.boundingManager, this.renderer.getInstanceManager());

		this.world = new WorldGlim(this.boundingManager);

		EntityGlim.setEntityManager(this.entityManager);
		this.player = EntityPlayer.create(this.world);
		RendererGlim.CAMERA.setCameraController(new CameraControllerGlim(this.player, this.world));

		RendererGlim.LIGHTS.add(Light.createSpotLight(0, 0, 0, 0xFFFFFF, 1F, 0.1F, 0, 30F, 1, 1, 1));
		RendererGlim.LIGHTS.add(Light.createDirectionLight(1, 1F, 1F, 0xFFFFFF, 0.1F, 0.06F));

		Mesh mesh = RendererGlim.createMeshFromMap(this.world.getMap());
		RendererGlim.register("mesh.dungeon", mesh);
		RendererGlim.register("model.dungeon", new Model(mesh));
		this.renderer.getInstanceManager().add(new Instance(RendererGlim.get("model.dungeon"), RendererGlim.get("material.crate")));

		this.renderer.getInstanceManager().add(new Instance(RendererGlim.get("model.box"), RendererGlim.get("material.bird")));

		EntityCrate.create(this.world, -4, 0, 0);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		RendererGlim.CAMERA.update(delta);

		Light light = RendererGlim.LIGHTS.get(0);
		light.position = new Vector4f(RendererGlim.CAMERA.getTransform().position, 1);
		light.coneDirection = RendererGlim.CAMERA.getTransform().getForward(new Vector3f());

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		EntitySystem.stopAll();
	}

	public Entity getPlayer()
	{
		return this.player;
	}

	public WorldGlim getWorld()
	{
		return this.world;
	}
}
