package net.jimboi.stage_b.glim;

import net.jimboi.stage_a.dood.system.EntitySystem;
import net.jimboi.stage_a.mod.sprite.TiledTextureAtlas;
import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.entity.EntityCrate;
import net.jimboi.stage_b.glim.entity.EntityGlim;
import net.jimboi.stage_b.glim.entity.EntityPlayer;
import net.jimboi.stage_b.glim.gameentity.GameEntity;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.glim.resourceloader.MeshLoader;
import net.jimboi.stage_b.glim.resourceloader.ModelLoader;
import net.jimboi.stage_b.glim.system.EntitySystemBounding;
import net.jimboi.stage_b.glim.system.EntitySystemInstance;
import net.jimboi.stage_b.gnome.asset.Asset;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.transform.Transform;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.model.Model;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends SceneGlimBase
{
	private EntitySystemInstance sys_instance;
	private EntitySystemBounding sys_bounding;

	private BoundingManager boundingManager;

	private WorldGlim world;
	private GameEntity player;
	private Instance plane;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.boundingManager = new BoundingManager();
	}

	@Override
	protected void onSceneStart()
	{
		this.sys_instance = new EntitySystemInstance(this.entityManager, this.renderer.getInstanceManager());
		this.sys_bounding = new EntitySystemBounding(this.entityManager, this, this.boundingManager);

		this.world = new WorldGlim(this.boundingManager);

		EntityGlim.setEntityManager(this.entityManager);
		this.player = EntityPlayer.create(this.world);
		RendererGlim.CAMERA.setCameraController(new CameraControllerGlim(this.player, this.world));

		Mesh mesh = RendererGlim.createMeshFromMap(this.world.getMap(), new TiledTextureAtlas(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "atlas").getSource(),
				16, 16));
		Asset<Mesh> msh_dungeon = RendererGlim.INSTANCE.getAssetManager().registerAsset(Mesh.class, "dungeon",
				new MeshLoader.MeshParameter(mesh));
		Asset<Model> mdl_dungeon = RendererGlim.INSTANCE.getAssetManager().registerAsset(Model.class, "dungeon",
				new ModelLoader.ModelParameter(msh_dungeon));
		this.renderer.getInstanceManager().add(new Instance(
				mdl_dungeon,
				RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, "font"),
				"diffuse"));

		this.renderer.getInstanceManager().add(this.plane = new Instance(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Model.class, "plane"),
				RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, "plane"),
				"billboard"));

		EntityCrate.create(this.world, -4, 0, 0);
		EntityCrate.create(this.world, 20, 5, 30);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		RendererGlim.CAMERA.update(delta);

		Transform transform = this.player.getComponent(GameComponentTransform.class).transform;

		GlimLight light = RendererGlim.LIGHTS.get(0);
		light.position = new Vector4f(transform.position(), 1);
		light.coneDirection.lerp(RendererGlim.CAMERA.getTransform().getForward(new Vector3f()), 0.2F);
		GlimLight pt = RendererGlim.LIGHTS.get(1);
		pt.position = light.position;

		Transform playerTransform = player.getComponent(GameComponentTransform.class).transform;
		this.plane.transformation().translation(playerTransform.position());
		this.plane.transformation().translate(RendererGlim.CAMERA.getTransform().getForward(new Vector3f()).mul(3));

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		EntitySystem.stopAll();
	}

	public GameEntity getPlayer()
	{
		return this.player;
	}

	public WorldGlim getWorld()
	{
		return this.world;
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}
}
