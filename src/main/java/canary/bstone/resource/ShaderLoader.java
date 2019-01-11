package canary.bstone.resource;

import canary.bstone.mogli.Shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andy on 10/28/17.
 */
public class ShaderLoader implements ResourceLoader<Shader>
{
	private final int type;

	public ShaderLoader(int type)
	{
		this.type = type;
	}

	@Override
	public Shader load(InputStream stream) throws Exception
	{
		StringBuilder sb = new StringBuilder();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream)))
		{
			String buffer;
			while ((buffer = reader.readLine()) != null)
			{
				sb.append(buffer);
				sb.append('\n');
			}
		}
		return new Shader(sb.toString(), this.type);
	}

	public int getType()
	{
		return this.type;
	}
}
