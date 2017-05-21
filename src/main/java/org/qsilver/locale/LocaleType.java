package org.qsilver.locale;

public enum LocaleType
{
	EN_US("en", "US");

	private final String language;
	private final String country;

	LocaleType(String language, String country)
	{
		this.language = language;
		this.country = country;
	}

	public String getLanguageCode()
	{
		return this.language;
	}

	public String getCountryCode()
	{
		return this.country;
	}

	public boolean equals(LocaleType type)
	{
		return type != null && (this.language.equals(type.language) && this.country.equals(type.country));
	}
}
