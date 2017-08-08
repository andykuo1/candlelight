package net.jimboi.boron.stage_a.candle.world;

import net.jimboi.apricot.base.renderer.property.PropertyTexture;
import net.jimboi.boron.stage_a.candle.Candle;
import net.jimboi.boron.stage_a.candle.CandleRenderer;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderableBase;
import org.bstone.render.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.asset.Asset;
import org.qsilver.resource.MeshLoader;
import org.qsilver.util.map2d.IntMap;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;

import java.util.Random;

/**
 * Created by Andy on 7/29/17.
 */
public class DungeonHandler
{
	private WorldCandle world;
	private Asset<Texture> texture;
	private Asset<TextureAtlas> textureAtlas;

	private IntMap tilemap = new IntMap(16, 16);
	private MeshData meshdata;

	public DungeonHandler(WorldCandle world, Asset<Texture> texture, Asset<TextureAtlas> textureAtlas)
	{
		this.world = world;
		this.texture = texture;
		this.textureAtlas = textureAtlas;

		Random rand = new Random();
		for(int i = 0; i < this.tilemap.size(); ++i)
		{
			this.tilemap.set(i, 0);
		}
		this.tilemap.set(4, 4, 1);
	}

	public boolean intersects(float x, float y)
	{
		if (x < 0 || y < 0) return true;
		return this.tilemap.get((int) x, (int) y) == 1;
	}

	private Model model;

	public void updateMesh(CandleRenderer renderer)
	{
		final MeshData meshdata = this.getMeshData();
		if (this.model == null)
		{
			Asset<Mesh> mesh = Candle.getCandle().getAssetManager().registerAsset(Mesh.class, "world", new MeshLoader.MeshParameter(meshdata, false));
			this.model = new Model(mesh, this.world.getScene().getMaterialManager().createMaterial(
					new PropertyTexture(this.texture)
			), "simple");

			renderer.addCustomRenderable(new RenderableBase(this.model, new Matrix4f().translation(0, 0, -1)));
		}
	}

	public MeshData getMeshData()
	{
		if (this.meshdata == null)
		{
			this.meshdata = createMeshFromMap(this.tilemap, this.textureAtlas);
		}
		return this.meshdata;
	}

	private static MeshData createMeshFromMap(IntMap map, Asset<TextureAtlas> textureAtlas)
	{
		MeshBuilder mb = new MeshBuilder();

		Vector2f texTopLeft = new Vector2f();
		Vector2f texBotRight = new Vector2f(1, 1);

		Sprite sprite;
		Vector2f vec = new Vector2f();

		for (int x = 0; x < map.width; ++x)
		{
			for (int y = 0; y < map.height; ++y)
			{
				Vector2fc wallPos = new Vector2f(x, y);

				int tile = map.get(x, y);
				sprite = textureAtlas.getSource().getSprite(tile);
				texTopLeft.set(sprite.getU(), sprite.getV());
				texBotRight.set(texTopLeft).add(sprite.getWidth(), sprite.getHeight());

				mb.addPlane(wallPos, wallPos.add(1, 1, vec), 0, texTopLeft, texBotRight);
			}
		}

		return mb.bake(false, false);
	}
}
