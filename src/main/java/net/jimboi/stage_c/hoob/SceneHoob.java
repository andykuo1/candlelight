package net.jimboi.stage_c.hoob;

import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_c.hoob.world.World;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.qsilver.asset.Asset;
import org.qsilver.entity.Entity;
import org.zilar.animation.AnimatorSpriteSheet;
import org.zilar.base.GameEngine;
import org.zilar.base.SceneBase;
import org.zilar.bounding.BoundingManager;
import org.zilar.bounding.Shape;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyTexture;
import org.zilar.sprite.SpriteSheet;
import org.zilar.sprite.TextureAtlas;

/**
 * Created by Andy on 6/25/17.
 */
public class SceneHoob extends SceneBase
{
	public static void main(String[] args)
	{
		GameEngine.run(SceneHoob.class, args);
	}

	protected EntitySystemBoundingCollider systemBoundingCollider;
	protected BoundingManager boundingManager;

	protected World world;

	public SceneHoob()
	{
		super(new RenderHoob());
	}

	@Override
	protected void onSceneCreate()
	{
		super.onSceneCreate();

		this.boundingManager = new BoundingManager();
		this.systemBoundingCollider = new EntitySystemBoundingCollider(this.entityManager, this.boundingManager);

		this.world = new World(this);
	}

	@Override
	protected void onSceneStart()
	{
		this.systemBoundingCollider.start(this);

		this.spawnEntity(0, 0, -0.1F, "crate", "ground");

		Transform3 transform = new Transform3();
		transform.position.set(0, 0, 1);
		this.getRenderer().getCameraController().setTarget(transform);

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
			Asset<TextureAtlas> atlas = GameEngine.ASSETMANAGER.getAsset(TextureAtlas.class, textureID);
			spritesheet = new SpriteSheet(atlas, 0, 3);
			this.getAnimationManager().start(new AnimatorSpriteSheet(spritesheet, 0.2F));
		}
		else
		{
			texture = GameEngine.ASSETMANAGER.getAsset(Texture.class, textureID);
		}

		Asset<Mesh> mesh = GameEngine.ASSETMANAGER.getAsset(Mesh.class, meshID);
		Material material = this.getMaterialManager().createMaterial(
				new PropertyDiffuse(),
				spritesheet != null ? new PropertyTexture(spritesheet) : new PropertyTexture(texture)
		);
		Model model = new Model(mesh, material, "simple");
		return this.getEntityManager().createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, model),
				shape == null ? null : collider ? new EntityComponentBoundingCollider(shape) : new EntityComponentBoundingStatic(shape)
		);
	}

	public BoundingManager getBoundingManager()
	{
		return this.boundingManager;
	}

	public World getWorld()
	{
		return this.world;
	}
}
