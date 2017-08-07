package org.bstone.render;

import net.jimboi.apricot.base.renderer.property.PropertyTexture;

import org.bstone.material.Material;
import org.bstone.material.MaterialManager;
import org.bstone.mogli.Mesh;
import org.joml.Vector2f;
import org.qsilver.asset.Asset;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.MeshData;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.sprite.FontSheet;
import org.zilar.sprite.Sprite;

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
	private String renderType;
	private Material material;

	public TextModelManager(MaterialManager materialManager, FontSheet fontsheet, String renderType)
	{
		this.fontsheet = fontsheet;
		this.renderType = renderType;

		this.material = materialManager.createMaterial(new PropertyTexture(this.fontsheet.get((char) 0).getTexture()));
	}

	public void destroy()
	{
		for(Mesh mesh : this.texts.values())
		{
			mesh.close();
		}

		for(Mesh mesh : this.meshes)
		{
			mesh.close();
		}

		this.texts.clear();
		this.meshes.clear();
	}

	public Model createStaticText(String text)
	{
		float width = this.getTextWidth(text);
		float height = this.getTextHeight(text);

		Mesh mesh;
		if (this.texts.containsKey(text))
		{
			mesh = this.texts.get(text);
		}
		else
		{
			mesh = ModelUtil.createStaticMesh(this.buildMeshFromText(text, width, height));
			this.texts.put(text, mesh);
		}
		Model model = new Model(Asset.wrap(mesh), this.material, this.renderType);
		model.transformation().scale(width, height, 1);
		return model;
	}

	public TextModel createDynamicText(String text)
	{
		float width = this.getTextWidth(text);
		float height = this.getTextHeight(text);
		Mesh mesh = ModelUtil.createDynamicMesh(this.buildMeshFromText(text, width, height));
		this.meshes.add(mesh);
		TextModel model = new TextModel(this, text, Asset.wrap(mesh), this.material, this.renderType);
		model.transformation().scale(width, height, 1);
		return model;
	}

	public void updateText(TextModel model, String text)
	{
		float width = this.getTextWidth(text);
		float height = this.getTextHeight(text);
		MeshData meshdata = this.buildMeshFromText(text, width, height);
		Mesh mesh = model.getMesh().getSource();
		mesh.bind();
		{
			ModelUtil.updateMesh(model.getMesh().getSource(), meshdata);
		}
		mesh.unbind();
	}

	public MeshData buildMeshFromText(String text, float textWidth, float textHeight)
	{
		MeshBuilder mb = new MeshBuilder();
		float x = 0;
		float y = 0;

		float w = 1 / textWidth;
		float h = 1 / textHeight;

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
