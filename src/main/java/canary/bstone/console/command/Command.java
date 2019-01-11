package canary.bstone.console.command;

/**
 * Created by Andy on 12/6/17.
 */
public interface Command
{
	void process(String[] args);

	default String getUsage()
	{
		return "[...]";
	}
}
