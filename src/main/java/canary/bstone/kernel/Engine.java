package canary.bstone.kernel;

/**
 * Created by Andy on 1/15/18.
 */
public interface Engine
{
	boolean initialize();
	void update();
	void terminate();
}
