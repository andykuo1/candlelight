package canary.bstone.resource;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.json.JSONObject;
import canary.bstone.mogli.Bitmap;
import canary.bstone.mogli.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Created by Andy on 10/26/17.
 */
public class TextureLoader extends AssetLoader<Texture>
{
	public TextureLoader(AssetManager assetManager)
	{
		super(assetManager);
	}

	@Override
	protected Texture onLoad(JSONObject src) throws Exception
	{
		Asset<Bitmap> bitmap = this.assets.getAsset("bitmap", this.getString(src, "bitmap"));
		String minMagFilterKey = this.getString(src, "minMagFilter");
		String wrapModeKey = this.getString(src, "wrapMode");

		int minMagFilter;
		if ("nearest".equals(minMagFilterKey))
		{
			minMagFilter = GL11.GL_NEAREST;
		}
		else if ("linear".equals(minMagFilterKey))
		{
			minMagFilter = GL11.GL_LINEAR;
		}
		else
		{
			throw new IllegalArgumentException("could not find valid value for parameter '" + "minMagFilter" + "'");
		}

		int wrapMode;
		if ("clamp_edge".equals(wrapModeKey))
		{
			wrapMode = GL12.GL_CLAMP_TO_EDGE;
		}
		else
		{
			throw new IllegalArgumentException("could not find valid value for parameter '" + "wrapMode" + "'");
		}

		return new Texture(bitmap.get(), minMagFilter, wrapMode);
	}
}
