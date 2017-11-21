package net.jimboi.canary.stage_a.owle;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.gridmap.ByteMap;
import net.jimboi.boron.base_ab.gridmap.IntMap;
import net.jimboi.boron.base_ab.sprite.Sprite;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;

import org.bstone.mogli.Mesh;
import org.bstone.transform.Transform2;
import org.bstone.util.ColorUtil;
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
	private final Mesh mesh;

	private final int width;
	private final int height;

	private final IntMap colors;
	private final ByteMap types;
	private boolean dirty;

	private final Asset<TextureAtlas> textureAtlas;

	public RasterView(int width, int height, Asset<TextureAtlas> textureAtlas)
	{
		this.width = width;
		this.height = height;

		this.colors = new IntMap(this.width, this.height);
		this.types = new ByteMap(this.width, this.height);

		this.textureAtlas = textureAtlas;
		this.mesh = ModelUtil.createDynamicMesh(this.generateMeshData());
	}

	public void close() throws Exception
	{
		this.mesh.close();
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
				final Sprite sprite = this.textureAtlas.getSource().getSprite(this.types.get(i, j));
				ColorUtil.getNormalizedRGB(this.colors.get(i, j), color);
				from.set(i, j);
				to.set(i + 1, j + 1);
				textl.set(sprite.getU(), sprite.getV());
				texbr.set(sprite.getU() + sprite.getWidth(), sprite.getV() + sprite.getHeight());

				int count = mb.getVertexCount();
				mb.addVertex(from.x(), from.y(), 0, texbr.x(), textl.y(), color.x(), color.y(), color.z());
				mb.addVertex(to.x(), from.y(), 0, textl.x(), textl.y(), color.x(), color.y(), color.z());
				mb.addVertex(from.x(), to.y(), 0, texbr.x(), texbr.y(), color.x(), color.y(), color.z());
				mb.addVertex(to.x(), from.y(), 0, textl.x(), textl.y(), color.x(), color.y(), color.z());
				mb.addVertex(to.x(), to.y(), 0, textl.x(), texbr.y(), color.x(), color.y(), color.z());
				mb.addVertex(from.x(), to.y(), 0, texbr.x(), texbr.y(), color.x(), color.y(), color.z());

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
			ModelUtil.updateMesh(this.mesh, this.generateMeshData());
			this.dirty = false;
		}
	}

	public void doRender(Matrix4fc transformation, ConsoleProgramRenderer renderer)
	{
		final Mesh mesh = this.mesh;
		this.update();
		renderer.draw(mesh,
				this.textureAtlas.getSource().getSprite(0).getTexture(),
				Transform2.ZERO, Transform2.IDENTITY, true,
				null, transformation);
	}

	public void clear(byte type, int color)
	{
		this.colors.clear(color);
		this.types.clear(type);
		this.dirty = true;
	}

	public void setPixels(RasterView view)
	{
		this.colors.putAll(view.colors);
		this.types.putAll(view.types);
		this.dirty = true;
	}

	public void setPixelTypes(RasterView view)
	{
		this.types.putAll(view.types);
		this.dirty = true;
	}

	public void setPixelColors(RasterView view)
	{
		this.colors.putAll(view.colors);
		this.dirty = true;
	}

	public void setPixel(int x, int y, int color, byte type)
	{
		if (!this.colors.containsKey(x, y)) return;

		this.colors.put(x, y, color);
		this.types.put(x, y, type);
		this.dirty = true;
	}

	public void setPixelType(int x, int y, byte type)
	{
		if (!this.types.containsKey(x, y)) return;

		this.types.put(x, y, type);
		this.dirty = true;
	}

	public void setPixelColor(int x, int y, int color)
	{
		if (!this.colors.containsKey(x, y)) return;

		this.colors.put(x, y, color);
		this.dirty = true;
	}

	public byte getPixelType(int x, int y)
	{
		if (!this.types.containsKey(x, y)) return 0;

		return this.types.get(x, y);
	}

	public int getPixelColor(int x, int y)
	{
		if (!this.colors.containsKey(x, y)) return 0;

		return this.colors.get(x, y);
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
