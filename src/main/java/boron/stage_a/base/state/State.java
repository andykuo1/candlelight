package boron.stage_a.base.state;

/**
 * Created by Andy on 8/31/17.
 */
public interface State
{
	void onStateStart(StateManager stateManager);
	void onStateUpdate(StateManager stateManager);
	void onStateStop(StateManager stateManager);
}
