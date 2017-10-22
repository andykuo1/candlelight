package net.jimboi.boron.base_ab.window.input;

/**
 * Created by Andy on 9/14/17.
 */
@Deprecated
public class TextHandler
{
	private StringBuffer buffer;

	public TextHandler()
	{
		this.buffer = new StringBuffer();
	}

	public void text(int codepoint)
	{
		this.buffer.append((char) codepoint);
	}

	public void backspace()
	{
		if (this.buffer.length() > 0)
		{
			this.buffer.setLength(this.buffer.length() - 1);
		}
	}

	public StringBuffer getBuffer()
	{
		return this.buffer;
	}

	public String get()
	{
		return this.buffer.toString();
	}

	public void clear()
	{
		this.buffer.setLength(0);
	}
}
