package canary.qsilver.locale;

public class I18N
{
	private static Locale locale;

	protected static void setLocale(Locale locale)
	{
		I18N.locale = locale;
	}

	public static Locale getLocale()
	{
		return locale;
	}

	public static String of(String str, Object...formats)
	{
		return locale == null ? str : locale.format(str, formats);
	}
}
