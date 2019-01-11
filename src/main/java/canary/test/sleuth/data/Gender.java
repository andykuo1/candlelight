package canary.test.sleuth.data;

/**
 * Created by Andy on 9/23/17.
 */
public enum Gender
{
	MALE,
	FEMALE;

	@Override
	public final String toString()
	{
		return this == MALE ? "Male" : "Female";
	}
}
