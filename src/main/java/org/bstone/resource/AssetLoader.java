package org.bstone.resource;

import org.bstone.asset.AssetManager;
import org.bstone.json.JSONObject;
import org.bstone.json.JSONString;
import org.bstone.util.dataformat.json.JSONFormatParser;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andy on 10/28/17.
 */
public abstract class AssetLoader<T extends AutoCloseable> implements ResourceLoader<T>
{
	protected final AssetManager assets;

	public AssetLoader(AssetManager assetManager)
	{
		this.assets = assetManager;
	}

	@Override
	public T load(InputStream stream) throws Exception
	{
		JSONFormatParser parser = new JSONFormatParser(256);
		JSONObject src = (JSONObject) parser.parse(new InputStreamReader(stream, "UTF-8"));
		return this.onLoad(src);
	}

	protected abstract T onLoad(JSONObject src) throws Exception;

	protected final String getString(JSONObject src, String key)
	{
		JSONString s = (JSONString) src.get(key);
		return s != null ? s.get() : null;
	}

	protected final String getStringOrDefault(JSONObject src, String key, String defaultValue)
	{
		String ret = this.getString(src, key);
		return ret != null ? ret : defaultValue;
	}

	public final AssetManager getAssets()
	{
		return this.assets;
	}
}
