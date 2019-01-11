package canary.test.sleuth;

/**
 * Created by Andy on 9/23/17.
 */
public enum Motive
{
	SECRET("Hide a Secret", "Someone stumbles upon a closely guarded secret.", 0),
	//What is the secret?
	GREED("Greed", "Someone has a fortune, property or something of value.", 0),
	//What does the Murderer want that belongs to the Victim?
	REVENGE("Get Revenge", "Someone has a score to settle.", 1),
	//What did the Victim do to cross the Murderer?
	OBSESSION("For Obsession", "Sociopaths.", 1),
	//What strong belief did the Victim violate?
	LOVE("For Love, Sex & Jealousy", "Love Triangles.", 1),
	//When and how did the Murderer figure this out?
	//What is the Murderer's relationship with the Victim?
	//With whom is the affair?
	FURY("Fury of the Moment", "Someone snaps into a fit of rage.", 2),
	//What triggered the anger in the Murderer?
	MENTAL("Mental Disorder", "Detachment from society.", 0),
	//This is unexplainable...
	STATUS("Protect Personal Status", "Someone feels threatened.", 0),
	//How does the Victim threaten the Murderer
	RELATION("Protect Love Ones", "Someone close feels threatened.", 0),
	//How does the Murderer threaten someone else?
	//Why does the Murderer care about that someone?
	EMPATHY("Empathy", "Have Mercy.", 0);
	//What irreversible affliction causes the Victim to suffer?
	//How is this justified by the Murderer?

	//Usually, (to hurt) will have multiple wounds, weapons from the scene, and have history
	//Otherwise, usually will hide / dispose / clean crime
	//If scene is non-familiar, then it is because of external factors
	//Otherwise, almost always familiar places

	private final String name;
	private final String definition;
	private final int painLevel;

	Motive(String name, String definition, int painLevel)
	{
		this.name = name;
		this.definition = definition;
		this.painLevel = painLevel;
	}

	public int getPainLevel()
	{
		return this.painLevel;
	}

	public String getDefinition()
	{
		return this.definition + (this.painLevel > 0 ? " (to hurt)" : "");
	}

	@Override
	public final String toString()
	{
		return this.name;
	}
}
