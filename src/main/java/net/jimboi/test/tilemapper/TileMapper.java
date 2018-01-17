package net.jimboi.test.tilemapper;

import org.bstone.application.FrameCounter;
import org.bstone.input.InputEngine;
import org.bstone.render.RenderEngine;
import org.bstone.render.RenderFramework;
import org.bstone.tick.TickEngine;
import org.bstone.tick.Tickable;
import org.bstone.window.Window;

/**
 * Created by Andy on 1/15/18.
 */
public class TileMapper implements Tickable, RenderFramework
{
	@Override
	public void start()
	{

	}

	@Override
	public void stop()
	{

	}

	@Override
	public void tick()
	{
		FPS.tick();
	}

	@Override
	public void load()
	{

	}

	@Override
	public void unload()
	{

	}

	@Override
	public void render()
	{
		FPS.frame();
	}

	public void onClick(double x, double y, int button)
	{

	}

	public void onKey(int keycode)
	{

	}

	private static final FrameCounter FPS = new FrameCounter();

	public static void main(String[] args)
	{
		Window.initializeGLFW();

		Window window = new Window().setSize(640, 480).setTitle("TileMapper");
		window.show();

		TileMapper mapper = new TileMapper();
		TickEngine tickEngine = new TickEngine(60, mapper);
		RenderEngine renderEngine = new RenderEngine(window, mapper);
		InputEngine inputEngine = new InputEngine(window);

		while(true)
		{
			tickEngine.update();
			renderEngine.update();

			FPS.poll();

			if (window.shouldWindowClose())
			{
				break;
			}
		}

		Window.terminateGLFW();
	}
}
