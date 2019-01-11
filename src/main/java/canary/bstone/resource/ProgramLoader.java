package canary.bstone.resource;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.json.JSONObject;
import canary.bstone.mogli.Program;
import canary.bstone.mogli.Shader;

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
	protected Program onLoad(JSONObject src) throws Exception
	{
		String vertexShaderName = this.getString(src, "vertex_shader");
		String fragmentShaderName = this.getString(src, "fragment_shader");

		Asset<Shader> vertexShader = this.assets.getAsset("vertex_shader", vertexShaderName);
		Asset<Shader> fragmentShader = this.assets.getAsset("fragment_shader", fragmentShaderName);

		Program program = new Program();
		program.link(vertexShader.get(), fragmentShader.get());

		vertexShader.delete();
		fragmentShader.delete();

		return program;
	}
}
