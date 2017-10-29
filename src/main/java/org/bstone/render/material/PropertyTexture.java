package org.bstone.render.material;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.mogli.Texture;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Created by Andy on 8/10/17.
 */
public class PropertyTexture extends Property<ContextTexture>
{
	public static final PropertyTexture PROPERTY = new PropertyTexture(new ContextTexture());

	public static final String TEXTURE_NAME = "texture";
	public static final String SPRITE_OFFSET_NAME = "spriteOffset";
	public static final String SPRITE_SCALE_NAME = "spriteScale";
	public static final String TRANSPARENCY_NAME = "transparency";

	public static final Vector2fc SPRITE_OFFSET_DEFAULT = new Vector2f();
	public static final Vector2fc SPRITE_SCALE_DEFAULT = new Vector2f(1, 1);
	public static final boolean TRANSPARENCY_DEFAULT = true;

	protected PropertyTexture(ContextTexture context)
	{
		super(context);
	}

	@Override
	protected void addSupportForMaterial(Material material)
	{
		material.addProperty(TEXTURE_NAME, Asset.class);
		material.addProperty(SPRITE_OFFSET_NAME, Vector2fc.class, SPRITE_OFFSET_DEFAULT);
		material.addProperty(SPRITE_SCALE_NAME, Vector2fc.class, SPRITE_SCALE_DEFAULT);
		material.addProperty(TRANSPARENCY_NAME, Boolean.class, TRANSPARENCY_DEFAULT);
	}

	@SuppressWarnings("unchecked")
	public Asset<Texture> getTexture(Material material)
	{
		return (Asset<Texture>) material.getProperty(Asset.class, TEXTURE_NAME);
	}

	public Vector2fc getSpriteOffset(Material material)
	{
		return material.getProperty(Vector2fc.class, SPRITE_OFFSET_NAME);
	}

	public Vector2fc getSpriteScale(Material material)
	{
		return material.getProperty(Vector2fc.class, SPRITE_SCALE_NAME);
	}

	public boolean getTransparency(Material material)
	{
		return material.getProperty(Boolean.class, TRANSPARENCY_NAME);
	}
}
