package canary.test.sleuth.data;

/**
 * Created by Andy on 9/23/17.
 */
public class Time implements Comparable<Time>
{
	public static final int HOURS_IN_DAY = 24;
	public static final int DAYS_IN_WEEK = 7;
	public static final int WEEKS_IN_SEASON = 12;
	public static final int SEASONS_IN_YEAR = 4;

	public static final int HOURS_IN_WEEK = DAYS_IN_WEEK * HOURS_IN_DAY;
	public static final int HOURS_IN_SEASON = WEEKS_IN_SEASON * HOURS_IN_WEEK;
	public static final int HOURS_IN_YEAR = SEASONS_IN_YEAR * HOURS_IN_SEASON;

	public int year;
	public Season season;
	public int week;
	public WeekDay day;
	public int hour;

	public Time(int year, Season season, int week, WeekDay day, int hour)
	{
		this.set(year, season, week, day, hour);
	}

	public Time(int years, int seasons, int weeks, int days, int hours)
	{
		this.set(years, seasons, weeks, days, hours);
	}

	public Time(int hours)
	{
		this(0, 0, 0, 0, hours);
	}

	public Time()
	{
		this(0);
	}

	public Time(Time time)
	{
		this.set(time);
	}

	public Time set(int year, Season season, int week, WeekDay day, int hour)
	{
		this.year = year;
		this.season = season;
		this.week = week;
		this.day = day;
		this.hour = hour;
		return this;
	}

	public Time set(int years, int seasons, int weeks, int days, int hours)
	{
		days += hours / HOURS_IN_DAY;
		weeks += days / DAYS_IN_WEEK;
		seasons += weeks / WEEKS_IN_SEASON;
		years += seasons / SEASONS_IN_YEAR;

		this.year = years;
		this.season = Season.values()[seasons % SEASONS_IN_YEAR];
		this.week = weeks % WEEKS_IN_SEASON;
		this.day = WeekDay.values()[days % DAYS_IN_WEEK];
		this.hour = hours % HOURS_IN_DAY;

		return this;
	}

	public Time set(int hours)
	{
		return this.set(0, 0, 0, 0, hours);
	}

	public Time set(Time time)
	{
		this.year = time.year;
		this.season = time.season;
		this.week = time.week;
		this.day = time.day;
		this.hour = time.hour;
		return this;
	}

	public Time add(int hours, Time dst)
	{
		return dst.set(0, 0, 0, 0, this.getTotalHours() + hours);
	}

	public Time add(Time offset, Time dst)
	{
		return dst.set(this.year + offset.year,
				this.season.ordinal() + offset.season.ordinal(),
				this.week + offset.week,
				this.day.ordinal() + offset.day.ordinal(),
				this.hour + offset.hour);
	}

	public Time sub(Time offset, Time dst)
	{
		return dst.set(this.year - offset.year,
				this.season.ordinal() - offset.season.ordinal(),
				this.week - offset.week,
				this.day.ordinal() - offset.day.ordinal(),
				this.hour - offset.hour);
	}

	public int getHour()
	{
		return this.hour % HOURS_IN_DAY;
	}

	public int getTotalHours()
	{
		return this.year * HOURS_IN_YEAR + this.season.ordinal() * HOURS_IN_SEASON + this.week * HOURS_IN_WEEK + this.day.ordinal() * HOURS_IN_DAY + this.hour;
	}

	public boolean isDayTime()
	{
		int h = this.getHour();
		return h >= 6 && h <= 6;
	}

	public boolean isNightTime()
	{
		return !this.isDayTime();
	}

	@Override
	public int compareTo(Time o)
	{
		int diff = this.year - o.year;
		if (diff != 0) return diff;
		diff = this.season.compareTo(o.season);
		if (diff != 0) return diff;
		diff = this.week - o.week;
		if (diff != 0) return diff;
		diff = this.day.compareTo(o.day);
		if (diff != 0) return diff;
		return this.hour - o.hour;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Time)
		{
			Time o = (Time) obj;
			return this.year == o.year &&
					this.season == o.season &&
					this.week == o.week &&
					this.day == o.day &&
					this.hour == o.hour;
		}

		return false;
	}

	@Override
	public String toString()
	{
		return this.hour + ":00-" + this.day.name().substring(0, 3) + "." + this.week + "-" + this.season.name().substring(0, 3) + "." + this.year;
	}
}
