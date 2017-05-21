package net.jimboi.mod.resource;

import org.bstone.util.FileUtil;

public class ResourceLocation
{
	private final String filepath;

	public ResourceLocation(String basepath, String filepath)
	{
		this(new ResourceLocation(basepath), new ResourceLocation(filepath));
	}

	public ResourceLocation(ResourceLocation basepath, ResourceLocation filepath)
	{
		this.filepath = basepath.getFilePath() + FileUtil.SEPERATOR + filepath.getFilePath();
	}

	public String getFilePath()
	{
		return this.filepath;
	}

	public ResourceLocation(ResourceLocation basepath, String filepath)
	{
		this(basepath, new ResourceLocation(filepath));
	}

	public ResourceLocation(String filepath)
	{
		StringBuilder sb = new StringBuilder(filepath);

		String[] path = FileUtil.getPath(filepath);
		for (String folder : path)
		{
			if (folder == null || folder.equals("")) continue;
			if (sb.length() > 0) sb.append(FileUtil.SEPERATOR);
			sb.append(folder);
		}

		this.filepath = filepath;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ResourceLocation)
		{
			return this.getFilePath().equals(((ResourceLocation) obj).getFilePath());
		}

		return super.equals(obj);
	}

	@Override
	public String toString()
	{
		return this.getFilePath();
	}
}
