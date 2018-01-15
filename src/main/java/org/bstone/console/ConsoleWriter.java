package org.bstone.console;

import org.bstone.console.action.ActionLink;
import org.bstone.console.internal.InternalConsoleListener;

import java.util.StringTokenizer;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Created by Andy on 12/3/17.
 */
public class ConsoleWriter
{
	public static boolean DEBUGMODE = true;

	private final StyledDocument document;
	private final int maxLength = 40;
	private int lineLength = 0;

	private InternalConsoleListener listener;

	private volatile double delayFactor = 1;

	public ConsoleWriter(StyledDocument document)
	{
		this.document = document;

		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setLineSpacing(attributeSet, 0.5F);
		this.document.setParagraphAttributes(0, this.document.getLength(), attributeSet, false);
	}

	public ConsoleWriter setListener(InternalConsoleListener listener)
	{
		this.listener = listener;
		return this;
	}

	public ConsoleWriter setTypeDelay(double factor)
	{
		this.delayFactor = factor;
		return this;
	}

	public void write(String text, javax.swing.text.AttributeSet attrib)
	{
		if (this.lineLength >= this.maxLength) this.newline();

		if (this.delayFactor != 0 && ActionLink.getActionFromAttributes(attrib) == null)
		{
			for(char c : text.toCharArray())
			{
				if (c == '\n')
				{
					this.newline();
					continue;
				}

				this.append("" + c, attrib);

				if (this.lineLength >= this.maxLength) this.newline();

				try
				{
					Thread.sleep((int) (this.getDelayByCharacter(c) * this.delayFactor));
				}
				catch (InterruptedException e)
				{
				}
			}
		}
		else
		{
			if (text.indexOf('\n') == -1)
			{
				this.writeWithinBounds(text, attrib);
			}
			else
			{
				StringTokenizer tokenizer = new StringTokenizer(text, "\n");
				while (tokenizer.hasMoreTokens())
				{
					this.writeWithinBounds(tokenizer.nextToken(), attrib);
					this.newline();
				}
			}
		}
	}

	private void writeWithinBounds(String text, javax.swing.text.AttributeSet attrib)
	{
		int remaining = this.maxLength - this.lineLength;
		while (text.length() > remaining)
		{
			String s = text.substring(0, remaining);
			text = text.substring(remaining);

			this.append(s, attrib);
			this.newline();
		}

		if (!text.isEmpty())
		{
			this.append(text, attrib);
		}
	}

	private void append(String text, javax.swing.text.AttributeSet attrib)
	{
		if (DEBUGMODE) System.out.print(text);

		try
		{
			this.document.insertString(this.document.getLength(), text, attrib);

			this.lineLength += text.length();
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		this.listener.onWriterAppend(text, attrib);
	}

	public void newline()
	{
		if (DEBUGMODE) System.out.println();

		try
		{
			this.document.insertString(this.document.getLength(), "\n", null);

			this.lineLength = 0;
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		this.listener.onWriterNewLine();
	}

	public void deleteLines(int lines)
	{
		String s = null;
		try
		{
			String text = this.document.getText(0, this.document.getLength());
			int newLineIndex = -1;
			for(int i = 0; i < lines; ++i)
			{
				int j = newLineIndex == -1 ? text.lastIndexOf('\n')
						: text.lastIndexOf('\n', newLineIndex - 1);
				if (j == -1) break;
				newLineIndex = j;
			}

			if (newLineIndex == -1) newLineIndex = 0;
			int remaining = text.length() - newLineIndex;
			s = text.substring(newLineIndex, remaining);
			this.document.remove(newLineIndex, remaining);

			int previous = text.lastIndexOf('\n', newLineIndex - 1);
			if (previous != -1)
			{
				this.lineLength = newLineIndex - previous - 1;
			}
			else
			{
				this.lineLength = newLineIndex;
			}
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		if (s != null)
		{
			this.listener.onWriterDeleteLine(s);
		}
	}

	public void deleteChars(int chars)
	{
		String s = null;
		try
		{
			int startIndex = this.document.getLength() - chars;
			String text = this.document.getText(startIndex, chars);
			s = text;
			int j = text.indexOf('\n');
			if (j != -1) throw new IllegalArgumentException("cannot delete newline");
			this.lineLength -= chars;
			if (this.lineLength < 0) throw new IllegalStateException("invalid previous newline index!");
			this.document.remove(startIndex, chars);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		if (s != null)
		{
			this.listener.onWriterDelete(s);
		}
	}

	public void deleteAll()
	{
		String s = null;
		try
		{
			s = this.document.getText(0, this.document.getLength());
			this.document.remove(0, this.document.getLength());

			this.lineLength = 0;
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}

		if (s != null)
		{
			this.listener.onWriterDelete(s);
		}
	}

	public double getTypeDelay()
	{
		return this.delayFactor;
	}

	public int getMaxLineLength()
	{
		return this.maxLength;
	}

	public int getCurrentLineLength()
	{
		return this.lineLength;
	}

	public StyledDocument getDocument()
	{
		return this.document;
	}

	private int getDelayByCharacter(char c)
	{
		return c == '.' || c == '?' || c == '!' ? 200 : c == ',' ? 100 : 30;
	}
}
