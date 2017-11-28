package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.base.MaterialProperty;
import net.jimboi.canary.stage_a.base.renderer.SimpleRenderer;

import org.bstone.asset.Asset;
import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.bstone.sprite.textureatlas.SubTexture;
import org.bstone.sprite.textureatlas.TextureAtlas;
import org.bstone.util.ColorUtil;
import org.bstone.util.grid.IntMap;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;

/**
 * Created by Andy on 9/13/17.
 */
public class RasterView implements AutoCloseable
{
	private final Asset<Mesh> mesh;

	private final int width;
	private final int height;

	private final IntMap colors;
	private final IntMap glyphs;
	private boolean dirty;

	private final Asset<TextureAtlas> textureAtlas;
	private final Material material;

	public RasterView(int width, int height, Asset<TextureAtlas> textureAtlas)
	{
		this.width = width;
		this.height = height;

		this.colors = new IntMap(this.width, this.height);
		this.glyphs = new IntMap(this.width, this.height);

		this.textureAtlas = textureAtlas;
		this.material = new Material();
		this.material.setProperty(MaterialProperty.TEXTURE, this.textureAtlas.get().getTexture());
		Mesh mesh = ModelUtil.createDynamicMesh(this.generateMeshData());
		Console.getConsole().getAssetManager().cacheResource("mesh", this.toString(), mesh);
		this.mesh = Console.getConsole().getAssetManager().getAsset("mesh", this.toString());
	}

	public void close() throws Exception
	{
		Console.getConsole().getAssetManager().unloadResource("mesh", this.toString());
	}

	private MeshData generateMeshData()
	{
		Vector2f from = new Vector2f();
		Vector2f to = new Vector2f();
		Vector2f textl = new Vector2f();
		Vector2f texbr = new Vector2f();
		Vector3f color = new Vector3f();

		MeshBuilder mb = new MeshBuilder();
		for(int i = 0; i < this.width; ++i)
		{
			for(int j = 0; j < this.height; ++j)
			{
				final SubTexture subTexture = this.textureAtlas.get()
						.getSubTexture(this.glyphs.get(i, j));
				ColorUtil.getNormalizedRGB(this.colors.get(i, j), color);
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
				mb.addVertexIndex(count++, count++, count++);
			}
		}
		return mb.bake(false, false);
	}

	public void update()
	{
		if (this.dirty)
		{
			ModelUtil.updateMesh(this.mesh.get(), this.generateMeshData());
			this.dirty = false;
		}
	}

	public void doRender(Matrix4fc transformation, SimpleRenderer renderer)
	{
		this.update();
		renderer.draw(this.mesh, this.material, transformation);
	}

	public void color(int x, int y, int color)
	{
		if (x >= 0 && x < this.colors.width() && y >= 0 && y < this.colors.height())
		{
			if (this.colors.set(x, y, color) != color)
			{
				this.dirty = true;
			}
		}
	}

	public void glyph(int x, int y, char glyph)
	{
		if (x >= 0 && x < this.glyphs.width() && y >= 0 && y < this.glyphs.height())
		{
			if (this.glyphs.set(x, y, glyph) != glyph)
			{
				this.dirty = true;
			}
		}
	}

	public void draw(int x, int y, char glyph, int color)
	{
		if (x >= 0 && x < this.glyphs.width() && y >= 0 && y < this.glyphs.height())
		{
			if (this.glyphs.set(x, y, glyph) != glyph || this.colors.set(x, y, color) != color)
			{
				this.dirty = true;
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

	public Asset<TextureAtlas> getTextureAtlas()
	{
		return this.textureAtlas;
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}
}
