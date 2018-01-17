package org.bstone.application.game;

import org.bstone.input.InputContext;
import org.bstone.input.InputListener;
import org.bstone.render.RenderEngine;
import org.bstone.render.RenderService;
import org.bstone.tick.TickEngine;
import org.bstone.tick.TickService;
import org.qsilver.poma.Poma;

/**
 * Created by Andy on 10/17/17.
 */
public interface Game extends InputListener
{
	default void onFirstUpdate(TickEngine tickEngine) {}
	default void onLastUpdate(TickEngine tickEngine) {}
	default void onEarlyUpdate() {}
	default void onFixedUpdate() {}
	default void onLateUpdate() {}

	default void onRenderLoad(RenderEngine renderEngine) {}
	default void onRenderUnload(RenderEngine renderEngine) {}
	default void onRenderUpdate(RenderEngine renderEngine, double delta) {}

	@Override
	default void onInputUpdate(InputContext context) {}

	class Tick extends TickService
	{
		private final Game game;

		public Tick(Game game)
		{
			this.game = game;
		}

		@Override
		protected void onFirstUpdate(TickEngine handler)
		{
			Poma.info("Starting Game...");
			this.game.onFirstUpdate(handler);
		}

		@Override
		protected void onLastUpdate(TickEngine handler)
		{
			Poma.info("Stopping Game...");
			this.game.onLastUpdate(handler);
		}

		@Override
		protected void onFixedUpdate()
		{
			this.game.onFixedUpdate();
		}

		public final Game getGame()
		{
			return this.game;
		}
	}


	class Render extends RenderService
	{
		private final Game game;

		public Render(Game game)
		{
			this.game = game;
		}

		@Override
		protected void onRenderLoad(RenderEngine renderEngine)
		{
			Poma.info("Loading Game...");
			this.game.onRenderLoad(renderEngine);
		}

		@Override
		protected void onRenderUnload(RenderEngine renderEngine)
		{
			Poma.info("Unloading Game...");
			this.game.onRenderUnload(renderEngine);
		}

		@Override
		protected void onRenderUpdate(RenderEngine renderEngine, double delta)
		{
			this.game.onRenderUpdate(renderEngine, delta);
		}

		public final Game getGame()
		{
			return this.game;
		}
	}
}
