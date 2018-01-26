package org.bstone.application.game;

import org.bstone.application.service.RenderService;
import org.bstone.application.service.TickService;
import org.bstone.render.RenderEngine;
import org.bstone.tick.TickEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andy on 10/17/17.
 */
public interface Game
{
	Logger LOG = LoggerFactory.getLogger(Game.class);

	default void onFirstUpdate(TickEngine tickEngine) {}
	default void onLastUpdate(TickEngine tickEngine) {}
	default void onFixedUpdate() {}

	default void onRenderLoad(RenderEngine renderEngine) {}
	default void onRenderUnload(RenderEngine renderEngine) {}
	default void onRenderUpdate(RenderEngine renderEngine) {}

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
			LOG.info("Starting Game...");
			this.game.onFirstUpdate(handler);
		}

		@Override
		protected void onLastUpdate(TickEngine handler)
		{
			LOG.info("Stopping Game...");
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
			LOG.info("Loading Game...");
			this.game.onRenderLoad(renderEngine);
		}

		@Override
		protected void onRenderUnload(RenderEngine renderEngine)
		{
			LOG.info("Unloading Game...");
			this.game.onRenderUnload(renderEngine);
		}

		@Override
		protected void onRenderUpdate(RenderEngine renderEngine)
		{
			this.game.onRenderUpdate(renderEngine);
		}

		public final Game getGame()
		{
			return this.game;
		}
	}
}
