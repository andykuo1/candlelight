package net.jimboi.apricot.stage_a.mod.resource;

import org.qsilver.ResourceLocation;

public class ModResourceLocation extends ResourceLocation
{
	public ModResourceLocation(String path)
	{
		super(getFullPath(path));
	}

	private static String getFullPath(String path)
	{
		int i = path.lastIndexOf('.');
		int j = path.indexOf(':');
		if (i != -1 && j != -1)
		{
			return path.substring(0, j + 1) + ResourceManager.getTypeName(path.substring(i + 1)) + "/" + path.substring(j + 1);
		}
		return path;
	}
}
