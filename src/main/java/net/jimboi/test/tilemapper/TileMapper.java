package net.jimboi.test.tilemapper;

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
		window.show();

		while(true)
		{
			window.clearScreenBuffer();
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

		Window.terminateGLFW();
	}
}
