package net.jimboi.apricot.stage_c.hoob;

import net.jimboi.apricot.base.OldGameEngine;
import net.jimboi.apricot.base.OldSceneBase;
import net.jimboi.apricot.base.animation.AnimatorSpriteSheet;
import net.jimboi.apricot.base.collision.CollisionManager;
import net.jimboi.apricot.base.collision.Shape;
import net.jimboi.apricot.base.controller.BasicSideScrollCameraController;
import net.jimboi.apricot.base.entity.Entity;
import net.jimboi.apricot.base.material.OldMaterial;
import net.jimboi.apricot.base.render.OldModel;
import net.jimboi.apricot.base.renderer.property.OldPropertyDiffuse;
import net.jimboi.apricot.base.renderer.property.OldPropertyTexture;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.apricot.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.apricot.stage_c.hoob.world.World;
import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.sprite.SpriteSheet;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;

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
