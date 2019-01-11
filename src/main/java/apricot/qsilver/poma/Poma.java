package apricot.qsilver.poma;

/**
 * Created by Andy on 10/21/16.
 */

public class Poma
{
	public static final boolean __DEBUG = true; //"true".equals(System.getProperty("debug"));

	public static final PomaStream out = new PomaStream(System.out);
	public static final PomaStream err = new PomaStream(System.err);

	static
	{
		out.setWrapMode(PomaStream.WrapMode.WRAP);
		err.setWrapMode(PomaStream.WrapMode.NONE).setFormat(PomaStream.Format.IMPORTANT).removeFormat(PomaStream.Format.LINETRACE);
	}

	public static void makeSystemLogger()
	{
		System.setOut(Poma.out);
		System.setErr(Poma.err);
	}

	public static void ASSERT(boolean condition)
	{
		if (!__DEBUG) return;

		if (!condition)
		{
			StackTraceElement stackTraceElement = getCallerStackTrace();
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
			StackTraceElement stackTraceElement = getCallerStackTrace();
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
			StackTraceElement stackTraceElement = getCallerStackTrace();
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
			StackTraceElement stackTraceElement = getCallerStackTrace();
			String className = stackTraceElement.getClassName();
			int lineNumber = stackTraceElement.getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") " + s + " - " + message);
		}
	}

	public static void info(Object x)
	{
		out.ignore(PomaStream.Format.LINETRACE).println(String.valueOf(x), 1);
	}

	public static void debug(Object x)
	{
		if (!Poma.__DEBUG) return;
		out.println(" #: " + String.valueOf(x), 1);
	}

	public static void warn(Object x)
	{
		out.format(PomaStream.Format.IMPORTANT).println(String.valueOf(x), 1);
	}

	public static void div()
	{
		out.printdiv();
	}

	private static StackTraceElement getCallerStackTrace()
	{
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		return stacktrace[stacktrace.length - 2];
	}
}