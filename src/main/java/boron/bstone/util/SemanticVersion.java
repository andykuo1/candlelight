package boron.bstone.util;

/**
 * Created by Andy on 6/12/17.
 */

public class SemanticVersion implements Comparable<SemanticVersion>
{
	public static boolean isSemanticVersion(String version)
	{
		int i = version.indexOf('.');
		if (i == 0) return false;
		String major = version.substring(0, i);
		int majori = 0;
		try
		{
			majori = Integer.parseInt(major);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		version = version.substring(i + 1);
		i = version.indexOf('.');
		if (i == 0) return false;
		String minor = version.substring(0, i);
		int minori = 0;
		try
		{
			minori = Integer.parseInt(minor);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		version = version.substring(i + 1);
		int versioni = 0;
		try
		{
			versioni = Integer.parseInt(version);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		return true;
	}

	private final int major;
	private final int minor;
	private final int fix;

	public SemanticVersion(String version)
	{
		int i = version.indexOf('.');
		if (i == 0)
			throw new IllegalArgumentException("Invalid version format - Missing '.' separators!");
		String major = version.substring(0, i);
		this.major = Integer.parseInt(major);

		version = version.substring(i + 1);
		i = version.indexOf('.');
		if (i == 0)
			throw new IllegalArgumentException("Invalid version format - Missing '.' separators!");
		String minor = version.substring(0, i);
		this.minor = Integer.parseInt(minor);

		version = version.substring(i + 1);
		this.fix = Integer.parseInt(version);
	}

	public SemanticVersion(int major, int minor, int fix)
	{
		this.major = major;
		this.minor = minor;
		this.fix = fix;
	}

	public boolean isCompatibleWith(SemanticVersion version)
	{
		return this.major == version.major && this.minor == version.minor;
	}

	@Override
	public String toString()
	{
		return this.major + "." + this.minor + "." + this.fix;
	}

	@Override
	public int compareTo(SemanticVersion o)
	{
		int i = this.major - o.major;
		if (i == 0)
		{
			i = this.minor - o.minor;
			if (i == 0)
			{
				i = this.fix - o.fix;
			}
		}
		return i;
	}
}
