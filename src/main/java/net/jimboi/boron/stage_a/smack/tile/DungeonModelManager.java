package net.jimboi.boron.stage_a.smack.tile;

import net.jimboi.apricot.base.renderer.property.PropertyTexture;

import org.bstone.material.Material;
import org.bstone.material.MaterialManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.model.Model;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.asset.Asset;
import org.qsilver.util.map2d.IntMap;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 8/7/17.
 */
public class DungeonModelManager
{
	private Asset<Texture> texture;
	private Asset<TextureAtlas> textureAtlas;
	private Set<Mesh> meshes = new HashSet<>();
	private String renderType;
	private Material material;

	public DungeonModelManager(MaterialManager materialManager, Asset<Texture> texture, Asset<TextureAtlas> textureAtlas, String renderType)
	{
		this.texture = texture;
		this.textureAtlas = textureAtlas;
		this.renderType = renderType;

		this.material = materialManager.createMaterial(new PropertyTexture(this.texture));
	}

	public void destroy()
	{
		for(Mesh mesh : this.meshes)
		{
			mesh.close();
		}

		this.meshes.clear();
	}

	public Model createStaticDungeon(IntMap tiles)
	{
		Mesh mesh = ModelUtil.createStaticMesh(createMeshFromMap(tiles, this.textureAtlas));
		this.meshes.add(mesh);
		return new Model(Asset.wrap(mesh), this.material, this.renderType);
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
