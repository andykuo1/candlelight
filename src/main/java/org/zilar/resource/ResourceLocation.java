package org.zilar.resource;

public class ResourceLocation
{
	private static String DEFAULT_DOMAIN = null;

	public static void setDefaultDomain(String domain)
	{
		DEFAULT_DOMAIN = domain;
	}

	public static String getDefaultDomain()
	{
		return DEFAULT_DOMAIN;
	}

	private final String filepath;

	public ResourceLocation(String path)
	{
		StringBuilder sb = new StringBuilder();
		int colon = path.indexOf(':');
		int slash = path.indexOf('/');
		if (slash == 0)
		{
			if (colon != -1) throw new IllegalArgumentException("Invalid path - expected absolute path, but found domain identifier ':' for path: '" + path + "'!");

			sb.append(getMainDirectory());
			sb.append(path);
		}
		else if (colon == -1)
		{
			if (DEFAULT_DOMAIN == null)
			{
				throw new IllegalArgumentException("Invalid path - expected default domain path, but could not find default domain name for path: '" + path + "'!");
			}
			else
			{
				sb.append(getDomainDirectory(DEFAULT_DOMAIN));
				sb.append('/');
				sb.append(path);
			}
		}
		else
		{
			sb.append(getDomainDirectory(path.substring(0, colon)));
			if (colon + 2 < path.length())
			{
				sb.append('/');
				sb.append(path.substring(colon + 1));
			}
		}

		this.filepath = sb.toString();
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

	public static String getMainDirectory()
	{
		return System.getProperty("user.dir");
	}

	public static String getDomainDirectory(String domain)
	{
		if (!isPOSIXCompatibleString(domain))
		{
			throw new IllegalArgumentException("Invalid domain name - not POSIX compatible (refer to fully portable POSIX characters)!");
		}

		return getMainDirectory() + "/src/main/res/" + domain;
	}

	public static boolean isPOSIXCompatibleString(String string)
	{
		if (string.isEmpty()) return true;

		if (string.charAt(0) == '-') return false;
		for(int i = 1; i < string.length(); ++i)
		{
			char c = string.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '_' && c != '-')
			{
				return false;
			}
		}
		return true;
	}
}
