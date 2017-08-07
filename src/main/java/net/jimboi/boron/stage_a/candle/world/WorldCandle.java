package net.jimboi.boron.stage_a.candle.world;

import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.boron.base.WorldBase;
import net.jimboi.boron.stage_a.base.livingentity.EntityComponentRenderable;
import net.jimboi.boron.stage_a.candle.Candle;
import net.jimboi.boron.stage_a.candle.SceneCandle;
import net.jimboi.boron.stage_a.candle.entity.EntityCandleBase;
import net.jimboi.boron.stage_a.candle.entity.EntityPlayer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.Model;
import org.bstone.transform.Transform3;
import org.qsilver.scene.Scene;
import org.zilar.sprite.SpriteSheet;
import org.zilar.sprite.TextureAtlas;

/**
 * Created by Andy on 7/29/17.
 */
public class WorldCandle extends WorldBase
{
	private DungeonHandler dungeonHandler;
	private EntityPlayer player;

	public WorldCandle(SceneCandle scene)
	{
		super(scene);
	}

	@Override
	protected void setupWorld()
	{
		this.dungeonHandler = new DungeonHandler(this,
				Candle.getCandle().getAssetManager().getAsset(Texture.class, "font"),
				Candle.getCandle().getAssetManager().getAsset(TextureAtlas.class, "font"));

		Transform3 transform = new Transform3();
		transform.setPosition(0, 0, 1);
		this.player = new EntityPlayer(this, transform, this.createRenderable(transform, "bunny"));
		this.spawn(this.player);
	}

	@Override
	public void onSceneUpdate(Scene scene)
	{
	}

	public EntityCandleBase spawn(EntityCandleBase living)
	{
		return this.getScene().spawn(living);
	}

	public EntityCandleBase spawn(float x, float y, String textureID)
	{
		Transform3 transform = new Transform3();
		transform.setPosition(x, y, 0.0F);
		return this.getScene().spawn(new EntityCandleBase(this, transform,
				this.createRenderable(transform, textureID)));
	}

	public EntityCandleBase spawn(float x, float y, SpriteSheet spritesheet)
	{
		Transform3 transform = new Transform3();
		transform.setPosition(x, y, 0.0F);
		return this.getScene().spawn(new EntityCandleBase(this, transform,
				this.createAnimatedRenderable(transform, spritesheet)));
	}

	public EntityComponentRenderable createRenderable(Transform3 transform, String textureID)
	{
		return new EntityComponentRenderable(transform,
				new Model(Candle.getCandle().getAssetManager().getAsset(Mesh.class, "2dc"),
						this.getScene().getMaterialManager().createMaterial(
								new PropertyTexture(Candle.getCandle().getAssetManager().getAsset(Texture.class, textureID)).setTransparent(true)
						), "simple"));
	}

	public EntityComponentRenderable createAnimatedRenderable(Transform3 transform, SpriteSheet spritesheet)
	{
		return new EntityComponentRenderable(transform,
				new Model(Candle.getCandle().getAssetManager().getAsset(Mesh.class, "2dc"),
						this.getScene().getMaterialManager().createMaterial(
								new PropertyTexture(spritesheet).setTransparent(true)
						), "simple"));
	}

	@Override
	public SceneCandle getScene()
	{
		return (SceneCandle) super.getScene();
	}

	public DungeonHandler getDungeonHandler()
	{
		return this.dungeonHandler;
	}
}
