package net.jimboi.stage_b.gnome.resource;

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

	public static String getDomainDirectory(String domain)
	{
		return "src/main/res/" + domain;
	}

	public static String getDirectory(String domain, String id, String type)
	{
		String filepath = "";
		String typename = null;
		if (id.indexOf('/') == -1)
		{
			typename = resourceType.get(type);
		}

		if (typename != null) filepath += typename + "/";

		filepath += id;

		return "src/main/res/" + domain + "/" + filepath + "." + type;
	}
}
