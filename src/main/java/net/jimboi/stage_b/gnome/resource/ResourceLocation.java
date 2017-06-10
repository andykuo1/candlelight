package net.jimboi.stage_b.gnome.resource;

public class ResourceLocation
{
	private String filepath;

	public ResourceLocation(String path)
	{
		int i = path.indexOf(':');
		int j = path.indexOf('.');
		if (i == -1)
		{
			this.filepath = path;
		}
		else
		{
			String domain = path.substring(0, i);
			String id = path.substring(i + 1, j);
			String type = path.substring(j + 1);
			this.filepath = ResourceManager.getDirectory(domain, id, type);
		}
	}

	public String getFilePath()
	{
		return this.filepath;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof ResourceLocation)
		{
			return this.filepath.equals(((ResourceLocation) o).filepath);
		}

		return this.filepath.equals(o);
	}

	@Override
	public int hashCode()
	{
		return this.filepath.hashCode();
	}

	@Override
	public String toString()
	{
		return this.filepath;
	}
}
