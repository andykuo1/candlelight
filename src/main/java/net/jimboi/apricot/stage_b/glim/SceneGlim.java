package net.jimboi.apricot.stage_b.glim;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.OldSceneBase;
import net.jimboi.apricot.base.animation.AnimatorSpriteSheet;
import net.jimboi.apricot.base.assets.resource.MeshLoader;
import net.jimboi.apricot.base.assets.resource.TextureAtlasLoader;
import net.jimboi.apricot.base.entity.Entity;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.property.OldPropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.OldPropertyShadow;
import net.jimboi.apricot.base.renderer.property.OldPropertySpecular;
import net.jimboi.apricot.base.renderer.property.OldPropertyTexture;
import net.jimboi.apricot.base.renderer.shadow.DynamicLight;
import net.jimboi.apricot.stage_b.glim.bounding.BoundingManager;
import net.jimboi.apricot.stage_b.glim.entity.EntityBunny;
import net.jimboi.apricot.stage_b.glim.entity.EntityCrate;
import net.jimboi.apricot.stage_b.glim.entity.EntityGlim;
import net.jimboi.apricot.stage_b.glim.entity.EntityPlayer;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.apricot.stage_b.glim.entity.system.EntitySystemBillboard;
import net.jimboi.apricot.stage_b.glim.entity.system.EntitySystemBounding;
import net.jimboi.apricot.stage_b.glim.entity.system.EntitySystemHeading;
import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.sprite.SpriteSheet;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;
import net.jimboi.boron.base_ab.sprite.TextureAtlasBuilder;
import net.jimboi.boron.base_ab.sprite.TextureAtlasData;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform;
import org.bstone.transform.Transform3;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.zilar.meshbuilder.MeshData;

/**
 * Created by Andy on 6/1/17.
 */
public class SceneGlim extends OldSceneBase
{
	public static void main(String[] args)
	{
		OldGameEngine.init(SceneGlim.class, args);
		OldGameEngine.run();
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
		super(new RenderGlim(OldGameEngine.RENDERENGINE), new CameraControllerGlim());
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

		this.getCameraController().setTarget(this.player, this.world);

		Asset<Texture> font = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "font");

		TextureAtlasBuilder tab = new TextureAtlasBuilder(font, 256, 256);
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		TextureAtlasData atlas = tab.bake();
		tab.clear();
		Asset<TextureAtlas> textureAtlas = OldGameEngine.ASSETMANAGER.registerAsset(TextureAtlas.class, "font",
				new TextureAtlasLoader.TextureAtlasParameter(atlas));

		MeshData mesh = WorldGlim.createMeshFromMap(this.world.getMap(), textureAtlas);
		OldGameEngine.ASSETMANAGER.registerAsset(Mesh.class, "dungeon",
				new MeshLoader.MeshParameter(mesh));

		this.getEntityManager().createEntity(
				new EntityComponentRenderable(
						new Transform3(),
						new OldModel(OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "dungeon"),
								this.getMaterialManager().createMaterial(
										new OldPropertyDiffuse(),
										new OldPropertySpecular(),
										new OldPropertyShadow(true, true),
										new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "font"))),
								"diffuse"))
		);

		SpriteSheet spritesheet = new SpriteSheet(textureAtlas, 0, 256);
		this.getAnimationManager().start(new AnimatorSpriteSheet(spritesheet, 0.1F));

		this.shadowplane = this.getEntityManager().createEntity(
				new EntityComponentRenderable(
						new Transform3(),
						new OldModel(OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, "plane"),
								this.getMaterialManager().createMaterial(
										new OldPropertyDiffuse(),
										new OldPropertySpecular(),
										new OldPropertyShadow(true, true),
										new OldPropertyTexture(OldGameEngine.ASSETMANAGER.getAsset(Texture.class, "shadowmap"))),//spritesheet)),
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

		DynamicLight light = RenderGlim.LIGHTS.get(0);
		light.position = new Vector4f(transform.getPosition(new Vector3f()), 1);
		light.coneDirection.lerp(this.getRenderer().getCamera().transform().getForward(new Vector3f()), 0.2F);
		DynamicLight pt = RenderGlim.LIGHTS.get(1);
		pt.position = light.position;

		EntityComponentRenderable renderable = this.shadowplane.getComponent(EntityComponentRenderable.class);
		Vector3f position = renderable.getTransform().position;
		Vector3f next = this.getRenderer().getCamera().transform().getForward(new Vector3f()).mul(3).add(transform.getPosition(new Vector3f()));
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
	public CameraControllerGlim getCameraController()
	{
		return (CameraControllerGlim) super.getCameraController();
	}

	@Override
	public RenderGlim getRenderer()
	{
		return (RenderGlim) super.getRenderer();
	}
}
