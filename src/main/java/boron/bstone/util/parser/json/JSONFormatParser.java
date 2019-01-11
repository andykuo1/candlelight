package boron.bstone.util.parser.json;

import boron.bstone.json.JSON;
import boron.bstone.json.JSONArray;
import boron.bstone.json.JSONBoolean;
import boron.bstone.json.JSONLiteral;
import boron.bstone.json.JSONObject;
import boron.bstone.json.JSONString;
import boron.bstone.json.JSONValue;
import boron.bstone.util.parser.DataFormatParser;

import java.io.IOException;

/**
 * Created by Andy on 6/11/17.
 */
public class JSONFormatParser extends DataFormatParser<JSONValue>
{
	private static final char CONTROL_CHAR = 0x20;
	private static final int MAX_LEVEL = 1000;

	private final boolean strict;

	private int level;

	public JSONFormatParser(int bufferSize)
	{
		this(bufferSize, true);
	}

	public JSONFormatParser(int bufferSize, boolean strict)
	{
		super(bufferSize);
		this.strict = strict;
	}

	@Override
	protected JSONValue readData(Cursor cursor) throws IOException
	{
		return this.readValue(cursor);
	}

	protected JSONValue readValue(Cursor cursor) throws IOException
	{
		this.consumeWhiteSpaces(cursor);

		switch (cursor.getChar())
		{
			case '"':
				return this.readString(cursor);
			case '[':
				return this.readArray(cursor);
			case '{':
				return this.readObject(cursor);
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
				return JSON.number(this.readNumber(cursor));
			default:
				if (this.strict)
				{
					char c = cursor.getChar();
					if (c == 'n') return this.readNull(cursor);
					if (c == 't') return this.readTrue(cursor);
					if (c == 'f') return this.readFalse(cursor);
				}
				else if (isLetter(cursor.getChar()))
				{
					return this.readLiteral(cursor);
				}

				throw formatError(cursor, "Found invalid format");
		}
	}

	protected JSONArray readArray(Cursor cursor) throws IOException
	{
		JSONArray arr = JSON.array();
		cursor.read(this.reader, this.capture);//[

		if (++this.level > MAX_LEVEL) throw formatError(cursor, "Too many nested objects!");

		this.consumeWhiteSpaces(cursor);
		if (this.readChar(cursor, ']'))
		{
			--this.level;
			return arr;
		}

		do
		{
			this.consumeWhiteSpaces(cursor);
			JSONValue value = this.readValue(cursor);
			this.consumeWhiteSpaces(cursor);

			arr.add(value);
		}
		while (this.readChar(cursor, ','));

		requireAs(cursor, () -> this.readChar(cursor, ']'), ",' or ']");

		--this.level;
		return arr;
	}

	protected JSONObject readObject(Cursor cursor) throws IOException
	{
		JSONObject obj = JSON.object();
		cursor.read(this.reader, this.capture);//{

		if (++this.level > MAX_LEVEL) throw formatError(cursor, "Too many nested objects!");

		this.consumeWhiteSpaces(cursor);
		if (this.readChar(cursor, '}'))
		{
			--this.level;
			return obj;
		}

		do
		{
			this.consumeWhiteSpaces(cursor);
			String name = this.readEntryName(cursor);
			this.consumeWhiteSpaces(cursor);
			this.requireReadChar(cursor, ':');
			this.consumeWhiteSpaces(cursor);
			JSONValue value = this.readValue(cursor);
			this.consumeWhiteSpaces(cursor);

			obj.put(name, value);
		}
		while (this.readChar(cursor, ','));

		this.requireReadChar(cursor, '}');

		--this.level;
		return obj;
	}

	protected String readEntryName(Cursor cursor) throws IOException
	{
		requireAs(cursor, cursor.getChar(), (c) -> (c == '"'), "name");

		return this.readString(cursor).get();
	}

	protected JSONLiteral readLiteral(Cursor cursor) throws IOException
	{
		this.startBufferCapture(cursor);
		{
			char c = cursor.getChar();
			while(!isVerticalWhiteSpace(c) && c != ',')
			{
				cursor.read(this.reader, this.capture);
				c = cursor.getChar();
			}
		}
		String buffer = this.stopBufferCapture(cursor);
		if ("null".equals(buffer)) return JSON.NULL;
		if ("true".equals(buffer)) return JSON.TRUE;
		if ("false".equals(buffer)) return JSON.FALSE;
		return JSON.literal(buffer);
	}

	protected JSONLiteral readNull(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//n
		this.requireReadCharSequence(cursor, 'u', 'l', 'l');
		return JSON.NULL;
	}

	protected JSONBoolean readTrue(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//t
		this.requireReadCharSequence(cursor, 'r', 'u', 'e');
		return JSON.TRUE;
	}

	protected JSONBoolean readFalse(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//f
		this.requireReadCharSequence(cursor, 'a', 'l', 's', 'e');
		return JSON.FALSE;
	}

	protected JSONString readString(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//"
		this.startBufferCapture(cursor);
		{
			while (cursor.getChar() != '"')
			{
				if (cursor.getChar() == '\\')
				{
					this.pauseBufferCapture(cursor);
					this.readEscape(cursor);
					this.startBufferCapture(cursor);
				}
				else
				{
					requireAs(cursor, cursor.getChar(), (c) -> (c >= CONTROL_CHAR), "valid string char");

					cursor.read(this.reader, this.capture);
				}
			}
		}
		String string = this.stopBufferCapture(cursor);
		cursor.read(this.reader, this.capture);//"
		return JSON.value(string);
	}

	protected void readEscape(Cursor cursor) throws IOException
	{
		cursor.read(this.reader, this.capture);//\
		switch (cursor.getChar())
		{
			case '"':
			case '/':
			case '\\':
				this.capture.add(cursor.getChar());
				break;
			case 'b':
				this.capture.add('\b');
				break;
			case 'f':
				this.capture.add('\f');
				break;
			case 'n':
				this.capture.add('\n');
				break;
			case 'r':
				this.capture.add('\r');
				break;
			case 't':
				this.capture.add('\t');
				break;
			case 'u':
				char[] hexchars = new char[4];
				for (int i = 0; i < hexchars.length; i++)
				{
					cursor.read(this.reader, this.capture);

					requireAs(cursor, cursor.getChar(), DataFormatParser::isHexDigit, "hexadecimal digit");
					hexchars[i] = cursor.getChar();
				}
				this.capture.add((char) Integer.parseInt(new String(hexchars), 16));
				break;
			default:
				throw formatError(cursor, "Found invalid escape sequence");
		}
		cursor.read(this.reader, this.capture);
	}
}
