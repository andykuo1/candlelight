package org.bstone.resource;

import org.bstone.asset.AssetManager;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONString;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.bstone.util.dataformat.json.JSONFormatParser;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andy on 10/26/17.
 */
public class TextureLoader implements ResourceLoader<Texture>
{
	private final AssetManager assets;

	public TextureLoader(AssetManager assets)
	{
		this.assets = assets;
	}

	@Override
	public Texture load(InputStream stream) throws Exception
	{
		JSONFormatParser parser = new JSONFormatParser(256);
		JSONObject json = (JSONObject) parser.parse(new InputStreamReader(stream, "UTF-8"));

		JSONString pBitmap = (JSONString) json.get("bitmap");
		JSONString pMinMagFilter = (JSONString) json.get("minMagFilter");
		JSONString pWrapMode = (JSONString) json.get("wrapMode");

		Bitmap bitmap = this.assets.<Bitmap>getAsset("bitmap", pBitmap.get()).get();

		int minMagFilter;
		if ("nearest".equals(pMinMagFilter.get()))
		{
			minMagFilter = GL11.GL_NEAREST;
		}
		else if ("linear".equals(pMinMagFilter.get()))
		{
			minMagFilter = GL11.GL_LINEAR;
		}
		else
		{
			throw new IllegalArgumentException("could not find valid value for parameter '" + "minMagFilter" + "'");
		}

		int wrapMode;
		if ("clamp_edge".equals(pWrapMode.get()))
		{
			wrapMode = GL12.GL_CLAMP_TO_EDGE;
		}
		else
		{
			throw new IllegalArgumentException("could not find valid value for parameter '" + "wrapMode" + "'");
		}

		return new Texture(bitmap, minMagFilter, wrapMode);
	}
}
