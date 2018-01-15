package net.jimboi.test.tilemapper;

import org.bstone.input.InputEngine;
import org.bstone.tick.TickEngine;
import org.bstone.window.Window;

/**
 * Created by Andy on 1/15/18.
 */
public class TileMapper
{
	public void onStart()
	{
	}

	public void onRender()
	{

	}

	public void onStop()
	{

	}

	public void onClick(double x, double y, int button)
	{

	}

	public void onKey(int keycode)
	{

	}

	public static void main(String[] args)
	{
		Window window = new Window();
		TileMapper mapper = new TileMapper();

		InputEngine inputs = new InputEngine(window);
		TickEngine loop = new TickEngine(60, false);
	}
}
