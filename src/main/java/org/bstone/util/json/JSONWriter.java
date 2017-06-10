package org.bstone.util.json;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Andy on 6/9/17.
 */
public class JSONWriter
{
	private static final int UNIT_SEPARATOR = 0x001f;
	private static final char[] QUOTATION = {'\\', '"'};
	private static final char[] BACKSLASH = {'\\', '\\'};
	private static final char[] TAB = {'\\', 't'};
	private static final char[] LF = {'\\', 'n'};
	private static final char[] CR = {'\\', 'r'};
	private static final char[] LINE_SEPARATOR = {'\\', 'u', '2', '0', '2', '8'};
	private static final char[] PARAGRAPH_SEPARATOR = {'\\', 'u', '2', '0', '2', '9'};
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f'};

	private Writer writer;
	private boolean compact;
	private int level;

	JSONWriter()
	{
	}

	public void write(Writer writer, JSONValue value, boolean compact) throws IOException
	{
		this.writer = writer;

		this.compact = compact;
		this.level = 0;

		this.write(value);
	}

	private void write(JSONValue json) throws IOException
	{
		if (json instanceof JSONNumber)
		{
			this.write((JSONNumber) json);
		}
		else if (json instanceof JSONString)
		{
			this.write((JSONString) json);
		}
		else if (json instanceof JSONLiteral)
		{
			this.write((JSONLiteral) json);
		}
		else if (json instanceof JSONArray)
		{
			this.write((JSONArray) json);
		}
		else if (json instanceof JSONObject)
		{
			this.write((JSONObject) json);
		}
		else
		{
			throw new UnsupportedOperationException("JSON type not supported - " + json);
		}
	}

	private void write(JSONObject json) throws IOException
	{
		this.writer.write('{');
		if (!this.compact)
		{
			this.writer.write('\n');
			++this.level;
		}
		boolean first = true;

		for (String name : json.names())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				this.writer.write(',');
				if (!this.compact)
				{
					this.writer.write('\n');
				}
			}

			if (!this.compact)
			{
				for (int i = 0; i < this.level; ++i)
				{
					this.writer.write("    ");
				}
			}

			this.writer.write('"');
			this.writeWithFormat(name);
			this.writer.write('"');

			this.writer.write(':');

			if (!this.compact)
			{
				this.writer.write(" ");
			}

			this.write(json.get(name));
		}

		if (!this.compact)
		{
			--this.level;
			this.writer.write('\n');
			for (int i = 0; i < this.level; ++i)
			{
				this.writer.write("    ");
			}
		}

		this.writer.write('}');
	}

	private void write(JSONArray json) throws IOException
	{
		this.writer.write('[');
		if (!this.compact)
		{
			this.writer.write('\n');
			++this.level;
		}
		boolean first = true;

		for (JSONValue value : json.values)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				this.writer.write(',');
				if (!this.compact)
				{
					this.writer.write('\n');
				}
			}

			if (!this.compact)
			{
				for (int i = 0; i < this.level; ++i)
				{
					this.writer.write("    ");
				}
			}

			this.write(value);
		}

		if (!this.compact)
		{
			--this.level;
			this.writer.write('\n');
			for (int i = 0; i < this.level; ++i)
			{
				this.writer.write("    ");
			}
		}

		this.writer.write(']');
	}

	private void write(JSONNumber json) throws IOException
	{
		this.writer.write(json.string);
	}

	private void write(JSONString json) throws IOException
	{
		this.writer.write('"');
		this.writeWithFormat(json.value);
		this.writer.write('"');
	}

	private void write(JSONLiteral json) throws IOException
	{
		this.writer.write(json.value);
	}

	private void writeWithFormat(String string) throws IOException
	{
		int len = string.length();
		int start = 0;
		for (int i = 0; i < len; ++i)
		{
			char[] formatted = getFormattedCharacter(string.charAt(i));
			if (formatted != null)
			{
				this.writer.write(string, start, i - start);
				this.writer.write(formatted);
				start = i + 1;
			}
		}
		this.writer.write(string, start, len - start);
	}

	private char[] getFormattedCharacter(char c)
	{
		if (c > '\\')
		{
			if (c < '\u2028' || c > '\u2029') return null;
			return c == '\u2028' ? LINE_SEPARATOR : PARAGRAPH_SEPARATOR;
		}

		if (c == '\\') return BACKSLASH;
		if (c > '"') return null;
		if (c == '"') return QUOTATION;
		if (c > UNIT_SEPARATOR) return null;
		if (c == '\n') return LF;
		if (c == '\r') return CR;
		if (c == '\t') return TAB;

		return new char[]{'\\', 'u', '0', '0', HEX_DIGITS[c >> 4 & 0x000f], HEX_DIGITS[c & 0x000f]};
	}
}
