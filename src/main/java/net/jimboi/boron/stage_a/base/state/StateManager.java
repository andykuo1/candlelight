package net.jimboi.boron.stage_a.base.state;

import org.bstone.util.listener.Listenable;

/**
 * Created by Andy on 8/31/17.
 */
public class StateManager
{
	public interface OnStateUpdateListener
	{
		void onStateUpdate(StateManager stateManager, State currentState);
	}

	public final Listenable<OnStateUpdateListener> onStateUpdate = new Listenable<>((listener, objects) -> listener.onStateUpdate((StateManager) objects[0], (State) objects[1]));

	private State currentState;
	private State nextState;
	private boolean setup = false;

	public void update()
	{
		if (this.setup)
		{
			if (this.currentState != null)
			{
				this.currentState.onStateStop(this);
			}

			this.currentState = this.nextState;
			this.nextState = null;

			this.currentState.onStateStart(this);

			this.setup = false;
		}
		else
		{
			if (this.currentState != null)
			{
				this.currentState.onStateUpdate(this);
			}

			this.onStateUpdate.notifyListeners(this, this.currentState);
		}
	}

	public void setNextState(State state)
	{
		this.nextState = state;
		this.setup = true;
	}

	public State getCurrentState()
	{
		return this.currentState;
	}

	public boolean isActive()
	{
		return !this.setup && this.currentState != null;
	}
}
