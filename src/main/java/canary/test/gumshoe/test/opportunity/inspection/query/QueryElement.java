package canary.test.gumshoe.test.opportunity.inspection.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andy on 12/8/17.
 */
public class QueryElement implements Comparable<QueryElement>
{
	private final int timeStamp;
	private final String[] sequence;

	public QueryElement(int timeStamp, String sequence)
	{
		this.timeStamp = timeStamp;

		List<String> sequences = new ArrayList<>();
		parse(sequence.toLowerCase(), sequences);

		this.sequence = sequences.toArray(new String[sequences.size()]);
	}

	public final String[] getSequence()
	{
		return this.sequence;
	}

	public final int getTimeStamp()
	{
		return this.timeStamp;
	}

	@Override
	public final int compareTo(QueryElement o)
	{
		int i = this.timeStamp - o.timeStamp;
		if (i == 0 && !this.equals(o)) return 1;
		else return i;
	}

	private static void parse(String sequence, Collection<String> sequences)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sequence.length(); ++i)
		{
			char c = sequence.charAt(i);

			if (Character.isLetterOrDigit(c))
			{
				sb.append(c);
			}
			else if (Character.isWhitespace(c))
			{
				tryBuildWord(sb, sequences);
			}
		}
		tryBuildWord(sb, sequences);
	}

	private static void tryBuildWord(StringBuilder sb, Collection<String> dst)
	{
		if (sb.length() > 0)
		{
			dst.add(sb.toString());
			sb.setLength(0);
		}
	}
}
