package boron.qsilver.poma;

import boron.bstone.util.small.SmallSet;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Created by Andy on 10/21/16.
 */

final class PomaStream extends PrintStream
{
	public enum Format
	{
		IMPORTANT,
		DATE,
		TIME,
		THREAD,
		CALLER,
		LINETRACE
	}

	public enum WrapMode
	{
		NONE,
		WRAP,
		FADE
	}

	private static final int PRINT_MARGIN = 200;
	private static final String PRINT_TAB = "          ";
	private static final String PRINT_FADE = "... ";
	private static final String PRINT_DIVIDER = "-=(*)=-";

	private final Set<Format> formats = new SmallSet<>();
	private final Set<Format> nexts = new SmallSet<>();
	private final Set<Format> ignores = new SmallSet<>();
	private WrapMode wrapMode = WrapMode.NONE;

	public PomaStream(OutputStream out)
	{
		super(out);

		this.setFormat(Format.DATE, Format.TIME, Format.THREAD, Format.CALLER, Format.LINETRACE);
	}

	public PomaStream setFormat(Format... modes)
	{
		synchronized (this)
		{
			Collections.addAll(this.formats, modes);
		}
		return this;
	}

	public PomaStream removeFormat(Format format)
	{
		synchronized (this)
		{
			this.formats.remove(format);
		}
		return this;
	}

	public boolean hasFormat(Format format)
	{
		return (this.formats.contains(format) || this.nexts.contains(format)) && !this.ignores.contains(format);
	}

	public PomaStream clearFormat()
	{
		synchronized (this)
		{
			this.formats.clear();
		}
		return this;
	}

	public PomaStream setWrapMode(WrapMode wrapMode)
	{
		synchronized (this)
		{
			this.wrapMode = wrapMode == null ? WrapMode.NONE : wrapMode;
		}
		return this;
	}

	public WrapMode getWrapMode()
	{
		return this.wrapMode;
	}

	protected void addDateTime(StringBuilder sb, boolean date, boolean time)
	{
		if (!date && !time) return;

		String format = null;
		if (date)
		{
			format = "MMM-dd-yyyy";
		}
		if (time)
		{
			format = format != null ? format + " " : "";
			format += "HH:mm:ss";
		}
		DateFormat dateFormat = new SimpleDateFormat(format);
		sb.append('[');
		sb.append(dateFormat.format(new Date()));
		sb.append(']');
	}

	protected void addThreadName(StringBuilder sb)
	{
		Thread thread = Thread.currentThread();
		sb.append('[');
		sb.append(thread.getName());
		sb.append(']');
	}

	protected void addCallerName(StringBuilder sb, StackTraceElement stacktrace)
	{
		sb.append('[');
		String className = stacktrace.getClassName();
		sb.append(className.substring(className.lastIndexOf('.') + 1));
		sb.append(']');
	}

	protected void addLineTrace(StringBuilder sb, StackTraceElement stacktrace)
	{
		sb.append("@");
		sb.append(' ');
		sb.append(stacktrace);
		sb.append(' ');
	}

	protected void addWhiteSpace(StringBuilder sb, int length)
	{
		final int len = PRINT_TAB.length() - 1;
		for (int i = length; i >= 0; --i)
		{
			if (i > len)
			{
				sb.append(PRINT_TAB);
				i -= len;
			}
			else
			{
				sb.append(' ');
			}
		}
	}

	protected void addDivider(StringBuilder sb, char div, int length)
	{
		StringBuilder buffer = new StringBuilder();
		for (int i = length - PRINT_DIVIDER.length() - PRINT_DIVIDER.length() / 2 - 1; i >= 0; --i)
			buffer.append(div);
		sb.append('\n');
		sb.append(PRINT_DIVIDER);
		sb.append(buffer);
		sb.append(PRINT_DIVIDER);
		sb.append("\n");
	}

	private StackTraceElement getCallerStackTrace(int depth)
	{
		Thread thread = Thread.currentThread();
		StackTraceElement[] stackTraceElements = thread.getStackTrace();
		return stackTraceElements[3 + depth];
	}

	public PomaStream format(Format... formats)
	{
		Collections.addAll(this.nexts, formats);
		return this;
	}

	public PomaStream ignore(Format... formats)
	{
		Collections.addAll(this.ignores, formats);
		return this;
	}

	protected void printdiv()
	{
		StringBuilder sb = new StringBuilder();
		this.addDivider(sb, '=', PRINT_MARGIN);
		super.println(sb.toString());
	}

	protected void println(String x, int depth)
	{
		boolean date;
		boolean time;
		boolean thread;
		boolean caller;
		boolean important;
		boolean linetrace;
		WrapMode wrapMode;

		synchronized (this)
		{
			date = this.hasFormat(Format.DATE);
			time = this.hasFormat(Format.TIME);
			thread = this.hasFormat(Format.THREAD);
			caller = this.hasFormat(Format.CALLER);
			important = this.hasFormat(Format.IMPORTANT);
			linetrace = this.hasFormat(Format.LINETRACE);
			wrapMode = this.getWrapMode();

			this.nexts.clear();
			this.ignores.clear();
		}

		StringBuilder sb = new StringBuilder();
		StackTraceElement stacktrace = this.getCallerStackTrace(depth);

		this.addDateTime(sb, date, time);
		if (thread) this.addThreadName(sb);
		if (caller) this.addCallerName(sb, stacktrace);
		if (important) sb.append(" (!) => ");
		else sb.append(": ");

		//TODO: format newlines in x correctly!
		String[] xs = x.split("\n");
		x = xs[0];
		switch (wrapMode)
		{
			case FADE:
			{
				int diff = PRINT_MARGIN - sb.length() - x.length();
				if (diff > 0)
				{
					sb.append(x);
					this.addWhiteSpace(sb, diff);
				}
				else
				{
					sb.append(x.substring(0, PRINT_MARGIN - sb.length() - PRINT_FADE.length() + 1));
					sb.append(PRINT_FADE);
				}
				if (linetrace) this.addLineTrace(sb, stacktrace);
			}
			break;
			case WRAP:
			{
				int startMargin = sb.length() - 1;
				int index = 0;
				int diff = PRINT_MARGIN - sb.length();
				if (diff < 0) diff = 0;

				boolean first = true;
				while (index < x.length())
				{
					if (diff > 0)
					{
						int i = x.length() - index - diff;
						if (i < 0)
						{
							sb.append(x.substring(index));
							this.addWhiteSpace(sb, i);
							index = x.length();
							break;
						}
						else
						{
							sb.append(x.substring(index, index + diff));
							index += diff;
							diff = 0;
						}
					}
					else
					{
						//NEW LINE
						if (first)
						{
							first = false;
							sb.append(' ');
							if (linetrace) this.addLineTrace(sb, stacktrace);
						}

						sb.append("\n");
						this.addWhiteSpace(sb, startMargin - 2);
						sb.append("- ");
						diff = PRINT_MARGIN - startMargin - 1;
					}
				}

				if (first)
				{
					this.addWhiteSpace(sb, PRINT_MARGIN - startMargin - 1 - index - 1);
					if (linetrace) this.addLineTrace(sb, stacktrace);
				}
			}
			break;
			case NONE:
			default:
			{
				sb.append(x);
				sb.append(' ');
				if (linetrace) this.addLineTrace(sb, stacktrace);
			}
			break;
		}

		super.println(sb.toString());

		for (int i = 1; i < xs.length; ++i)
		{
			super.println(xs[i]);
		}
	}

	@Override
	public void println(String x)
	{
		this.println(x, 1);
	}

	@Override
	public void println(Object x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(boolean x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(char x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(int x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(long x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(float x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(double x)
	{
		this.println(String.valueOf(x), 1);
	}

	@Override
	public void println(char[] x)
	{
		this.println(String.valueOf(x), 1);
	}
}
