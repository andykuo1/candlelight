package org.bstone.input;

import org.bstone.application.Application;
import org.bstone.application.Engine;
import org.bstone.input.context.InputContext;
import org.bstone.input.context.InputMapping;
import org.bstone.util.Pair;
import org.bstone.window.Window;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Andy on 10/12/17.
 */
public class InputEngine extends Engine
{
	private final Window window;

	private Queue<Pair<Integer, InputContext>> contexts = new PriorityQueue<>(11,
			Comparator.comparingInt(Pair::getFirst));

	private InputMapping mapping;

	private KeyboardHandler keyboard;
	private MouseHandler mouse;
	private TextHandler text;

	public InputEngine(Window window)
	{
		this.window = window;
		this.mapping = new InputMapping(this);
	}

	@Override
	protected boolean onStart(Application app)
	{
		this.keyboard = new KeyboardHandler(this.window);
		this.mouse = new MouseHandler(this.window);
		this.text = new TextHandler(this.window);
		return true;
	}

	@Override
	protected void onUpdate(Application app)
	{
		this.window.poll();

		this.keyboard.update();
		this.mouse.update();

		//TODO: this does not work, cause it will only poll the first one...
		for(Pair<Integer, InputContext> ctx : this.contexts)
		{
			ctx.getSecond().poll();
		}
	}

	@Override
	protected void onStop(Application app)
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
		InputContext ctx = new InputContext(this.mapping);
		this.contexts.add(new Pair<>(id, ctx));
		return ctx;
	}

	public void destroyContext(InputContext ctx)
	{
		this.contexts.removeIf(integerContextPair -> integerContextPair.getSecond().equals(ctx));
	}

	public InputMapping getInput()
	{
		return this.mapping;
	}
}
