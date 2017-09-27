package net.jimboi.boron.stage_a.gordo;

import org.bstone.mogli.Mesh;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.util.gridmap.ByteMap;
import org.bstone.util.gridmap.IntMap;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.qsilver.asset.Asset;
import org.qsilver.util.ColorUtil;
import org.zilar.sprite.Sprite;
import org.zilar.sprite.TextureAtlas;

/**
 * Created by Andy on 9/13/17.
 */
public class RasterView
{
	private final int width;
	private final int height;

	private final IntMap colors;
	private final ByteMap types;

	private final Asset<Mesh> mesh;
	private final Asset<TextureAtlas> textureAtlas;

	public RasterView(int width, int height, Asset<Mesh> mesh, Asset<TextureAtlas> textureAtlas)
	{
		this.width = width;
		this.height = height;

		this.colors = new IntMap(this.width, this.height);
		this.types = new ByteMap(this.width, this.height);

		this.mesh = mesh;
		this.textureAtlas = textureAtlas;
	}

	public void doRender(Matrix4fc transformation, SimpleProgramRenderer renderer)
	{
		final Matrix4f mat = new Matrix4f();
		final Vector3f vec = new Vector3f();
		final Mesh mesh = this.mesh.getSource();
		for(int x = 0; x < this.width; ++x)
		{
			for(int y = 0; y < this.height; ++y)
			{
				mat.set(transformation).translate(x, y, 0);
				ColorUtil.getNormalizedRGB(this.colors.get(x, y), vec);

				final Sprite sprite = this.textureAtlas.getSource().getSprite(this.types.get(x, y));
				renderer.draw(mesh, sprite, true, vec, mat);
			}
		}
	}

	public void setPixels(RasterView view)
	{
		this.colors.putAll(view.colors);
		this.types.putAll(view.types);
	}

	public void setPixelTypes(RasterView view)
	{
		this.types.putAll(view.types);
	}

	public void setPixelColors(RasterView view)
	{
		this.colors.putAll(view.colors);
	}

	public void setPixel(int x, int y, int color, byte type)
	{
		if (!this.colors.containsKey(x, y)) return;

		this.colors.put(x, y, color);
		this.types.put(x, y, type);
	}

	public void setPixelType(int x, int y, byte type)
	{
		if (!this.types.containsKey(x, y)) return;

		this.types.put(x, y, type);
	}

	public void setPixelColor(int x, int y, int color)
	{
		if (!this.colors.containsKey(x, y)) return;

		this.colors.put(x, y, color);
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

	public IntMap getPixelColors()
	{
		return this.colors;
	}

	public ByteMap getPixelTypes()
	{
		return this.types;
	}

	public Asset<Mesh> getMesh()
	{
		return this.mesh;
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
