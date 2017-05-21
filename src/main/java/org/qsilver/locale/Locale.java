package org.qsilver.locale;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Locale
{
	private static final List<Locale> LOCALES = new ArrayList<Locale>();

	public static Locale create(LocaleType type, Map<String, String> langMap, boolean unicode)
	{
		Locale locale = new Locale(type, langMap, unicode);
		LOCALES.add(locale);
		return locale;
	}

	public static Locale getLocale(LocaleType type)
	{
		return LOCALES.get(LOCALES.indexOf(new Locale(type, null, false)));
	}

	public static void bind(LocaleType type)
	{
		bind(getLocale(type));
	}

	public static void bind(Locale locale)
	{
		I18N.setLocale(locale);
	}

	public static void unbind()
	{
		I18N.setLocale(null);
	}

	public static void delete(Locale locale)
	{
		locale.langMap.clear();
	}

	public static void clear()
	{
		for(Locale locale : LOCALES)
		{
			delete(locale);
		}
	}

	private final LocaleType type;

	private final Map<String, String> langMap;
	private final boolean unicode;

	public Locale(LocaleType type, Map<String, String> langMap, boolean unicode)
	{
		this.type = type;

		this.langMap = langMap;
		this.unicode = unicode;
	}

	public String format(String key, Object... formats)
	{
		String local = this.langMap.get(key);
		return local == null ? null : String.format(local, formats);
	}

	public void put(String key, String value)
	{
		this.langMap.put(key, value);
	}

	public LocaleType getType()
	{
		return this.type;
	}

	public boolean isUnicode()
	{
		return this.unicode;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Locale)
		{
			return this.type == ((Locale)obj).type;
		}

		return super.equals(obj);
	}
}
