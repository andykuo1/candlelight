package net.jimboi.test.sleuth.data;

/**
 * Created by Andy on 9/23/17.
 */
public enum Occupation
{
	DOCTOR(EducationLevel.DOCTORAL),
	SCIENTIST(EducationLevel.DOCTORAL),
	LAWYER(EducationLevel.UNIVERSITY),
	ACCOUNTANT(EducationLevel.COLLEGE),
	WRITER(EducationLevel.COLLEGE),
	NEWS_REPORTER(EducationLevel.COLLEGE),
	COMPUTER_PROGRAMMER(EducationLevel.COLLEGE),
	ELECTRICIAN(EducationLevel.COLLEGE),
	TEACHER(EducationLevel.COLLEGE),
	STUDENT(EducationLevel.HIGHSCHOOL),
	MANAGER(EducationLevel.BASIC),
	BARTENDER(EducationLevel.BASIC),
	UNEMPLOYED(EducationLevel.BASIC),
	CHEF(EducationLevel.BASIC),
	SALESMAN(EducationLevel.BASIC),
	PLUMBER(EducationLevel.BASIC),
	MAID(EducationLevel.BASIC),
	DANCER(EducationLevel.BASIC),
	TAXI_DRIVER(EducationLevel.BASIC),
	BUS_DRIVER(EducationLevel.BASIC),
	PAINTER(EducationLevel.BASIC),
	CAR_MECHANIC(EducationLevel.BASIC),
	FACTORY_WORKER(EducationLevel.BASIC);

	private final EducationLevel education;

	Occupation(EducationLevel education)
	{
		this.education = education;
	}

	public boolean isCompatibleEducationLevel(EducationLevel education)
	{
		return this.education.compareTo(education) <= 0;
	}
}
