package net.jimboi.canary.stage_a.cuplet.scene_main.gui;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.sprite.FontSheet;
import org.zilar.sprite.Sprite;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 7/17/17.
 */
public class TextMesh implements AutoCloseable
{
	public static FontSheet defaultFontSheet;
	public static final Map<String, TextMesh> texts = new HashMap<>();

	public static TextMesh getText(String text)
	{
		return texts.computeIfAbsent(text, t -> new TextMesh(defaultFontSheet, t));
	}

	public static void clear()
	{
		for(TextMesh textMesh : texts.values())
		{
			try
			{
				textMesh.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		texts.clear();
	}

	protected String text;
	protected Mesh mesh;
	protected FontSheet fontsheet;
	protected Asset<Texture> texture;
	protected boolean dirty;

	TextMesh(FontSheet fontsheet, String text)
	{
		this.text = text;
		this.fontsheet = fontsheet;
		this.texture = this.fontsheet.get((char) 0).getTexture();
		this.dirty = true;
	}

	@Override
	public void close() throws Exception
	{
		this.mesh.close();
	}

	public float getTextWidth(String text)
	{
		int len = 1;
		int j = 0;
		for(int i = 0; i < text.length(); ++i)
		{
			char c = text.charAt(i);
			if (c == '\n')
			{
				if (j > len) len = j;
				continue;
			}
			j++;
		}
		return j > len ? j : len;
	}

	public float getTextHeight(String text)
	{
		int len = 1;
		for(int i = 0; i < text.length(); ++i)
		{
			char c = text.charAt(i);
			if (c == '\n') len++;
		}
		return len;
	}

	public void setText(String text)
	{
		this.text = text;
		this.dirty = true;
	}

	private void update(String text)
	{
		MeshBuilder mb = new MeshBuilder();
		float x = 0;
		float y = 0;

		float w = 1 / this.getTextWidth(this.text);
		float h = 1 / this.getTextHeight(this.text);

		float u = 0;
		float v = 0;
		float tw = 0;
		float th = 0;

		for(int i = 0; i < text.length(); ++i)
		{
			char c = text.charAt(i);
			if (c == ' ')
			{
				x += w;
			}
			else if (c == '\n')
			{
				x = 0;
				y += h;
			}
			else
			{
				Sprite sprite = this.fontsheet.get(c);
				u = sprite.getU();
				v = sprite.getV();
				tw = sprite.getWidth();
				th = sprite.getHeight();

				mb.addPlane(new Vector2f(x, y), new Vector2f(x + w, y + h), 0, new Vector2f(u, v), new Vector2f(u + tw, v + th));
				x += w;
			}
		}

		MeshData data = mb.bake(false, false);
		if (this.mesh != null)
		{
			ModelUtil.updateMesh(this.mesh, data);
		}
		else
		{
			this.mesh = ModelUtil.createDynamicMesh(data);
		}
	}

	public Asset<Texture> getTexture()
	{
		return this.texture;
	}

	public Mesh getMesh()
	{
		if (this.dirty)
		{
			this.update(this.text);
			this.dirty = false;
		}

		return this.mesh;
	}

	public boolean equals(Object o)
	{
		if (o instanceof TextMesh)
		{
			TextMesh textMesh = (TextMesh) o;
			return textMesh.fontsheet == this.fontsheet && this.text.equals(textMesh.text);
		}

		return false;
	}
}
