package net.jimboi.test.chubbycat.message;

import org.bstone.util.pair.Pair;

import java.util.StringTokenizer;

/**
 * Created by Andy on 11/29/17.
 */
public class Message
{
	public static boolean isCommand(Message msg)
	{
		String data = msg.getData().trim();
		return data.length() > 1 && data.charAt(0) == '!';
	}

	public static Pair<String, String[]> parseCommand(String data)
	{
		String command;
		String args;

		data = data.trim();
		if (data.charAt(0) != '!') throw new IllegalArgumentException("not a valid command");
		data = data.substring(1).trim();
		int i = data.indexOf(' ');
		if (i == -1) return new Pair<>(data, new String[0]);

		command = data.substring(0, i);
		args = data.substring(i + 1).trim();

		StringTokenizer tokenizer = new StringTokenizer(args, " ");
		String[] arr = new String[tokenizer.countTokens()];
		for(i = 0; i < arr.length; ++i)
		{
			arr[i] = tokenizer.nextToken();
		}

		return new Pair<>(command, arr);
	}

	private final String data;
	private String owner;

	public Message(String owner, String data)
	{
		this.owner = owner;
		this.data = data;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getOwner()
	{
		return this.owner;
	}

	public String getData()
	{
		return this.data;
	}

	public String toString()
	{
		return "[" + this.owner + "] " + this.data;
	}
}
