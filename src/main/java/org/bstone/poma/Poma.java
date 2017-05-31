package org.bstone.poma;

/**
 * Created by Andy on 10/21/16.
 */

public class Poma
{
	public static final boolean __DEBUG = true; //"true".equals(System.getProperty("debug"));

	static
	{
		System.setOut(new PomaStream(System.out, false, __DEBUG));
		System.setErr(new PomaStream(System.err, true, true));
	}

	public static void ASSERT(boolean condition)
	{
		if (!__DEBUG) return;

		if (!condition)
		{
			StackTraceElement stackTraceElement = getStackTrace(2);
			String className = stackTraceElement.getClassName();
			int lineNumber = stackTraceElement.getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") Expected true, but was " + condition);
		}
	}

	public static void ASSERT(boolean condition, String message)
	{
		if (!__DEBUG) return;

		if (!condition)
		{
			StackTraceElement stackTraceElement = getStackTrace(2);
			String className = stackTraceElement.getClassName();
			int lineNumber = stackTraceElement.getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") " + message);
		}
	}

	public static void ASSERT(boolean condition, Object message)
	{
		if (!__DEBUG) return;

		if (!condition)
		{
			StackTraceElement stackTraceElement = getStackTrace(2);
			String className = stackTraceElement.getClassName();
			int lineNumber = stackTraceElement.getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") " + (message == null ? "null" : message.toString()));
		}
	}

	public static <T> void ASSERT_INST_AS(Object arg, Class<T> type, String message)
	{
		if (!__DEBUG) return;

		String s = null;

		if (arg == null)
		{
			s = "Cannot be null";
		}
		else if (!type.isAssignableFrom(arg.getClass()))
		{
			s = "Invalid type! Must be '" + type.getCanonicalName() + "'" + ", found '" + arg.getClass().getCanonicalName() + "' instead";
		}

		if (s != null)
		{
			StackTraceElement stackTraceElement = getStackTrace(2);
			String className = stackTraceElement.getClassName();
			int lineNumber = stackTraceElement.getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") " + s + " - " + message);
		}
	}

	public static void OUT(Object msg)
	{
		System.out.println(msg);
	}

	public static void OUT()
	{
		System.out.println();
	}

	public static void i(Object msg)
	{
		System.out.println(msg);
	}

	public static void d(Object msg)
	{
		if (!__DEBUG) return;
		System.out.println(msg);
	}

	public static void e(Object msg)
	{
		System.err.println(msg);
	}

	public static void e(Object msg, Throwable throwable)
	{
		System.err.println(msg);
		throwable.printStackTrace();
	}

	public static String tag()
	{
		StackTraceElement traceElement = Poma.getStackTrace(4);
		String className = traceElement.getClassName();
		return className.substring(className.lastIndexOf('.') + 1);
	}

	protected static StackTraceElement getStackTrace(int stackIndex)
	{
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackIndex >= stackTraceElements.length - 1) stackIndex = stackTraceElements.length - 2;
		return Thread.currentThread().getStackTrace()[stackIndex + 1];
	}
}