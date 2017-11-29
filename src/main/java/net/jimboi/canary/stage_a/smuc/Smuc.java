package net.jimboi.canary.stage_a.smuc;

import net.jimboi.canary.stage_a.base.TextureAtlasBuilder;
import net.jimboi.canary.stage_a.smuc.gui.GuiManager;
import net.jimboi.canary.stage_a.smuc.gui.GuiPanel;
import net.jimboi.canary.stage_a.smuc.gui.GuiRenderer;
import net.jimboi.canary.stage_a.smuc.screen.ScreenManager;

import org.bstone.application.Application;
import org.bstone.application.game.GameEngine;
import org.bstone.input.InputContext;
import org.bstone.render.RenderEngine;
import org.bstone.util.Direction;
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
	private GuiManager guiManager;
	private GuiRenderer guiRenderer;
	private GuiPanel panel;

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

		this.input.registerEvent("mousex", this.inputEngine.getMouse().getCursorX()::getRange);
		this.input.registerEvent("mousey", this.inputEngine.getMouse().getCursorY()::getRange);
		this.input.registerEvent("mouseleft", this.inputEngine.getMouse().getButton(0)::getAction);

		this.console = new Console(this.window.getCurrentViewPort(),
				this.assetManager,
				this.assetManager.getAsset("texture_atlas", "font"),
				this.assetManager.getAsset("program", "simple"));

		this.screenManager = new ScreenManager();

		//ViewPort viewport = new ViewPort(0, 0, 200, 200);

		this.guiManager = new GuiManager(this.window.getCurrentViewPort());
		this.panel = new GuiPanel(this.guiManager);
		panel.setOffset(10, 10);
		panel.setSize(100, 50);
		//panel.setRelativeHeight(0.5F);
		panel.setRelativeWidth(0.5F);
		this.panel.setAnchorDirection(Direction.NORTHEAST);
		this.guiManager.getFrame().addGui(panel);

		this.guiRenderer = new GuiRenderer();
		this.guiRenderer.load(this.assetManager);
	}

	private int i = 0;

	@Override
	public void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		if (++this.i > 20)
		{
			//this.panel.setAnchorDirection(Direction.getClockwise(this.panel.getAnchorDirection()));
			this.i = 0;
		}

		this.screenManager.render(this.console.getView());
		this.console.render();

		this.guiManager.update();
		this.guiRenderer.render(this.guiManager.getFrame(), this.guiManager.getElements());
	}

	@Override
	public void onInputUpdate(InputContext context)
	{
		this.guiManager.setCursorPosition(this.input.getRange("mousex").getRange(), this.input.getRange("mousey").getRange());
		this.guiManager.setCursorAction(this.input.getAction("mouseleft").isReleasedAndConsume());
	}
}
