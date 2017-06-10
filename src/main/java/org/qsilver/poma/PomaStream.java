package org.qsilver.poma;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * Created by Andy on 10/21/16.
 */

final class PomaStream extends PrintStream
{
	private boolean attention = false;
	private boolean trace = false;

	public PomaStream(OutputStream out, boolean attention, boolean trace)
	{
		super(out);
		this.attention = attention;
		this.trace = trace;
	}

	@Override
	public void println(String string)
	{
		Date date = new Date();
		Thread thread = Thread.currentThread();
		StackTraceElement traceElement = Poma.getStackTrace(4);
		String className = traceElement.getClassName();
		if (className.contains("Poma"))
		{
			traceElement = Poma.getStackTrace(5);
			className = traceElement.getClassName();
		}
		className = className.substring(className.lastIndexOf('.') + 1);

		StringBuilder sb = new StringBuilder();
		sb.append("[").append(date).append("]");
		sb.append("[").append(thread.getName()).append("]");
		sb.append("[").append(className).append("]");
		sb.append(" :");

		if (attention)
		{
			sb.append(" >>");
		}

		sb.append(" ");
		sb.append(string);

		if (trace)
		{
			sb.append(" >> ");
			sb.append(Poma.getStackTrace(4));
		}

		super.println(sb.toString());
	}
}
