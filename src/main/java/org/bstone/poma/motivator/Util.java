package org.bstone.poma.motivator;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Andy on 1/18/18.
 */
class Util
{
	static final Random RANDY = new Random();

	static final NonDiminishingReturnSet WELLNESS = new NonDiminishingReturnSet()
			.add("Good")
			.add("Ok, ")
			.add("Happy")
			.add("Awesome")
			.add("Great")
			.add("Interesting");

	static final NonDiminishingReturnSet SURPRISE = new NonDiminishingReturnSet()
			.add("Woah.")
			.add("Woah!")
			.add("Hey!")
			.add("Dang!")
			.add("Yo!")
			.add("Hold up!")
			.add("Hold it!")
			.add("Wait a second.")
			.add("Hey.");

	static final NonDiminishingReturnSet REMARK = new NonDiminishingReturnSet()
			.add("Great.")
			.add("What did you do?")
			.add("Wonderful.")
			.add("Why would you do that?")
			.add("Just my luck.")
			.add("How inconvenient...")
			.add("Well, well...");

	static final NonDiminishingReturnSet GREETING = new NonDiminishingReturnSet()
			.add(() -> WELLNESS.get(RANDY) + " " + Util.getTimeOfDay() + "!")
			.add(() -> WELLNESS.get(RANDY) + " " + Util.getTimeOfDay() + ".")
			.add(() -> Util.getTimeOfDay() + ".")
			.add("Hi.")
			.add("Hello.")
			.add("Hey.");

	static final NonDiminishingReturnSet FAREWELL = new NonDiminishingReturnSet()
			.add("Good Bye!")
			.add("See ya!")
			.add("See you soon.")
			.add("Bye.")
			.add("Later.")
			.add("Adios.")
			.add("See you around!")
			.add("Until next time!")
			.add("Get outta here already!")
			.add("Go outside!")
			.add("Farewell, my darling.")
			.add("Farewell.")
			.add("Another day, then.")
			.add("Another time.")
			.add("Tomorrow then?")
			.add("How about later?");

	static final NonDiminishingReturnSet DEATH_CRY = new NonDiminishingReturnSet()
			.add("AHHHHHHHH--")
			.add("I love y--")
			.add("Oh, shi--")
			.add("Great.--")
			.add("...--")
			.add("--")
			.add("Really?--")
			.add("Again?--")
			.add("Sometimes, I lik--")
			.add("Nothing, says you care lik--")
			.add("NOOOO--")
			.add("Waitwaitwa--")
			.add("DON'T--")
			.add("Wait! NOOOOOO--");

	static final NonDiminishingReturnSet ERROR = new NonDiminishingReturnSet()
			.add("You better fix this.")
			.add("Good luck.")
			.add("I think you need a bigger console.")
			.add("I know you can do better.")
			.add("You can do it, come on.")
			.add("TOO. MANY. ERRORS.")
			.add("Sometimes, I just close my eyes...")
			.add("Woo! Errors for everyone!")
			.add("If you fix this, I'll give you a cookie.")
			.add("How about some cheese?")
			.add("Squash this bug! SQUASH IT!")
			.add("Hurry! It's getting away!");

	public static String getTimeOfDay()
	{
		Calendar c = Calendar.getInstance();
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

		if(timeOfDay >= 0 && timeOfDay < 12)
		{
			return "Morning";
		}
		else if(timeOfDay >= 12 && timeOfDay < 16)
		{
			return "Afternoon";
		}
		else if(timeOfDay >= 16 && timeOfDay < 21)
		{
			return "Evening";
		}
		else if(timeOfDay >= 21 && timeOfDay < 24)
		{
			return "Night";
		}

		return "...what time is it...";
	}
}
