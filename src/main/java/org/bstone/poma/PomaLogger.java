package org.bstone.poma;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andy on 1/18/18.
 */
public class PomaLogger implements Logger
{
	private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
	private static final boolean USE_FULL_DATE = false;

	private final Thread thread = Thread.currentThread();
	private final String name;

	public PomaLogger(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public boolean isTraceEnabled()
	{
		return true;
	}

	@Override
	public void trace(String msg)
	{
		this.log(Level.TRACE, msg, null);
	}

	@Override
	public void trace(String format, Object arg)
	{
		this.flog(Level.TRACE, format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2)
	{
		this.flog(Level.TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments)
	{
		this.flogArray(Level.TRACE, format, arguments);
	}

	@Override
	public void trace(String msg, Throwable t)
	{
		this.log(Level.TRACE, msg, t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void trace(Marker marker, String msg) {}

	@Override
	public void trace(Marker marker, String format, Object arg) {}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {}

	@Override
	public boolean isDebugEnabled()
	{
		return true;
	}

	@Override
	public void debug(String msg)
	{
		this.log(Level.DEBUG, msg, null);
	}

	@Override
	public void debug(String format, Object arg)
	{
		this.flog(Level.DEBUG, format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2)
	{
		this.flog(Level.DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments)
	{
		this.flogArray(Level.DEBUG, format, arguments);
	}

	@Override
	public void debug(String msg, Throwable t)
	{
		this.log(Level.DEBUG, msg, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void debug(Marker marker, String msg) {}

	@Override
	public void debug(Marker marker, String format, Object arg) {}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {}
	@Override
	public void debug(Marker marker, String format, Object... arguments) {}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {}

	@Override
	public boolean isInfoEnabled()
	{
		return true;
	}

	@Override
	public void info(String msg)
	{
		this.log(Level.INFO, msg, null);
	}

	@Override
	public void info(String format, Object arg)
	{
		this.flog(Level.INFO, format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2)
	{
		this.flog(Level.INFO, format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments)
	{
		this.flogArray(Level.INFO, format, arguments);
	}

	@Override
	public void info(String msg, Throwable t)
	{
		this.log(Level.INFO, msg, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void info(Marker marker, String msg) {}

	@Override
	public void info(Marker marker, String format, Object arg) {}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {}

	@Override
	public void info(Marker marker, String format, Object... arguments) {}

	@Override
	public void info(Marker marker, String msg, Throwable t) {}

	@Override
	public boolean isWarnEnabled()
	{
		return true;
	}

	@Override
	public void warn(String msg)
	{
		this.log(Level.WARN, msg, null);
	}

	@Override
	public void warn(String format, Object arg)
	{
		this.flog(Level.WARN, format, arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2)
	{
		this.flog(Level.WARN, format, arg1, arg2);
	}

	@Override
	public void warn(String format, Object... arguments)
	{
		this.flogArray(Level.WARN, format, arguments);
	}

	@Override
	public void warn(String msg, Throwable t)
	{
		this.log(Level.WARN, msg, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void warn(Marker marker, String msg) {}

	@Override
	public void warn(Marker marker, String format, Object arg) {}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {}

	@Override
	public boolean isErrorEnabled()
	{
		return true;
	}

	@Override
	public void error(String msg)
	{
		this.log(Level.ERROR, msg, null);
	}

	@Override
	public void error(String format, Object arg)
	{
		this.flog(Level.ERROR, format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2)
	{
		this.flog(Level.ERROR, format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments)
	{
		this.flogArray(Level.ERROR, format, arguments);
	}

	@Override
	public void error(String msg, Throwable t)
	{
		this.log(Level.ERROR, msg, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker)
	{
		return false;
	}

	@Override
	public void error(Marker marker, String msg) {}

	@Override
	public void error(Marker marker, String format, Object arg) {}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {}

	@Override
	public void error(Marker marker, String format, Object... arguments) {}

	@Override
	public void error(Marker marker, String msg, Throwable t) {}

	public void precondition(boolean condition)
	{

	}

	private void flog(Level level, String format, Object arg)
	{
		FormattingTuple tp = MessageFormatter.format(format, arg);
		this.log(level, tp.getMessage(), tp.getThrowable());
	}

	private void flog(Level level, String format, Object arg1, Object arg2)
	{
		FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
		this.log(level, tp.getMessage(), tp.getThrowable());
	}

	private void flogArray(Level level, String format, Object[] args)
	{
		FormattingTuple tp = MessageFormatter.arrayFormat(format, args);
		this.log(level, tp.getMessage(), tp.getThrowable());
	}

	private void log(Level level, String msg, Throwable t)
	{
		StringBuffer sb = new StringBuffer(32);

		sb.append("[");
		sb.append(getFormattedDate());
		sb.append("] ");

		sb.append("[");
		sb.append(this.thread.getName());
		sb.append("/");
		sb.append(level);
		sb.append("] ");

		sb.append("[");
		sb.append(parseShortName(this.name));
		sb.append("]");
		sb.append(": ");

		if (level == Level.WARN)
		{
			sb.append("(!) => ");
		}

		sb.append(msg);

		if (level == Level.TRACE)
		{
			sb.append("\t");
			sb.append("@ ");
			sb.append(getCallerStackTrace(1));
		}

		this.write(sb, t, level.error);
	}

	private void write(StringBuffer sb, Throwable t, boolean error)
	{
		PrintStream stream = error ? System.err : System.out;
		stream.println(sb.toString());
		if (t != null) t.printStackTrace(stream);
		stream.flush();
	}

	private StackTraceElement getCallerStackTrace(int depth)
	{
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTraceElements = thread.getStackTrace();
		return stackTraceElements[3 + depth];
	}

	private static String parseShortName(String name)
	{
		return name.substring(name.lastIndexOf(".") + 1);
	}

	private static String getFormattedDate()
	{
		return (USE_FULL_DATE ? FULL_DATE_FORMAT : DATE_FORMAT).format(new Date());
	}

	public enum Level
	{
		TRACE,
		DEBUG,
		INFO,
		WARN(true),
		ERROR(true);

		private final boolean error;

		Level()
		{
			this(false);
		}

		Level(boolean error)
		{
			this.error = error;
		}
	}
}
