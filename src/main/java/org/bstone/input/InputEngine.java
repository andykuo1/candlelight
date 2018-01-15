package org.bstone.input;

import org.bstone.application.kernel.Engine;
import org.bstone.util.pair.Pair;
import org.bstone.window.Window;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Andy on 10/12/17.
 */
public class InputEngine implements Engine
{
	private final Window window;

	private Queue<Pair<Integer, InputContext>> contexts = new PriorityQueue<>(11,
			Comparator.comparingInt(Pair::getFirst));
	private InputContext defaultContext;

	private KeyboardHandler keyboard;
	private MouseHandler mouse;

	private TextHandler text;

	public InputEngine(Window window)
	{
		this.window = window;
	}

	@Override
	public boolean initialize()
	{
		this.defaultContext = new InputContext(this);

		this.keyboard = new KeyboardHandler(this.window);
		this.mouse = new MouseHandler(this.window);

		this.text = new TextHandler(this.window);
		return true;
	}

	@Override
	public void update()
	{
		//Usually called at the end of everything... but this is the same effect
		this.keyboard.poll();
		this.mouse.poll();

		//Actually update the system to new input (in other words: the start)
		this.window.poll();

		for(Pair<Integer, InputContext> ctx : this.contexts)
		{
			ctx.getSecond().poll();
		}

		this.defaultContext.poll();
	}

	@Override
	public void terminate()
	{
		this.contexts.clear();

		this.keyboard.clear();
		this.mouse.clear();

		this.text.clear();
	}

	public KeyboardHandler getKeyboard()
	{
		return this.keyboard;
	}

	public MouseHandler getMouse()
	{
		return this.mouse;
	}

	public TextHandler getText()
	{
		return this.text;
	}

	public InputContext createContext(int id)
	{
		InputContext ctx = new InputContext(this);
		this.contexts.add(new Pair<>(id, ctx));
		return ctx;
	}

	public void destroyContext(InputContext ctx)
	{
		this.contexts.removeIf(integerContextPair -> integerContextPair.getSecond().equals(ctx));
	}

	public InputContext getDefaultContext()
	{
		return this.defaultContext;
	}
}
