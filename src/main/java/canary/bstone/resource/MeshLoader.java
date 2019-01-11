package canary.bstone.resource;

import canary.bstone.mogli.Mesh;
import canary.bstone.util.parser.obj.OBJFormatParser;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andy on 10/26/17.
 */
public class MeshLoader implements ResourceLoader<Mesh>
{
	@Override
	public Mesh load(InputStream stream) throws Exception
	{
		OBJFormatParser parser = new OBJFormatParser(256);
		Mesh obj = parser.parse(new InputStreamReader(stream));
		return obj;
	}
}
