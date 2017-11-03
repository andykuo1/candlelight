package org.bstone.util.parser;

import org.bstone.util.function.IOBooleanSupplier;
import org.bstone.util.function.IOPredicate;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Predicate;

/**
 * Created by Andy on 6/11/17.
 */
public abstract class DataFormatParser<T>
{
	protected DataBuffer buffer;
	protected DataCapture capture;
	protected Reader reader;

	public DataFormatParser(int bufferSize)
	{
		this.buffer = new DataBuffer(bufferSize);
	}

	public final T parse(Reader reader) throws IOException
	{
		this.reader = reader;
		this.buffer.clear();
		this.capture = null;

		final Cursor cursor = new Cursor(this.buffer);

		cursor.read(this.reader, null);

		this.consumeWhiteSpaces(cursor);

		T data = this.readData(cursor);

		this.consumeWhiteSpaces(cursor);
		requireAs(cursor, cursor.getChar(), DataFormatParser::isEndOfFile, "end of file");

		return data;
	}

	protected abstract T readData(Cursor cursor) throws IOException;

	protected boolean readChar(Cursor cursor, char c) throws IOException
	{
		if (cursor.getChar() != c) return false;
		cursor.read(this.reader, this.capture);
		return true;
	}

	protected boolean readDigit(Cursor cursor) throws IOException
	{
		if (!isDigit(cursor.getChar())) return false;
		cursor.read(this.reader, this.capture);
		return true;
	}

	protected boolean readLetter(Cursor cursor) throws IOException
	{
		if (!isLetter(cursor.getChar())) return false;
		cursor.read(this.reader, this.capture);
		return true;
	}

	protected boolean readWhiteSpace(Cursor cursor) throws IOException
	{
		if (!isWhiteSpace(cursor.getChar())) return false;
		cursor.read(this.reader, this.capture);
		return true;
	}

	protected String readUnsignedInteger(Cursor cursor) throws IOException
	{
		this.startBufferCapture(cursor);
		{
			requireAs(cursor, this::readDigit, "digit");

			this.consumeDigits(cursor);
		}
		return this.stopBufferCapture(cursor);
	}

	protected String readInteger(Cursor cursor) throws IOException
	{
		this.startBufferCapture(cursor);
		{
			this.readChar(cursor, '-');

			requireAs(cursor, this::readDigit, "digit");

			this.consumeDigits(cursor);
		}
		return this.stopBufferCapture(cursor);
	}


	protected String readNumber(Cursor cursor) throws IOException
	{
		this.startBufferCapture(cursor);
		{
			this.readChar(cursor, '-');

			char firstDigit = cursor.getChar();

			requireAs(cursor, this::readDigit, "digit");

			if (firstDigit != '0')
			{
				this.consumeDigits(cursor);
			}

			this.readDecimal(cursor);
			this.readExponent(cursor);
		}
		return this.stopBufferCapture(cursor);
	}

	private boolean readDecimal(Cursor cursor) throws IOException
	{
		if (!this.readChar(cursor, '.')) return false;

		requireAs(cursor, this::readDigit, "digit");

		this.consumeDigits(cursor);
		return true;
	}

	private boolean readExponent(Cursor cursor) throws IOException
	{
		if (!this.readChar(cursor, 'e') && !this.readChar(cursor, 'E')) return false;
		if (!this.readChar(cursor, '+')) this.readChar(cursor, '-');

		requireAs(cursor, this::readDigit, "digit");

		this.consumeDigits(cursor);
		return true;
	}

	protected void requireReadChar(Cursor cursor, char required) throws IOException
	{
		requireAs(cursor, () -> this.readChar(cursor, required), "" + required);
	}

	protected void requireReadCharSequence(Cursor cursor, char... chars) throws IOException
	{
		for (char c : chars)
		{
			this.requireReadChar(cursor, c);
		}
	}

	protected void consume(Cursor cursor, IOPredicate<Cursor> testFunction) throws IOException
	{
		while (testFunction.test(cursor))
		{
		}
	}

	protected void consumeChar(Cursor cursor, char c) throws IOException
	{
		while (cursor.getChar() == c)
		{
			cursor.read(this.reader, this.capture);
		}
	}

	protected void consumeLetters(Cursor cursor) throws IOException
	{
		while (this.readLetter(cursor))
		{
		}
	}

	protected void consumeDigits(Cursor cursor) throws IOException
	{
		while (this.readDigit(cursor))
		{
		}
	}

	protected void consumeWhiteSpaces(Cursor cursor) throws IOException
	{
		while (this.readWhiteSpace(cursor))
		{
		}
	}

	protected void consumeHorizontalWhiteSpaces(Cursor cursor) throws IOException
	{
		while (isHorizontalWhiteSpace(cursor.getChar()))
		{
			cursor.read(this.reader, this.capture);
		}
	}

	protected void consumeLine(Cursor cursor) throws IOException
	{
		char c;
		while (!isVerticalWhiteSpace(c = cursor.getChar()) && !isEndOfFile(c))
		{
			cursor.read(this.reader, this.capture);
		}

		if (!isEndOfFile(c))
		{
			cursor.read(this.reader, this.capture);
		}
	}

	protected void startBufferCapture(Cursor cursor)
	{
		if (this.capture == null)
		{
			this.capture = new DataCapture();
		}

		this.capture.activate(cursor.getIndex() - 1);
	}

	protected void pauseBufferCapture(Cursor cursor)
	{
		int end = isEndOfFile(cursor.getChar()) ? cursor.getIndex() : cursor.getIndex() - 1;
		this.capture.capture(this.buffer, end);
		this.capture.deactivate();
	}

	protected String stopBufferCapture(Cursor cursor)
	{
		int end = cursor.getIndex() - 1;
		this.capture.capture(this.buffer, end);
		String ret = this.capture.poll();
		this.capture.deactivate();
		return ret;
	}

	protected static boolean isWhiteSpace(char c)
	{
		return isHorizontalWhiteSpace(c) || isVerticalWhiteSpace(c);
	}

	protected static boolean isHorizontalWhiteSpace(char c)
	{
		return c == ' ' || c == '\t';
	}

	protected static boolean isVerticalWhiteSpace(char c)
	{
		return c == '\n' || c == '\r';
	}

	protected static boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}

	protected static boolean isLetter(char c)
	{
		return c >= 'A' && c <= 'z';
	}

	protected static boolean isHexDigit(char c)
	{
		return isDigit(c) || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
	}

	protected static boolean isEndOfFile(char c)
	{
		return c == (char) -1;
	}

	protected static boolean isPOSIXCompatible(char c)
	{
		return isLetter(c) || isDigit(c) || c == '_';
	}

	protected static void requireAs(Cursor cursor, IOBooleanSupplier testFunction, String requiredMsg) throws IOException
	{
		char c = cursor.getChar();
		if (!testFunction.getAsBoolean())
		{
			throw formatError(cursor, "Expected '" + requiredMsg + "', but found '" + c + "'!");
		}
	}

	protected static void requireAs(Cursor cursor, IOPredicate<Cursor> testFunction, String requiredMsg) throws IOException
	{
		char c = cursor.getChar();
		if (!testFunction.test(cursor))
		{
			throw formatError(cursor, "Expected '" + requiredMsg + "', but found '" + c + "'!");
		}
	}

	protected static void requireAs(Cursor cursor, char c, Predicate<Character> testFunction, String requiredMsg) throws IOException
	{
		if (!testFunction.test(c))
		{
			throw formatError(cursor, "Expected '" + requiredMsg + "', but found '" + c + "'!");
		}
	}

	protected static DataFormatException formatError(Cursor cursor, String msg)
	{
		return new DataFormatException(msg + " - " + cursor.getLine() + ":" + cursor.getColumn());
	}

	protected static final class DataBuffer
	{
		private final char[] data;
		private final int size;

		private int offset;
		private int index;

		DataBuffer(int size)
		{
			this.data = new char[this.size = size];

			this.offset = 0;
			this.index = 0;
		}

		public boolean poll(Reader reader) throws IOException
		{
			this.offset = this.index;
			this.index = reader.read(this.data, 0, this.size);
			return this.index != -1;
		}

		public void clear()
		{
			for (int i = 0; i < this.data.length; ++i)
			{
				this.data[i] = 0;
			}

			this.offset = 0;
			this.index = 0;
		}

		public char charAt(int index)
		{
			return this.data[index];
		}

		public char[] getData()
		{
			return this.data;
		}

		public int getIndex()
		{
			return this.index;
		}

		public int getOffset()
		{
			return this.offset;
		}
	}

	protected static final class DataCapture
	{
		private final StringBuilder captureBuffer = new StringBuilder();
		private int startIndex;
		private boolean active;

		DataCapture()
		{
			this.startIndex = -1;
			this.active = false;
		}

		public void activate(int index)
		{
			if (this.active) throw new IllegalStateException("Capture has already been activated!");
			this.active = true;
			this.startIndex = index;
		}

		public void deactivate()
		{
			if (!this.active)
				throw new IllegalStateException("Capture has already been deactivated!");
			this.active = false;
		}

		public void capture(DataBuffer buffer, int index)
		{
			if (!this.active) return;

			this.captureBuffer.append(buffer.getData(), this.startIndex, index - this.startIndex);
			this.startIndex = 0;
		}

		public void add(char c)
		{
			this.captureBuffer.append(c);
		}

		public void add(String s)
		{
			this.captureBuffer.append(s);
		}

		public String poll()
		{
			String string = this.captureBuffer.toString();
			this.captureBuffer.setLength(0);
			this.startIndex = 0;
			return string;
		}
	}

	protected static final class Cursor
	{
		private final DataBuffer buffer;
		private int index;
		private int line;
		private int lineOffset;
		private char current;

		Cursor(DataBuffer buffer)
		{
			this.buffer = buffer;
			this.index = 0;
			this.line = 1;
			this.lineOffset = 0;
			this.current = (char) -1;
		}

		public void read(Reader reader, DataCapture capture) throws IOException
		{
			if (this.index == this.buffer.getIndex())
			{
				if (capture != null)
				{
					capture.capture(this.buffer, this.buffer.getIndex());
				}

				this.index = 0;
				if (!this.buffer.poll(reader))
				{
					this.current = (char) -1;
					this.index++;
					return;
				}
			}

			if (this.current == '\n')
			{
				++this.line;
				this.lineOffset = this.buffer.getOffset() + this.index;
			}

			this.current = this.buffer.charAt(this.index++);
		}

		public int getIndex()
		{
			return this.index;
		}

		public char getChar()
		{
			return this.current;
		}

		public int getLine()
		{
			return this.line;
		}

		public int getColumn()
		{
			return (this.buffer.getOffset() + this.index - 1) - this.lineOffset + 1;
		}
	}
}
