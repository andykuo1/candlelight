package canary.test.gumshoe.test.opportunity.inspection.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andy on 12/8/17.
 */
public class QueryString
{
	private final String[] include;
	private final String[] exclude;

	public QueryString(String query)
	{
		List<String> includes = new ArrayList<>();
		List<String> excludes = new ArrayList<>();
		parse(query.toLowerCase(), includes, excludes);

		this.include = includes.toArray(new String[includes.size()]);
		this.exclude = excludes.toArray(new String[excludes.size()]);
	}

	public boolean evaluate(QueryElement element)
	{
		String[] seq = element.getSequence();

		for(String exclude : this.exclude)
		{
			if (contains(exclude, seq))
			{
				return false;
			}
		}

		for(String include : this.include)
		{
			if (!contains(include, seq))
			{
				return false;
			}
		}

		return true;
	}

	private static boolean contains(String s, String[] arr)
	{
		for(String a : arr)
		{
			if (s.compareToIgnoreCase(a) == 0)
			{
				return true;
			}
		}

		return false;
	}

	static void parse(String query, Collection<String> includes, Collection<String> excludes)
	{
		boolean exclude = false;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < query.length(); ++i)
		{
			char c = query.charAt(i);
			if (Character.isLetterOrDigit(c))
			{
				sb.append(c);
			}
			else if (Character.isWhitespace(c))
			{
				tryBuildWord(sb, exclude ? excludes : includes);
				exclude = false;
			}
			else if (c == '!')
			{
				if (sb.length() != 0) throw new IllegalArgumentException("invalid use of '!' - must be used before a word");

				exclude = true;
			}
		}
		tryBuildWord(sb, exclude ? excludes : includes);
	}

	static void tryBuildWord(StringBuilder sb, Collection<String> dst)
	{
		if (sb.length() > 0)
		{
			dst.add(sb.toString());
			sb.setLength(0);
		}
	}
}
