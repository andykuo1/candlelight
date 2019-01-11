package canary.bstone.resource;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.json.JSONArray;
import canary.bstone.json.JSONNumber;
import canary.bstone.json.JSONObject;
import canary.bstone.mogli.Texture;
import canary.bstone.sprite.textureatlas.SubTexture;
import canary.bstone.sprite.textureatlas.TextureAtlas;

/**
 * Created by Andy on 10/31/17.
 */
public class TextureAtlasLoader extends AssetLoader<TextureAtlas>
{
	public static final String PARAM_TEXTURE = "texture";
	public static final String PARAM_SUBTEXTURES = "subtextures";
	public static final String PARAM_SUBTEXTURE_UV = "uv";
	public static final String PARAM_SUBTEXTURE_SIZE = "size";

	public TextureAtlasLoader(AssetManager assetManager)
	{
		super(assetManager);
	}

	@Override
	protected TextureAtlas onLoad(JSONObject src) throws Exception
	{
		Asset<Texture> texture = this.assets.getAsset("texture",
				this.getString(src, PARAM_TEXTURE));
		JSONArray subtextures = (JSONArray) src.get(PARAM_SUBTEXTURES);

		TextureAtlas atlas = new TextureAtlas(texture, subtextures.size());
		for(int i = 0; i < atlas.length(); ++i)
		{
			JSONObject subtexture = (JSONObject) subtextures.get(i);
			JSONArray uv = (JSONArray) subtexture.get(PARAM_SUBTEXTURE_UV);
			JSONArray size = (JSONArray) subtexture.get(PARAM_SUBTEXTURE_SIZE);

			float u = 0;
			float v = 0;
			if (uv != null)
			{
				u = ((JSONNumber) uv.get(0)).toFloat();
				v = ((JSONNumber) uv.get(1)).toFloat();
			}

			float w = 1;
			float h = 1;
			if (size != null)
			{
				w = ((JSONNumber) size.get(0)).toFloat();
				h = ((JSONNumber) size.get(1)).toFloat();
			}

			SubTexture result = atlas.getSubTexture(i);
			result.setUV(u, v);
			result.setSize(w, h);
		}
		return atlas;
	}
}
