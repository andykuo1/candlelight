package org.zilar.render.model;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Mesh;
import org.zilar.render.material.Material;

/**
 * Created by Andy on 8/4/17.
 */
public class TextModel extends Model
{
	private TextModelManager textModelManager;
	private String text;

	TextModel(TextModelManager textModelManager, String text, Asset<Mesh> mesh, Material material)
	{
		super(mesh, material);

		this.textModelManager = textModelManager;
		this.text = text;
	}

	public void setText(String text)
	{
		float w = this.textModelManager.getTextWidth(text);
		float h = this.textModelManager.getTextHeight(text);
		float ow = this.textModelManager.getTextWidth(this.text);
		float oh = this.textModelManager.getTextHeight(this.text);

		if (w != ow || oh != h) throw new IllegalArgumentException("New text must be have the same lines and length of the original! (Add spaces if necessary)");

		this.textModelManager.updateText(this, text);
		this.text = text;
	}

	public String getText()
	{
		return this.text;
	}
}
