package org.bstone.poma.motivator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 1/18/18.
 */
public class Poma
{
	public static void startUp()
	{
		if (!HEART.isAlive())
		{
			Runtime.getRuntime().addShutdownHook(END);
			BEGIN.start();
		}
	}

	public static void shutDown()
	{
		if (HEART.isAlive())
		{
			HEART.kill();
			Runtime.getRuntime().removeShutdownHook(END);
		}
	}

	public static void error(StringBuffer sb, Throwable t)
	{
		SELF.onEvaluateError(sb, t);
	}

	public static void say(String msg)
	{
		System.out.println(format(msg));
	}

	public static void say(String msg, StringBuffer sb)
	{
		sb.append("\n");
		sb.append(format(msg));
	}

	private static String format(String msg)
	{
		return "\t" + "(._.)" + " (" + msg + ")";
	}

	public static final Poma SELF = new Poma();
	private static final Heart HEART = new Heart(SELF);
	private static final Thread BEGIN = new Thread(HEART);
	private static final Thread END = new Thread(Poma.SELF::onStop);

	private static final String DIVIDER = "-=(*)=-";
	private static final Set<String> NONDIMINISHING_RETURNS = new HashSet<>();

	private float awareness;

	void onStart()
	{
		say(Util.GREETING.get(Util.RANDY));
	}

	void onStop()
	{
		say(Util.FAREWELL.get(Util.RANDY));
	}

	void onDeath()
	{
		say(Util.DEATH_CRY.get(Util.RANDY));
	}

	void onHeartBeat()
	{
		if (Util.RANDY.nextFloat() < 0.2)
		{
			say(Util.NOISE.get(Util.RANDY));

			if (Util.RANDY.nextFloat() < 0.3)
			{
				say("That one was good.");
			}
		}
	}

	void onHeartSkip()
	{
		say(Util.SURPRISE.get(Util.RANDY) + "I think my heart just skipped a beat... " + Util.REMARK.get(Util.RANDY));
	}

	void onEvaluateError(StringBuffer sb, Throwable t)
	{
		if (Util.RANDY.nextFloat() < 0.3)
		{
			say(Util.ERROR.get(Util.RANDY), sb);
		}
	}

	public boolean isDeveloperConcentrating()
	{
		return false;
	}

	public void motivate()
	{

	}

	public void poke()
	{

	}

	public void slap()
	{

	}

	public void shake()
	{

	}
}
