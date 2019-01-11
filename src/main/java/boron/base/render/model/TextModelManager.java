package boron.base.render.model;

import boron.base.asset.Asset;
import boron.base.render.material.Material;
import boron.base.render.material.PropertyTexture;
import boron.base.sprite.FontSheet;
import boron.base.sprite.Sprite;

import boron.bstone.mogli.Mesh;
import org.joml.Vector2f;
import boron.zilar.meshbuilder.MeshBuilder;
import boron.zilar.meshbuilder.MeshData;
import boron.zilar.meshbuilder.ModelUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 8/4/17.
 */
public class TextModelManager
{
	private Map<String, Mesh> texts = new HashMap<>();
	private Set<Mesh> meshes = new HashSet<>();
	private FontSheet fontsheet;
	private Material material;

	public TextModelManager(FontSheet fontsheet)
	{
		this.fontsheet = fontsheet;

		this.material = new Material();
		this.material.addProperty(PropertyTexture.PROPERTY);
		PropertyTexture.PROPERTY.bind(this.material)
				.setTexture(this.fontsheet.get((char) 0).getTexture())
				.unbind();
	}

	public void destroy()
	{
		for(Mesh mesh : this.texts.values())
		{
			try
			{
				mesh.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		for(Mesh mesh : this.meshes)
		{
			try
			{
				mesh.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.texts.clear();
		this.meshes.clear();
	}

	public Model createStaticText(String text)
	{
		Mesh mesh;
		if (this.texts.containsKey(text))
		{
			mesh = this.texts.get(text);
		}
		else
		{
			mesh = ModelUtil.createStaticMesh(this.buildMeshFromText(text));
			this.texts.put(text, mesh);
		}
		return new Model(Asset.wrap(mesh), this.material);
	}

	public TextModel createDynamicText(String text)
	{
		Mesh mesh = ModelUtil.createDynamicMesh(this.buildMeshFromText(text));
		this.meshes.add(mesh);
		return new TextModel(this, text, Asset.wrap(mesh), this.material);
	}

	public void updateText(TextModel model, String text)
	{
		MeshData meshdata = this.buildMeshFromText(text);
		Mesh mesh = model.getMesh().getSource();
		mesh.bind();
		{
			ModelUtil.updateMesh(model.getMesh().getSource(), meshdata);
		}
		mesh.unbind();
	}

	public MeshData buildMeshFromText(String text)
	{
		MeshBuilder mb = new MeshBuilder();
		float x = 0;
		float y = 0;

		float w = 1;
		float h = 1;

		float u;
		float v;
		float tw;
		float th;

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

		return mb.bake(false, false);
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
}
