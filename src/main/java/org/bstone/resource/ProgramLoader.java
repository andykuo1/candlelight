package org.bstone.resource;

import org.bstone.asset.Asset;
import org.bstone.asset.AssetManager;
import org.bstone.json.JSONObject;
import org.bstone.mogli.Program;
import org.bstone.mogli.Shader;

/**
 * Created by Andy on 10/28/17.
 */
public class ProgramLoader extends AssetLoader<Program>
{
	public ProgramLoader(AssetManager assetManager)
	{
		super(assetManager);
	}

	@Override
	protected Program onLoad(JSONObject data) throws Exception
	{
		String vertexShaderName = this.getString(data, "vertex_shader");
		String fragmentShaderName = this.getString(data, "fragment_shader");

		Asset<Shader> vertexShader = this.assets.getAsset("vertex_shader", vertexShaderName);
		Asset<Shader> fragmentShader = this.assets.getAsset("fragment_shader", fragmentShaderName);

		Program program = new Program();
		program.link(vertexShader.get(), fragmentShader.get());

		vertexShader.delete();
		fragmentShader.delete();

		return program;
	}
}
