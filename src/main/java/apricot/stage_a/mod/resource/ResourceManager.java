package apricot.stage_a.mod.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 5/11/17.
 */
public class ResourceManager
{
	public static Map<String, String> resourceType = new HashMap<>();

	static
	{
		resourceType.put("vsh", "shader");
		resourceType.put("fsh", "shader");

		resourceType.put("png", "texture");
		resourceType.put("jpg", "texture");

		resourceType.put("obj", "model");
	}

	public static String getTypeName(String type)
	{
		return resourceType.get(type);
	}
}
