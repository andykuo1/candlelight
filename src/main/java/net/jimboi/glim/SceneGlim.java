package net.jimboi.glim;

import net.jimboi.dood.system.EntitySystem;
import net.jimboi.glim.bounding.BoundingManager;
import net.jimboi.glim.entity.EntityCrate;
import net.jimboi.glim.entity.EntityGlim;
import net.jimboi.glim.entity.EntityPlayer;
import net.jimboi.glim.gameentity.GameEntity;
import net.jimboi.glim.gameentity.component.GameComponentTransform;
import net.jimboi.glim.resourceloader.MeshLoader;
import net.jimboi.glim.resourceloader.ModelLoader;
import net.jimboi.glim.system.EntitySystemBounding;
import net.jimboi.glim.system.EntitySystemInstance;
import net.jimboi.mod.Light;
import net.jimboi.mod.instance.Instance;
import net.jimboi.mod2.asset.Asset;
import net.jimboi.mod2.sprite.TiledTextureAtlas;
import net.jimboi.mod2.transform.Transform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.material.Material;
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
	protected void onSceneStart()
	{
		this.boundingManager = new BoundingManager();

		this.sys_instance = new EntitySystemInstance(this.entityManager, this.renderer.getInstanceManager());
		this.sys_bounding = new EntitySystemBounding(this.entityManager, this, this.boundingManager, this.renderer.getInstanceManager());

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
				mdl_dungeon.getSource(),
				RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, "font").getSource(),
				"diffuse"));

		this.renderer.getInstanceManager().add(this.plane = new Instance(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Model.class, "plane").getSource(),
				RendererGlim.INSTANCE.getAssetManager().getAsset(Material.class, "plane").getSource(),
				"billboard"));

		EntityCrate.create(this.world, -4, 0, 0);
		EntityCrate.create(this.world, 20, 5, 30);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		RendererGlim.CAMERA.update(delta);

		Transform transform = this.player.getComponent(GameComponentTransform.class).transform;

		Light light = RendererGlim.LIGHTS.get(0);
		light.position = new Vector4f(transform.position(), 1);
		light.coneDirection.lerp(RendererGlim.CAMERA.getTransform().getForward(new Vector3f()), 0.2F);
		Light pt = RendererGlim.LIGHTS.get(1);
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
}
