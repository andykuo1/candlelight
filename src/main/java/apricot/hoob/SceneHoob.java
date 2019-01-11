package apricot.hoob;

import apricot.base.OldGameEngine;
import apricot.base.OldSceneBase;
import apricot.base.animation.AnimatorSpriteSheet;
import apricot.base.collision.CollisionManager;
import apricot.base.collision.Shape;
import apricot.base.controller.BasicSideScrollCameraController;
import apricot.base.entity.Entity;
import apricot.base.material.OldMaterial;
import apricot.base.render.OldModel;
import apricot.base.renderer.property.OldPropertyDiffuse;
import apricot.base.renderer.property.OldPropertyTexture;
import apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import apricot.stage_b.glim.entity.component.EntityComponentTransform;
import apricot.hoob.world.World;
import apricot.base.asset.Asset;
import apricot.base.sprite.SpriteSheet;
import apricot.base.sprite.TextureAtlas;

import apricot.bstone.mogli.Mesh;
import apricot.bstone.mogli.Texture;
import apricot.bstone.transform.Transform3;

/**
 * Created by Andy on 6/25/17.
 */
public class SceneHoob extends OldSceneBase
{
	//TODO: There is a leak with meshes...
	public static void main(String[] args)
	{
		OldGameEngine.init(SceneHoob.class, args);
		OldGameEngine.run();
	}

	protected EntitySystemBoundingCollider systemBoundingCollider;
	protected CollisionManager collisionManager;

	protected World world;

	public SceneHoob()
	{
		super(new RenderHoob(OldGameEngine.RENDERENGINE), new BasicSideScrollCameraController());
	}

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.collisionManager = new CollisionManager();
		this.systemBoundingCollider = new EntitySystemBoundingCollider(this.entityManager, this.collisionManager);

		this.world = new World(this);
	}

	@Override
	protected void onSceneStart()
	{
		this.systemBoundingCollider.start(this);

		this.spawnEntity(0, 0, -0.1F, "crate", "ground");

		Transform3 transform = new Transform3();
		transform.position.set(0, 0, 1);
		this.getCameraController().setTarget(transform);

		this.world.create();
	}

	@Override
	protected void onSceneUpdate(double delta)
	{
		super.onSceneUpdate(delta);

		this.world.update(delta);
	}

	@Override
	protected void onSceneStop()
	{
		super.onSceneStop();

		this.systemBoundingCollider.stop(this);
	}

	@Override
	public RenderHoob getRenderer()
	{
		return (RenderHoob) super.getRenderer();
	}

	public Entity spawnEntity(float x, float y, float z, String textureID, String meshID)
	{
		return this.spawnEntity(x, y, z, textureID, meshID, false, null, false);
	}

	public Entity spawnEntity(float x, float y, float z, String textureID, String meshID, boolean textureAtlas, Shape shape, boolean collider)
	{
		Transform3 transform = new Transform3();
		transform.position.set(x, y, z);
		Asset<Texture> texture = null;
		SpriteSheet spritesheet = null;

		if (textureAtlas)
		{
			Asset<TextureAtlas> atlas = OldGameEngine.ASSETMANAGER.getAsset(TextureAtlas.class, textureID);
			spritesheet = new SpriteSheet(atlas, 0, 3);
			this.getAnimationManager().start(new AnimatorSpriteSheet(spritesheet, 0.2F));
		}
		else
		{
			texture = OldGameEngine.ASSETMANAGER.getAsset(Texture.class, textureID);
		}

		Asset<Mesh> mesh = OldGameEngine.ASSETMANAGER.getAsset(Mesh.class, meshID);
		OldMaterial material = this.getMaterialManager().createMaterial(
				new OldPropertyDiffuse(),
				spritesheet != null ? new OldPropertyTexture(spritesheet) : new OldPropertyTexture(texture)
		);
		OldModel model = new OldModel(mesh, material, "simple");
		return this.getEntityManager().createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, model),
				shape == null ? null : collider ? new EntityComponentBoundingCollider(shape) : new EntityComponentBoundingStatic(shape)
		);
	}

	public CollisionManager getCollisionManager()
	{
		return this.collisionManager;
	}

	public World getWorld()
	{
		return this.world;
	}

	@Override
	public BasicSideScrollCameraController getCameraController()
	{
		return (BasicSideScrollCameraController) super.getCameraController();
	}
}
