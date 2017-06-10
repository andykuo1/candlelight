package org.bstone.util.json;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONReader
{
	private static final char CONTROL_CHAR = 0x20;
	private static final int MAX_LEVEL = 1000;

	protected Reader reader;

	private char[] buffer;
	private int bufferOffset;
	private int bufferLength;

	private int index;
	private int line;
	private int lineOffset;
	private char current;

	private int captureStartIndex;
	private StringBuilder captureBuffer;

	private int level;

	JSONReader()
	{
	}

	public JSONValue read(Reader reader, int bufferSize) throws IOException
	{
		this.reader = reader;

		this.buffer = new char[bufferSize];
		this.bufferOffset = 0;

		this.index = 0;
		this.bufferLength = 0;

		this.line = 1;
		this.lineOffset = 0;

		this.current = 0;

		this.captureBuffer = null;
		this.captureStartIndex = -1;

		this.read();

		this.consumeWhiteSpaces();
		JSONValue value = this.readValue();
		this.consumeWhiteSpaces();

		ASSERT_CURRENT_AS(this, this.isEndOfFile(this.current), "end of file");

		return value;
	}

	private int getCurrentLine()
	{
		return this.line;
	}

	private int getCurrentColumn()
	{
		return (this.bufferOffset + this.index - 1) - this.lineOffset + 1;
	}

	private void read() throws IOException
	{
		if (this.index == this.bufferLength)
		{
			if (this.captureStartIndex != -1)
			{
				this.captureBuffer.append(buffer, this.captureStartIndex, this.bufferLength - this.captureStartIndex);
				this.captureStartIndex = 0;
			}

			this.bufferOffset += this.bufferLength;
			this.bufferLength = this.reader.read(this.buffer, 0, this.buffer.length);
			this.index = 0;

			if (this.bufferLength == -1)
			{
				this.current = (char) -1;
				this.index++;
				return;
			}
		}

		if (this.current == '\n')
		{
			++this.line;
			this.lineOffset = this.bufferOffset + this.index;
		}

		this.current = this.buffer[this.index++];
	}

	private JSONValue readValue() throws IOException
	{
		switch (this.current)
		{
			case 'n':
				return this.readNull();
			case 't':
				return this.readTrue();
			case 'f':
				return this.readFalse();
			case '"':
				return this.readString();
			case '[':
				return this.readArray();
			case '{':
				return this.readObject();
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return this.readNumber();
			default:
				throw new JSONFormatException("Found invalid format - " + this.getCurrentLine() + ":" + this.getCurrentColumn());
		}
	}

	private JSONArray readArray() throws IOException
	{
		JSONArray arr = new JSONArray();
		this.read();//[

		if (++this.level > MAX_LEVEL) THROW(this, "Too many nested objects!");

		this.consumeWhiteSpaces();
		if (this.readChar(']'))
		{
			--this.level;
			return arr;
		}

		do
		{
			this.consumeWhiteSpaces();
			JSONValue value = this.readValue();
			this.consumeWhiteSpaces();

			arr.add(value);
		}
		while (this.readChar(','));

		ASSERT_CURRENT_AS(this, this.readChar(']'), ",' or ']");

		--this.level;
		return arr;
	}

	private JSONObject readObject() throws IOException
	{
		JSONObject obj = new JSONObject();
		this.read();//{

		if (++this.level > MAX_LEVEL) THROW(this, "Too many nested objects!");

		this.consumeWhiteSpaces();
		if (this.readChar('}'))
		{
			--this.level;
			return obj;
		}

		do
		{
			this.consumeWhiteSpaces();
			String name = this.readEntryName();
			this.consumeWhiteSpaces();
			ASSERT_CURRENT_AS(this, this.readChar(':'), ":");
			this.consumeWhiteSpaces();
			JSONValue value = this.readValue();
			this.consumeWhiteSpaces();

			obj.put(name, value);
		}
		while (this.readChar(','));

		ASSERT_CURRENT_AS(this, this.readChar('}'), ",' or '}");

		--this.level;
		return obj;
	}

	private String readEntryName() throws IOException
	{
		ASSERT_CURRENT_AS(this, this.current == '"', "name");

		return this.readString().value;
	}

	private JSONLiteral readNull() throws IOException
	{
		this.read();//n
		this.readCharWithAssert('u');
		this.readCharWithAssert('l');
		this.readCharWithAssert('l');
		return JSON.NULL;
	}

	private JSONBoolean readTrue() throws IOException
	{
		this.read();//t
		this.readCharWithAssert('r');
		this.readCharWithAssert('u');
		this.readCharWithAssert('e');
		return JSON.TRUE;
	}

	private JSONBoolean readFalse() throws IOException
	{
		this.read();//f
		this.readCharWithAssert('a');
		this.readCharWithAssert('l');
		this.readCharWithAssert('s');
		this.readCharWithAssert('e');
		return JSON.FALSE;
	}

	private JSONString readString() throws IOException
	{
		this.read();//"
		this.startCaptureToStringBuffer();
		{
			while (this.current != '"')
			{
				if (current == '\\')
				{
					this.waitCaptureToStringBuffer();
					this.readEscape();
					this.startCaptureToStringBuffer();
				}
				else
				{
					ASSERT_CURRENT_AS(this, this.current >= CONTROL_CHAR, "valid string char");

					this.read();
				}
			}
		}
		String string = this.stopCaptureToStringBuffer();
		this.read();//"
		return new JSONString(string);
	}

	private void readEscape() throws IOException
	{
		this.read();//\
		switch (this.current)
		{
			case '"':
			case '/':
			case '\\':
				this.captureBuffer.append(this.current);
				break;
			case 'b':
				this.captureBuffer.append('\b');
				break;
			case 'f':
				this.captureBuffer.append('\f');
				break;
			case 'n':
				this.captureBuffer.append('\n');
				break;
			case 'r':
				this.captureBuffer.append('\r');
				break;
			case 't':
				this.captureBuffer.append('\t');
				break;
			case 'u':
				char[] hexchars = new char[4];
				for (int i = 0; i < hexchars.length; i++)
				{
					this.read();
					ASSERT_CURRENT_AS(this, this.isHexDigit(this.current), "hexadecimal digit");
					hexchars[i] = this.current;
				}
				this.captureBuffer.append((char) Integer.parseInt(new String(hexchars), 16));
				break;
			default:
				ASSERT_CURRENT_AS(this, false, "valid escape sequence");
		}
		this.read();
	}

	private JSONNumber readNumber() throws IOException
	{
		this.startCaptureToStringBuffer();
		{
			this.readChar('-');

			int firstDigit = this.current;

			ASSERT_CURRENT_AS(this, this.readDigit(), "digit");

			if (firstDigit != '0')
			{
				this.consumeDigits();
			}

			this.readFraction();
			this.readExponent();
		}
		String string = this.stopCaptureToStringBuffer();
		return new JSONNumber(string);
	}

	private boolean readFraction() throws IOException
	{
		if (!this.readChar('.')) return false;

		ASSERT_CURRENT_AS(this, this.readDigit(), "digit");

		this.consumeDigits();
		return true;
	}

	private boolean readExponent() throws IOException
	{
		if (!this.readChar('e') && !this.readChar('E')) return false;
		if (!this.readChar('+')) this.readChar('-');

		ASSERT_CURRENT_AS(this, this.readDigit(), "digit");

		this.consumeDigits();
		return true;
	}

	private boolean readChar(char c) throws IOException
	{
		if (this.current != c) return false;
		read();
		return true;
	}

	private boolean readDigit() throws IOException
	{
		if (!this.isDigit(this.current)) return false;
		read();
		return true;
	}

	private boolean readWhiteSpace() throws IOException
	{
		if (!this.isWhiteSpace(this.current)) return false;
		read();
		return true;
	}

	private void readCharWithAssert(char valid) throws IOException
	{
		ASSERT_CURRENT_AS(this, this.readChar(valid), "" + valid);
	}

	private void consumeDigits() throws IOException
	{
		while (this.readDigit())
		{
		}
	}

	private void consumeWhiteSpaces() throws IOException
	{
		while (this.readWhiteSpace())
		{
		}
	}

	private void startCaptureToStringBuffer()
	{
		if (this.captureBuffer == null)
		{
			this.captureBuffer = new StringBuilder();
		}

		this.captureStartIndex = this.index - 1;
	}

	private void waitCaptureToStringBuffer()
	{
		int end = this.isEndOfFile(this.current) ? this.index : this.index - 1;
		this.captureBuffer.append(this.buffer, this.captureStartIndex, end);
		this.captureStartIndex = -1;
	}

	private String stopCaptureToStringBuffer()
	{
		int start = this.captureStartIndex;
		int end = this.index - 1;

		this.captureStartIndex = -1;
		if (this.captureBuffer.length() > 0)
		{
			this.captureBuffer.append(this.buffer, start, end - start);
			String captureString = this.captureBuffer.toString();
			this.captureBuffer.setLength(0);
			return captureString;
		}

		return new String(this.buffer, start, end - start);
	}

	private boolean isWhiteSpace(char c)
	{
		return c == ' ' || c == '\n' || c == '\r' || c == '\t';
	}

	private boolean isDigit(char c)
	{
		return c >= '0' && c <= '9';
	}

	private boolean isHexDigit(char c)
	{
		return isDigit(c) || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
	}

	private boolean isEndOfFile(char c)
	{
		return c == (char) -1;
	}

	private static final void ASSERT_CURRENT_AS(JSONReader reader, boolean condition, String valid)
	{
		if (!condition)
		{
			THROW(reader, "Expected '" + valid + "', but found '" + reader.current + "'!");
		}
	}

	private static final void THROW(JSONReader reader, String message) throws JSONFormatException
	{
		throw new JSONFormatException(message + " - " + reader.getCurrentLine() + ":" + reader.getCurrentColumn());
	}
}
