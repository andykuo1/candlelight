package apricot.bstone.util.parser.config;

import apricot.bstone.util.parser.DataFormatParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 10/26/17.
 */
public class ConfigFormatParser extends DataFormatParser<Map<String, String>>
{
	private final Map<String, String> config = new HashMap<>();

	public ConfigFormatParser(int bufferSize)
	{
		super(bufferSize);
	}

	@Override
	protected Map<String, String> readData(Cursor cursor) throws IOException
	{
		this.consumeWhiteSpaces(cursor);
		while(!isEndOfFile(cursor.getChar()))
		{
			this.readLine(cursor);
			this.consumeWhiteSpaces(cursor);
		}
		return this.config;
	}

	protected void readLine(Cursor cursor) throws IOException
	{
		char c = cursor.getChar();
		if (c == '#')
		{
			this.consumeComment(cursor);
		}
		else
		{
			String key, value;
			this.startBufferCapture(cursor);
			{
				this.consumeKeyValue(cursor);
			}
			key = this.stopBufferCapture(cursor);

			this.consumeWhiteSpaces(cursor);
			this.requireReadChar(cursor, '=');
			this.consumeWhiteSpaces(cursor);

			this.startBufferCapture(cursor);
			{
				this.consumeKeyValue(cursor);
			}
			value = this.stopBufferCapture(cursor);

			this.config.put(key, value);
		}
	}

	protected void consumeComment(Cursor cursor) throws IOException
	{
		this.readChar(cursor, '#');
		this.consumeLine(cursor);
	}

	protected void consumeKeyValue(Cursor cursor) throws IOException
	{
		char c = cursor.getChar();
		while(isKeyValue(c))
		{
			this.readChar(cursor, cursor.getChar());
			c = cursor.getChar();
		}
	}

	protected static boolean isKeyValue(char c)
	{
		return !isWhiteSpace(c) && c != '=';
	}
}
