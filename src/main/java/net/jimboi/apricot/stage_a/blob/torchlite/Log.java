package net.jimboi.apricot.stage_a.blob.torchlite;

import java.io.PrintStream;

public final class Log
{
	public static boolean STACK_SPACING = true;
	public static int STACK_SPACING_OFFSET = -5;

	private Log() {}

	public static void ASSERT(boolean condition)
	{
		if (!condition)
		{
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String className = stackTraceElements[2].getClassName();
			int lineNumber = stackTraceElements[2].getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") Expected true, but was " + condition);
		}
	}

	public static void ASSERT(boolean condition, Object message)
	{
		if (!condition)
		{
			String s = message.toString();
			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String className = stackTraceElements[2].getClassName();
			int lineNumber = stackTraceElements[2].getLineNumber();
			throw new AssertionError("(" + className + ".java:" + lineNumber + ") Expected true, but was " + condition + (s.isEmpty() ? "" : ": " + s));
		}
	}

	public static void ASSERT_EQUALS(Object obj, Object other)
	{
		if  (obj == other) return;
		if (obj == null || other == null) ASSERT(false, obj + " != " + other);
		ASSERT(obj.equals(other), obj + " != " + other);
	}

	public static void ASSERT_NOT_EQUALS(Object obj, Object other)
	{
		if (obj != other) return;
		if (obj == null && other == null) ASSERT(false, obj + " == " + other);
		ASSERT(!obj.equals(other), obj + " == " + other);
	}

	public static final void O(Object... message)
	{
		String threadName = getThreadName();
		String className = getCallerClassName();

		print(System.out, "[" + threadName + "]" + "[" + className.substring(className.lastIndexOf('.') + 1) + "]", message);
	}

	public static final void E(Object... message)
	{
		String threadName = getThreadName();
		String className = getCallerClassName();

		print(System.err, "[" + threadName + "]" + "[" + className.substring(className.lastIndexOf('.') + 1) + "]", message);
	}

	public static final void E(Exception e)
	{
		STACKTRACE(e);
	}

	public static final void E(Exception e, Object... message)
	{
		STACKTRACE(e);
		E(message);
	}

	private static String getThreadName()
	{
		return Thread.currentThread().getName();
	}

	private static String getCallerClassName()
	{ 
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (int i = 1; i < elements.length; i++)
		{
			StackTraceElement ste = elements[i];
			if (!ste.getClassName().equals(Log.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				return ste.getClassName();
			}
		}
		return null;
	}

	private static final Object NULL = new Object();
	private static final String SEPARATOR = ", ";

	private static final void print(PrintStream printStream, Object[] message)
	{
		Object p = null;
		for(Object o : message)
		{
			if (o == null)
			{
				if (p == NULL) printStream.print(SEPARATOR);
			}
			else if (p != null && o.getClass().isAssignableFrom(p.getClass()) && !(o instanceof String))
			{
				printStream.print(SEPARATOR);
			}

			printStream.print(o.toString());
			p = o == null ? NULL : o;
		}

		printStream.println();
	}

	private static final void print(PrintStream printStream, String header, Object[] message)
	{
		if (STACK_SPACING)
		{
			printStream.print("[" + StringUtil.repeat("-", (Thread.currentThread().getStackTrace().length + STACK_SPACING_OFFSET)) + "]");
		}

		printStream.print(header + " : ");
		print(printStream, message);
	}

	public static final void STACKTRACE(Exception e)
	{
		e.printStackTrace();
	}

	public static final void STACKTRACE()
	{
		new Exception().printStackTrace();
	}

	public static final void EXIT(int status)
	{
		System.exit(status);
	}
}
