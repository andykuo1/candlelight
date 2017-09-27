package net.jimboi.test.sleuth;

import net.jimboi.test.sleuth.data.CriminalConviction;
import net.jimboi.test.sleuth.data.EducationLevel;
import net.jimboi.test.sleuth.data.Gender;
import net.jimboi.test.sleuth.data.MaritalStatus;
import net.jimboi.test.sleuth.data.Occupation;
import net.jimboi.test.sleuth.data.WeekDay;
import net.jimboi.test.sleuth.util.WeightedSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 9/23/17.
 */
public class ActorFactory
{
	public static Actor create(Random rand, boolean iscrime)
	{
		Actor actor = new Actor();
		actor.gender = getRandomGender(rand);
		actor.name = getRandomNameByGender(rand, actor.gender == Gender.MALE);
		actor.attributes = new HashSet<>();
		for(int i = 0; i < 4; ++i)
		{
			String attrib = ATTRIBUTES[rand.nextInt(ATTRIBUTES.length)];
			if (actor.attributes.contains(attrib) || actor.attributes.contains("No " + attrib)) continue;
			actor.attributes.add((i >= 2 ? "No " : "") + attrib);
		}
		actor.education = EDUCATIONS.getRandomWeightedValue(rand);
		actor.criminalRecord = CONVICTIONS.getRandomWeightedValue(rand);
		actor.militaryService = getRandomMilitaryService(rand);
		actor.maritalStatus = getRandomMaritalStatus(rand, iscrime);
		actor.work = getRandomOccupation(rand, actor.education);
		actor.vehicle = VEHICLES.getRandomWeightedValue(rand);
		actor.weeklyActivity = getRandomWeeklyActivity(rand, actor);

		//actor.familyType = "???";
		//actor.behavior = "???";
		//actor.socialInterests = "???";
		//actor.personality = "???";
		//actor.intelligence = rand.nextInt(10) + 1;

		return actor;
	}

	private static Gender getRandomGender(Random rand)
	{
		return rand.nextBoolean() ? Gender.MALE : Gender.FEMALE;
	}

	private static String getRandomNameByGender(Random rand, boolean ismale)
	{
		return ismale ? NAMES_MALE[rand.nextInt(NAMES_MALE.length)] : NAMES_FEMALE[rand.nextInt(NAMES_FEMALE.length)];
	}

	private static MaritalStatus getRandomMaritalStatus(Random rand, boolean iscrime)
	{
		return iscrime ? MARITALSCRIME.getRandomWeightedValue(rand) : MARITALS.getRandomWeightedValue(rand);
	}

	private static Occupation getRandomOccupation(Random rand, EducationLevel education)
	{
		List<Occupation> occupations = new ArrayList<>();
		for(Occupation occupation : Occupation.values())
		{
			if (occupation.isCompatibleEducationLevel(education))
			{
				occupations.add(occupation);
			}
		}
		return occupations.get(rand.nextInt(occupations.size()));
	}

	private static String getRandomMilitaryService(Random rand)
	{
		return rand.nextInt(10) == 0 ? "Yes" : "No";
	}

	private static TimeTable getRandomWeeklyActivity(Random rand, Actor actor)
	{
		TimeTable table = new TimeTable();
		for(WeekDay day : WeekDay.values())
		{
			table.add("Work", 8, 8, day);
			table.add("Sleep", 23, 8, day);
		}
		return table;
	}

	public static final WeightedSet<CriminalConviction> CONVICTIONS = new WeightedSet<>();
	static
	{
		CONVICTIONS.add(CriminalConviction.MURDER, 6);
		CONVICTIONS.add(CriminalConviction.RAPE, 20);
		CONVICTIONS.add(CriminalConviction.ASSAULT, 40);
		CONVICTIONS.add(CriminalConviction.ROBBERY, 40);
		CONVICTIONS.add(CriminalConviction.BURGLARY, 90);
		CONVICTIONS.add(CriminalConviction.NONE, 1000);
	}

	public static final WeightedSet<EducationLevel> EDUCATIONS = new WeightedSet<>();
	static
	{
		EDUCATIONS.add(EducationLevel.DOCTORAL, 3 + 2);
		EDUCATIONS.add(EducationLevel.UNIVERSITY, 18 + 4);
		EDUCATIONS.add(EducationLevel.COLLEGE, 1);
		EDUCATIONS.add(EducationLevel.HIGHSCHOOL, 39);
		EDUCATIONS.add(EducationLevel.BASIC, 27);
	}

	public static final WeightedSet<MaritalStatus> MARITALS = new WeightedSet<>();
	static
	{
		MARITALS.add(MaritalStatus.SINGLE, 64);
		MARITALS.add(MaritalStatus.MARRIED, 93);
		MARITALS.add(MaritalStatus.SEPARATED, 10);
		MARITALS.add(MaritalStatus.DIVORCED, 22);
		MARITALS.add(MaritalStatus.WIDOWED, 12);
	}

	public static final WeightedSet<MaritalStatus> MARITALSCRIME = new WeightedSet<>();
	static
	{
		MARITALSCRIME.add(MaritalStatus.SINGLE, 41);
		MARITALSCRIME.add(MaritalStatus.MARRIED, 14);
		MARITALSCRIME.add(MaritalStatus.SEPARATED, 43);
		MARITALSCRIME.add(MaritalStatus.DIVORCED, 37);
		MARITALSCRIME.add(MaritalStatus.WIDOWED, 40);
	}

	public static final WeightedSet<String> VEHICLES = new WeightedSet<>();
	static
	{
		VEHICLES.add("None", 10);
		VEHICLES.add("1", 33);
		VEHICLES.add("2", 40);
		VEHICLES.add("3", 16);
		VEHICLES.add("4", 5);
		VEHICLES.add("5 or More", 3);
	}

	private static final String[] ATTRIBUTES = new String[] {
			"Alertness",
			"Attentiveness",
			"Availability",
			"Benevolence",
			"Boldness",
			"Cautiousness",
			"Compassion",
			"Contentment",
			"Creativity",
			"Decisiveness",
			"Deference",
			"Dependability",
			"Determination",
			"Diligence",
			"Discernment",
			"Discretion",
			"Endurance",
			"Enthusiasm",
			"Faith",
			"Flexibility",
			"Forgiveness",
			"Generosity",
			"Gentleness",
			"Gratefulness",
			"Honor",
			"Hospitality",
			"Humility",
			"Initiative",
			"Joyfulness",
			"Justice",
			"Loyalty",
			"Meekness",
			"Obedience",
			"Orderliness",
			"Patience",
			"Persuasiveness",
			"Punctuality",
			"Resourcefulness",
			"Responsibility",
			"Security",
			"Self-Control",
			"Sensitivity",
			"Sincerity",
			"Thoroughness",
			"Thriftiness",
			"Tolerance",
			"Virtue",
			"Wisdom"
	};

	private static final String[] NAMES_FEMALE = new String[] {
			"Jessica", "Julia", "Jasmine", "Julie", "June", "Jane", "Jade", "Jay", "Jennifer",
			"Judy", "Jenna", "Jessie", "Josephine", "Josie", "Justine", "Judith", "Joyce", "Joy",
			"Jordan", "Jodi", "Jodie", "Joanna", "Johanna", "Joan", "Jo"
	};

	private static final String[] NAMES_MALE = new String[] {
			"Jack", "Jacob", "James", "Joshua", "Jake", "Jamie", "Jerry", "Jude", "Jorge", "Jesus",
			"John", "Jackson", "Joe", "Joel", "Jaiden", "Jay", "Jason", "Jeremiah", "Jeremy",
			"Joey", "Johnathan", "Justin", "Julian"
	};
}
