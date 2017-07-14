package net.jimboi.stage_c.hoob;

import net.jimboi.stage_b.glim.entity.component.EntityComponentRenderable;
import net.jimboi.stage_b.glim.entity.component.EntityComponentTransform;
import net.jimboi.stage_c.hoob.world.World;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.qsilver.asset.Asset;
import org.qsilver.entity.Entity;
import org.zilar.animation.AnimatorSpriteSheet;
import org.zilar.base.GameEngine;
import org.zilar.base.SceneBase;
import org.zilar.model.Model;
import org.zilar.property.PropertyDiffuse;
import org.zilar.property.PropertyTexture;
import org.zilar.sprite.SpriteSheet;
import org.zilar.sprite.TextureAtlas;
import org.zilar.transform.Transform3;
import org.zilar.transform.Transform3Quat;

/**
 * Created by Andy on 6/25/17.
 */
public class SceneHoob extends SceneBase
{
	public static void main(String[] args)
	{
		GameEngine.run(SceneHoob.class, args);
	}

	public SceneHoob()
	{
		super(new RenderHoob());
	}

	private World world;

	@Override
	protected void onSceneStart()
	{
		this.spawnEntity(0, 0, -0.1F, "crate", "ground", null);

		Transform3 transform = Transform3.create();
		transform.setPosition(0, 0, 1);
		this.getRenderer().getCameraController().setTarget((Transform3Quat) transform);

		this.world = new World(this);
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
	}

	@Override
	public RenderHoob getRenderer()
	{
		return (RenderHoob) super.getRenderer();
	}

	public Entity spawnEntity(float x, float y, float z, String textureID, String meshID, String textureAtlasID)
	{
		Transform3 transform = Transform3.create();
		transform.setPosition(x, y, z);
		Asset<Texture> texture = GameEngine.ASSETMANAGER.getAsset(Texture.class, textureID);

		SpriteSheet spritesheet = null;
		if (textureAtlasID != null)
		{
			Asset<TextureAtlas> textureAtlas = GameEngine.ASSETMANAGER.getAsset(TextureAtlas.class, textureAtlasID);
			spritesheet = new SpriteSheet(textureAtlas, 0, 3);
			this.getAnimationManager().start(new AnimatorSpriteSheet(spritesheet, 0.2F));
		}

		Asset<Mesh> mesh = GameEngine.ASSETMANAGER.getAsset(Mesh.class, meshID);
		Material material = this.getMaterialManager().createMaterial(
				new PropertyDiffuse(),
				spritesheet != null ? new PropertyTexture(spritesheet) : new PropertyTexture(texture)
		);
		Model model = new Model(mesh, material, "simple");
		return this.getEntityManager().createEntity(
				new EntityComponentTransform(transform),
				new EntityComponentRenderable(transform, model)
		);
	}
}
