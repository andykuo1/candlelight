package canary.test.sleuth.data;

/**
 * Created by Andy on 9/23/17.
 */
public enum WeekDay
{
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY;

	public WeekDay next()
	{
		final WeekDay[] values = values();
		final int i = this.ordinal();
		return values[i == values.length - 1 ? 0 : i + 1];
	}

	public WeekDay previous()
	{
		final WeekDay[] values = values();
		final int i = this.ordinal();
		return values[i == 0 ? values.length - 1 : i - 1];
	}

	public boolean isWeekEnd()
	{
		return this == SATURDAY || this == SUNDAY;
	}
}
