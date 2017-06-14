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
import net.jimboi.stage_b.glim.system.EntitySystemBounding;
import net.jimboi.stage_b.glim.system.EntitySystemInstance;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyDiffuse;
import net.jimboi.stage_b.gnome.material.property.PropertyShadow;
import net.jimboi.stage_b.gnome.material.property.PropertySpecular;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.meshbuilder.MeshData;
import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.transform.Transform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends SceneGlimBase
{
	public static int MATERIAL_ID = -1;

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

		MeshData mesh = RendererGlim.createMeshFromMap(this.world.getMap(), new TiledTextureAtlas(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "font").getSource(),
				16, 16));
		RendererGlim.INSTANCE.getAssetManager().registerAsset(Mesh.class, "dungeon",
				new MeshLoader.MeshParameter(mesh));

		this.renderer.getInstanceManager().add(new Instance(new Model(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "dungeon"),
				RendererGlim.INSTANCE.getMaterialManager().createMaterial(
						new PropertyDiffuse(),
						new PropertySpecular(),
						new PropertyShadow(false, true),
						new PropertyTexture(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "font"))),
				"diffuse")));

		this.renderer.getInstanceManager().add(this.plane = new Instance(new Model(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "plane"),
				RendererGlim.INSTANCE.getMaterialManager().createMaterial(
						new PropertyDiffuse(),
						new PropertySpecular(),
						new PropertyShadow(true, true),
						new PropertyTexture(
								RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "shadowmap"))),
				"billboard")));

		EntityCrate.create(this.world, -4, 0, 0);
		EntityCrate.create(this.world, 20, 5, 30);

		//EntityBunny.create(this.world, 10, 1, 14);
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
