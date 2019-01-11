package canary.smuc;

import canary.base.MaterialProperty;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.material.Material;
import canary.bstone.mogli.Mesh;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import canary.bstone.util.ColorUtil;
import canary.bstone.util.function.TriConsumer;
import canary.bstone.util.grid.IntMap;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import canary.zilar.meshbuilder.MeshBuilder;
import canary.zilar.meshbuilder.MeshData;
import canary.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 11/21/17.
 */
public class RasterizedView
{
	private final int width;
	private final int height;

	private final IntMap colors;
	private final IntMap glyphs;
	private boolean dirty;

	private Asset<Mesh> mesh;
	private Asset<TextureAtlas> textureAtlas;
	private final Material material = new Material();

	public RasterizedView(int width, int height)
	{
		this.width = width;
		this.height = height;

		this.colors = new IntMap(this.width, this.height);
		this.glyphs = new IntMap(this.width, this.height);

		this.forEach((vec, character, integer) ->
				this.draw(vec.x(), vec.y(), 'T', 0xFFFFFF));

		for(int i = 0; i < this.width; ++i)
		{
			this.color(i, 1, 0xFFFFFF);
			this.glyph(i, 1, 'e');
		}
	}

	public RasterizedView setTextureAtlas(Asset<TextureAtlas> textureAtlas)
	{
		this.textureAtlas = textureAtlas;
		this.material.setProperty(MaterialProperty.TEXTURE,
				this.textureAtlas.get().getTexture());
		return this;
	}

	public void load(AssetManager assets)
	{
		MeshData data = generateMeshData(this);
		Mesh mesh = ModelUtil.createDynamicMesh(data);
		String name = "view" + this.hashCode();
		assets.cacheResource("mesh", name, mesh);
		this.mesh = assets.getAsset("mesh", name);
	}

	public void update()
	{
		if (this.dirty)
		{
			ModelUtil.updateMesh(this.mesh.get(), generateMeshData(this));
			this.dirty = false;
		}
	}

	public void color(int x, int y, int color)
	{
		if (this.colors.set(x, y, color) != color)
		{
			this.dirty = true;
		}
	}

	public void glyph(int x, int y, char glyph)
	{
		if (this.glyphs.set(x, y, glyph) != glyph)
		{
			this.dirty = true;
		}
	}

	public void draw(int x, int y, char glyph, int color)
	{
		boolean flag = this.glyphs.set(x, y, glyph) != glyph;
		flag |= this.colors.set(x, y, color) != color;

		if (flag)
		{
			this.dirty = true;
		}
	}

	public void forEach(TriConsumer<Vector2ic, Character, Integer> action)
	{
		Vector2i vec = new Vector2i();
		for(int i = 0; i < this.width; ++i)
		{
			for(int j = 0; j < this.height; ++j)
			{
				vec.set(i, j);
				char c = (char) this.glyphs.get(i, j);
				int k = this.colors.get(i, j);
				action.accept(vec, c, k);
			}
		}
	}

	public IntMap getColors()
	{
		return this.colors;
	}

	public IntMap getGlyphs()
	{
		return this.glyphs;
	}

	public void markDirty()
	{
		this.dirty = true;
	}

	public Asset<TextureAtlas> getTextureAtlas()
	{
		return this.textureAtlas;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
	}

	public Material getMaterial()
	{
		return this.material;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	private static MeshData generateMeshData(RasterizedView view)
	{
		Vector2f from = new Vector2f();
		Vector2f to = new Vector2f();
		Vector2f textl = new Vector2f();
		Vector2f texbr = new Vector2f();
		Vector3f color = new Vector3f();

		MeshBuilder mb = new MeshBuilder();
		for(int i = 0; i < view.width; ++i)
		{
			for(int j = 0; j < view.height; ++j)
			{
				final SubTexture subTexture = view.textureAtlas.get()
						.getSubTexture(view.glyphs.get(i, j));
				ColorUtil.getNormalizedRGB(view.colors.get(i, j), color);
				from.set(i, j);
				to.set(i + 1, j + 1);
				textl.set(subTexture.getU(), subTexture.getV());
				texbr.set(subTexture.getU() + subTexture.getWidth(),
						subTexture.getV() + subTexture.getHeight());

				int count = mb.getVertexCount();
				mb.addVertex(from.x(), from.y(), 0,
						textl.x(), texbr.y(),
						color.x(), color.y(), color.z());
				mb.addVertex(to.x(), from.y(), 0,
						texbr.x(), texbr.y(),
						color.x(), color.y(), color.z());
				mb.addVertex(from.x(), to.y(), 0,
						textl.x(), textl.y(),
						color.x(), color.y(), color.z());
				mb.addVertex(to.x(), from.y(), 0,
						texbr.x(), texbr.y(),
						color.x(), color.y(), color.z());
				mb.addVertex(to.x(), to.y(), 0,
						texbr.x(), textl.y(),
						color.x(), color.y(), color.z());
				mb.addVertex(from.x(), to.y(), 0,
						textl.x(), textl.y(),
						color.x(), color.y(), color.z());

				mb.addVertexIndex(count++, count++, count++);
				mb.addVertexIndex(count++, count++, count);
			}
		}
		return mb.bake(false, false);
	}
}
