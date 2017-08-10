package org.bstone.render.material;

import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.qsilver.asset.Asset;
import org.zilar.sprite.Sprite;

/**
 * Created by Andy on 8/10/17.
 */
public class PropertyTexture
{
	public static final String TEXTURE = "texture";
	public static final String SPRITE_OFFSET = "spriteOffset";
	public static final String SPRITE_SCALE = "spriteScale";
	public static final String TRANSPARENCY = "transparency";

	public static Material addProperty(Material material)
	{
		material.addProperty(TEXTURE, Asset.class);
		material.addProperty(SPRITE_OFFSET, Vector2fc.class, new Vector2f());
		material.addProperty(SPRITE_SCALE, Vector2fc.class, new Vector2f(1, 1));
		material.addProperty(TRANSPARENCY, Boolean.class, true);
		return material;
	}

	public static Material setSprite(Material material, Sprite sprite)
	{
		setTexture(material, sprite.getTexture());
		setSpriteOffset(material, new Vector2f(sprite.getU(), sprite.getV()));
		setSpriteScale(material, new Vector2f(sprite.getWidth(), sprite.getHeight()));
		return material;
	}

	public static Material setTexture(Material material, Asset<Texture> texture)
	{
		if (!material.hasProperty(TEXTURE))
		{
			material.addProperty(TEXTURE, Asset.class);
		}

		material.setProperty(TEXTURE, texture);
		return material;
	}

	public static Material setSpriteOffset(Material material, Vector2fc spriteOffset)
	{
		if (!material.hasProperty(SPRITE_OFFSET))
		{
			material.addProperty(SPRITE_OFFSET, Vector2fc.class);
		}

		material.setProperty(SPRITE_OFFSET, spriteOffset);
		return material;
	}

	public static Material setSpriteScale(Material material, Vector2fc spriteScale)
	{
		if (!material.hasProperty(SPRITE_SCALE))
		{
			material.addProperty(SPRITE_SCALE, Vector2fc.class);
		}

		material.setProperty(SPRITE_SCALE, spriteScale);
		return material;
	}

	public static Material setTransparency(Material material, boolean transparency)
	{
		if (!material.hasProperty(TRANSPARENCY))
		{
			material.addProperty(TRANSPARENCY, Boolean.class);
		}

		material.setProperty(TRANSPARENCY, transparency);
		return material;
	}

	@SuppressWarnings("unchecked")
	public static Asset<Texture> getTexture(Material material)
	{
		return (Asset<Texture>) material.getProperty(Asset.class, TEXTURE);
	}

	public static Vector2fc getSpriteOffset(Material material)
	{
		return material.getProperty(Vector2fc.class, SPRITE_OFFSET);
	}

	public static Vector2fc getSpriteScale(Material material)
	{
		return material.getProperty(Vector2fc.class, SPRITE_SCALE);
	}

	public static boolean getTransparency(Material material)
	{
		return material.getProperty(Boolean.class, TRANSPARENCY);
	}
}
