package org.bstone.render.model;

import org.bstone.material.Material;
import org.bstone.mogli.Mesh;
import org.qsilver.asset.Asset;

/**
 * Created by Andy on 8/4/17.
 */
public class TextModel extends Model
{
	private TextModelManager textModelManager;
	private String text;

	TextModel(TextModelManager textModelManager, String text, Asset<Mesh> mesh, Material material, String renderType)
	{
		super(mesh, material, renderType);

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
