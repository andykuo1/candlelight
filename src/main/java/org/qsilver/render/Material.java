package org.qsilver.render;

import net.jimboi.mod.sprite.Sprite;

import org.bstone.mogli.Texture;
import org.bstone.util.ColorUtil;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Created by Andy on 4/29/17.
 */
public class Material
{
	//Anything related to textures or how it appears on the surface
	private final String renderID;

	public Vector3f mainColor = new Vector3f(1, 1, 1);
	public Vector3f specularColor = new Vector3f(1, 1, 1);
	public float shininess = 80F;

	public Texture texture;
	public Sprite sprite;
	public Texture normalmap;
	public Vector2f tiling = new Vector2f();

	public Material(String renderID)
	{
		this.renderID = renderID;
	}

	public Material setColor(int color)
	{
		this.mainColor.set(ColorUtil.getRedf(color), ColorUtil.getGreenf(color), ColorUtil.getBluef(color));
		return this;
	}

	public Material setTexture(Texture texture)
	{
		this.texture = texture;
		return this;
	}

	public Texture getTexture()
	{
		return this.texture;
	}

	public boolean hasTexture()
	{
		return this.texture != null;
	}

	public final String getRenderID()
	{
		return this.renderID;
	}
}
