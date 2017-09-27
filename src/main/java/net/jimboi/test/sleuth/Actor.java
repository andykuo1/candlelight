package net.jimboi.test.sleuth;

import net.jimboi.test.sleuth.cluedo.MemoryBank;
import net.jimboi.test.sleuth.cluedo.Room;
import net.jimboi.test.sleuth.comm.Environment;
import net.jimboi.test.sleuth.data.CriminalConviction;
import net.jimboi.test.sleuth.data.EducationLevel;
import net.jimboi.test.sleuth.data.Gender;
import net.jimboi.test.sleuth.data.MaritalStatus;
import net.jimboi.test.sleuth.data.Occupation;

import java.util.Set;

/**
 * Created by Andy on 9/23/17.
 */
public class Actor extends SleuthStruct
{
	public String name;
	public Gender gender;
	public Set<String> attributes;
	public EducationLevel education;
	public CriminalConviction criminalRecord;
	public MaritalStatus maritalStatus;
	public Occupation work;
	public String vehicle;
	public String militaryService;

	public TimeTable weeklyActivity;

	public int stress = 0;
	public MemoryBank memories = new MemoryBank();
	public Room location;
	public Environment environment;

	//public String familyName;
	//public String familyType;

	//public String socialInterests;
	//public String personality;
	//public String behavior;
	//public String preoffenseBehavior = "";
	//	public String postoffenseBehavior = "";
	//	public String mentalDisorders = "???";
	//	public int stress;
	//	public int accuracy;
	//	public int intelligence;
	//	public String skills = "???";
	//ConsoleHelper.message(CONSOLE, "    - Stress: " + "inv_prep + crime_time + unexpected + action + attributes");
	//ConsoleHelper.message(CONSOLE, "    - Intelligence: " + "attributes + history");
	//ConsoleHelper.message(CONSOLE, "    - Accuracy: " + "attributes + inv_stress + skills");
	//ConsoleHelper.message(CONSOLE, "    - Skills: " + "history + attributes");

	Actor()
	{
	}
}
