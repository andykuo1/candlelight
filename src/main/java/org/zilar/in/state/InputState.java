package org.zilar.in.state;

import org.zilar.in.input.AxisInput;
import org.zilar.in.input.ButtonInput;
import org.zilar.in.provider.InputProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 1/22/18.
 */
public class InputState
{
	private final Map<InputProvider, RawInputState> providers = new HashMap<>();

	public InputState addProvider(InputProvider provider)
	{
		this.providers.put(provider, provider.getProviderState());
		return this;
	}

	public InputState removeProvider(InputProvider provider)
	{
		this.providers.remove(provider);
		return this;
	}

	public Float get(AxisInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			return state.getAxis(input.getID());
		}
		return null;
	}

	public Float getMotion(AxisInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			return state.getAxisMotion(input.getID());
		}
		return null;
	}

	public Integer get(ButtonInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			return state.getButton(input.getID());
		}
		return null;
	}

	public Integer getMotion(ButtonInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			return state.getButtonMotion(input.getID());
		}
		return null;
	}

	public boolean consume(AxisInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			state.consumeAxis(input.getID());
			return true;
		}
		return false;
	}

	public boolean consumeMotion(AxisInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			state.consumeAxisChange(input.getID());
			return true;
		}
		return false;
	}

	public boolean consume(ButtonInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			state.consumeButton(input.getID());
			return true;
		}
		return false;
	}

	public boolean consumeMotion(ButtonInput input)
	{
		RawInputState state = this.providers.get(input.getProvider());
		if (state != null)
		{
			state.consumeButtonChange(input.getID());
			return true;
		}
		return false;
	}

	public boolean contains(AxisInput input)
	{
		return this.providers.get(input.getProvider()) != null;
	}

	public boolean contains(ButtonInput input)
	{
		return this.providers.get(input.getProvider()) != null;
	}

	public Iterable<InputProvider> getProviders()
	{
		return this.providers.keySet();
	}
}
