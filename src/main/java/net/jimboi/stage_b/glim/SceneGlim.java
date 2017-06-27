package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.entity.EntityBunny;
import net.jimboi.stage_b.glim.entity.EntityCrate;
import net.jimboi.stage_b.glim.entity.EntityGlim;
import net.jimboi.stage_b.glim.entity.EntityPlayer;
import net.jimboi.stage_b.glim.gameentity.component.GameComponentTransform;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemBase;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemBillboard;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemBounding;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemHeading;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemInstance;
import net.jimboi.stage_b.glim.gameentity.system.EntitySystemSprite;
import net.jimboi.stage_b.glim.resourceloader.MeshLoader;
import net.jimboi.stage_b.gnome.instance.Instance;
import net.jimboi.stage_b.gnome.material.property.PropertyDiffuse;
import net.jimboi.stage_b.gnome.material.property.PropertyShadow;
import net.jimboi.stage_b.gnome.material.property.PropertySpecular;
import net.jimboi.stage_b.gnome.material.property.PropertyTexture;
import net.jimboi.stage_b.gnome.meshbuilder.MeshData;
import net.jimboi.stage_b.gnome.model.Model;
import net.jimboi.stage_b.gnome.sprite.TiledTextureAtlas;
import net.jimboi.stage_b.gnome.transform.Transform;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.entity.Entity;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends SceneGlimBase
{
	private EntitySystemInstance sys_instance;
	private EntitySystemBounding sys_bounding;
	private EntitySystemSprite sys_sprite;
	private EntitySystemHeading sys_heading;
	private EntitySystemBillboard sys_billboard;

	private BoundingManager boundingManager;

	private WorldGlim world;
	private Entity player;
	private Instance plane;

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.boundingManager = new BoundingManager();
		this.renderer.setBoundingManager(this.boundingManager);
	}

	@Override
	protected void onSceneStart()
	{
		this.sys_instance = new EntitySystemInstance(this.entityManager, this.renderer.getInstanceManager());
		this.sys_bounding = new EntitySystemBounding(this.entityManager, this, this.boundingManager);
		this.sys_sprite = new EntitySystemSprite(this.entityManager, this);
		this.sys_heading = new EntitySystemHeading(this.entityManager, this, this.boundingManager, RendererGlim.INSTANCE.getInstanceManager());
		this.sys_billboard = new EntitySystemBillboard(this.entityManager, this, RendererGlim.CAMERA);

		this.world = new WorldGlim(this.boundingManager);

		EntityGlim.setEntityManager(this.entityManager);
		this.player = EntityPlayer.create(this.world);
		RendererGlim.CAMERA.setCameraController(new CameraControllerGlim(this.player, this.world));

		TiledTextureAtlas textureAtlas = new TiledTextureAtlas(RendererGlim.INSTANCE.getAssetManager().getAsset(Texture.class, "font"), 16, 16);
		MeshData mesh = RendererGlim.createMeshFromMap(this.world.getMap(), textureAtlas);
		RendererGlim.INSTANCE.getAssetManager().registerAsset(Mesh.class, "dungeon",
				new MeshLoader.MeshParameter(mesh));

		this.renderer.getInstanceManager().add(new Instance(new Model(
				RendererGlim.INSTANCE.getAssetManager().getAsset(Mesh.class, "dungeon"),
				RendererGlim.INSTANCE.getMaterialManager().createMaterial(
						new PropertyDiffuse(),
						new PropertySpecular(),
						new PropertyShadow(true, true),
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

		EntityBunny.create(this.world, 10, 1, 14);
		EntityBunny.create(this.world, 10, 1, 16);
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
		EntitySystemBase.stopAll();
	}

	public Entity getPlayer()
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
