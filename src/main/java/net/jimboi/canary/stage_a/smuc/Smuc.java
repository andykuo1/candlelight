package net.jimboi.canary.stage_a.smuc;

import net.jimboi.canary.stage_a.base.TextureAtlasBuilder;
import net.jimboi.canary.stage_a.smuc.screen.ScreenManager;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.input.InputContext;
import org.bstone.render.RenderEngine;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 11/21/17.
 */
public class Smuc extends GameEngine
{
	public static Smuc INST;
	public static Application APP;

	public static void main(String[] args)
	{
		APP = new Application()
				.setFramework(INST = new Smuc());
		APP.run();
	}

	private Console console;
	private ScreenManager screenManager;

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		renderEngine.getWindow().setWindowSize(480, 640);

		this.assetManager.registerResourceLocation("program.simple",
				new ResourceLocation("gordo:program_simple.res"));
		this.assetManager.registerResourceLocation("vertex_shader.simple",
				new ResourceLocation("gordo:simple.vsh"));
		this.assetManager.registerResourceLocation("fragment_shader.simple",
				new ResourceLocation("gordo:simple.fsh"));
		this.assetManager.registerResourceLocation("bitmap.font",
				new ResourceLocation("gordo:font.png"));
		this.assetManager.registerResourceLocation("texture.font",
				new ResourceLocation("gordo:texture_font.res"));
		TextureAtlasBuilder tab = new TextureAtlasBuilder(
				this.assetManager.getAsset("texture", "font"),
				256, 256);
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.assetManager.cacheResource("texture_atlas", "font", tab.bake());

		this.console = new Console(this.window.getCurrentViewPort(),
				this.assetManager,
				this.assetManager.getAsset("texture_atlas", "font"),
				this.assetManager.getAsset("program", "simple"));

		this.screenManager = new ScreenManager();
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.screenManager.render(this.console.getView());
		this.console.render();
	}

	@Override
	public void onInputUpdate(InputContext context)
	{

	}
}
