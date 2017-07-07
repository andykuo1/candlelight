package net.jimboi.stage_b.glim;

import net.jimboi.stage_b.glim.bounding.BoundingManager;
import net.jimboi.stage_b.glim.entity.EntityBunny;
import net.jimboi.stage_b.glim.entity.EntityCrate;
import net.jimboi.stage_b.glim.entity.EntityGlim;
import net.jimboi.stage_b.glim.entity.EntityPlayer;
import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_b.glim.entity.system.EntitySystemBillboard;
import net.jimboi.stage_b.glim.entity.system.EntitySystemBounding;
import net.jimboi.stage_b.glim.entity.system.EntitySystemHeading;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.qsilver.asset.Asset;
import org.qsilver.entity.Entity;
import org.qsilver.transform.Transform;
import org.zilar.base.GameEngine;
import org.zilar.base.SceneBase;
import org.zilar.meshbuilder.MeshData;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyShadow;
import org.zilar.property.PropertySpecular;
import org.zilar.property.PropertyTexture;
import org.zilar.renderer.shadow.DynamicLight;
import org.zilar.resourceloader.MeshLoader;
import org.zilar.resourceloader.TextureAtlasLoader;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;
import org.zilar.sprite.TextureAtlasData;
import org.zilar.transform.Transform3;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends SceneBase
{
	public static void main(String[] args)
	{
		GameEngine.run(SceneGlim.class, args);
	}

	private EntitySystemBounding sys_bounding;
	private EntitySystemHeading sys_heading;
	private EntitySystemBillboard sys_billboard;

	private BoundingManager boundingManager;

	private WorldGlim world;
	private Entity player;
	private Entity shadowplane;

	public SceneGlim()
	{
		super(new RenderManagerGlim(new CameraControllerGlim()));
	}

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.boundingManager = new BoundingManager();
		this.getRenderer().setBoundingManager(this.boundingManager);
	}

	@Override
	protected void onSceneStart()
	{
		this.sys_bounding = new EntitySystemBounding(this.entityManager, this.boundingManager);
		this.sys_heading = new EntitySystemHeading(this.entityManager, this.boundingManager);
		this.sys_billboard = new EntitySystemBillboard(this.entityManager, this.getRenderer().getCamera());

		this.sys_bounding.start(this);
		this.sys_heading.start(this);
		this.sys_billboard.start(this);

		this.world = new WorldGlim(this.boundingManager);

		EntityGlim.setup(this, this.entityManager);
		this.player = EntityPlayer.create(this.world);

		((CameraControllerGlim) this.getRenderer().getCamera().getCameraController()).setTarget(this.player, this.world);

		Asset<Texture> font = GameEngine.ASSETMANAGER.getAsset(Texture.class, "font");

		TextureAtlasBuilder tab = new TextureAtlasBuilder();
		tab.addTileSheet(font, 0, 0, 16, 16, 0, 0, 16, 16);
		TextureAtlasData atlas = tab.bake();
		tab.clear();
		Asset<TextureAtlas> textureAtlas = GameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "font",
				new TextureAtlasLoader.TextureAtlasParameter(atlas));

		MeshData mesh = WorldGlim.createMeshFromMap(this.world.getMap(), textureAtlas);
		GameEngine.ASSETMANAGER.registerAsset(Mesh.class, "dungeon",
				new MeshLoader.MeshParameter(mesh));

		this.getEntityManager().createEntity(
				new EntityComponentRenderable(
						Transform3.create(),
						new Model(GameEngine.ASSETMANAGER.getAsset(Mesh.class, "dungeon"),
								this.getMaterialManager().createMaterial(
										new PropertyDiffuse(),
										new PropertySpecular(),
										new PropertyShadow(true, true),
										new PropertyTexture(GameEngine.ASSETMANAGER.getAsset(Texture.class, "font"))),
								"diffuse"))
		);

		this.shadowplane = this.getEntityManager().createEntity(
				new EntityComponentRenderable(
					Transform3.create(),
					new Model(GameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
							this.getMaterialManager().createMaterial(
									new PropertyDiffuse(),
									new PropertySpecular(),
									new PropertyShadow(true, true),
									new PropertyTexture(GameEngine.ASSETMANAGER.getAsset(Texture.class, "font"))),
							"billboard"))
		);

		EntityCrate.create(this.world, -4, 0, 0);
		EntityCrate.create(this.world, 20, 5, 30);

		EntityBunny.create(this.world, 10, 1, 14);
		EntityBunny.create(this.world, 10, 1, 16);
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		Transform transform = this.player.getComponent(EntityComponentTransform.class).transform;

		DynamicLight light = RenderManagerGlim.LIGHTS.get(0);
		light.position = new Vector4f(transform.position(), 1);
		light.coneDirection.lerp(this.getRenderer().getCamera().getTransform().getForward(new Vector3f()), 0.2F);
		DynamicLight pt = RenderManagerGlim.LIGHTS.get(1);
		pt.position = light.position;

		EntityComponentRenderable renderable = this.shadowplane.getComponent(EntityComponentRenderable.class);
		Vector3f position = renderable.getTransform().position;
		Vector3f next = this.getRenderer().getCamera().getTransform().getForward(new Vector3f()).mul(3).add(transform.position());
		position.lerp(next, (float) delta * 10);

		super.onSceneUpdate(delta);
	}

	@Override
	protected void onSceneStop()
	{
		this.sys_billboard.stop(this);
		this.sys_heading.stop(this);
		this.sys_bounding.stop(this);
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

	@Override
	public RenderManagerGlim getRenderer()
	{
		return (RenderManagerGlim) super.getRenderer();
	}
}
