package net.jimboi.test.tilemapper;

import org.bstone.tick.TickHandler;
import org.bstone.tick.Tickable;
import org.bstone.window.Window;

/**
 * Created by Andy on 1/15/18.
 */
public class TileMapper
{
	public void onStart()
	{
	}

	public void onUpdate()
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

	private static boolean dirtyFrame = true;

	public static void main(String[] args)
	{
		Window.initializeGLFW();

		Window window = new Window().setSize(640, 480).setTitle("TileMapper");
		TileMapper mapper = new TileMapper();
		TickHandler tick = new TickHandler(60, new Tickable()
		{
			@Override
			public void onFixedUpdate()
			{
				mapper.onUpdate();
				dirtyFrame = true;
			}
		});
		window.show();

		tick.initialize();
		while(true)
		{
			window.clearScreenBuffer();
			tick.update();
			if (dirtyFrame)
			{
				mapper.onRender();
				dirtyFrame = false;
			}
			window.updateScreenBuffer();
			window.poll();

			if (window.shouldWindowClose())
			{
				break;
			}
		}
		tick.terminate();

		Window.terminateGLFW();
	}
}
