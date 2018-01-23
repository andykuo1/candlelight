package net.jimboi.canary.stage_a.smuc;

import net.jimboi.canary.stage_a.base.TextureAtlasBuilder;
import net.jimboi.canary.stage_a.smuc.screen.ScreenManager;

import org.bstone.application.Application;
import org.bstone.render.RenderEngine;
import org.bstone.util.Direction;
import org.qsilver.ResourceLocation;
import org.zilar.gui.GuiFrame;
import org.zilar.gui.GuiPanel;
import org.zilar.gui.GuiRenderer;
import org.zilar.in.adapter.newadapt.AxisAdapter;
import org.zilar.in.adapter.newadapt.ButtonActionAdapter;
import org.zilar.in.provider.Mouse;

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
		System.exit(0);
	}

	private Console console;
	private ScreenManager screenManager;
	private GuiFrame frame;
	private GuiPanel panel;
	private GuiRenderer guiRenderer;

	@Override
	public void onFixedUpdate()
	{
		super.onFixedUpdate();

		this.frame.setCursorPosition(
				this.inputEngine.getRangeOrDefault("main", "mousex", 0),
				this.inputEngine.getRangeOrDefault("main", "mousey", 0));
		this.frame.setCursorAction(
				this.inputEngine.getActionOrDefault("main", "mouseleft", 0) < 0);
	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		renderEngine.getWindow().setSize(480, 640);

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

		this.inputEngine.register("main", "mousex", new AxisAdapter(this.getMouse().getAxis(Mouse.AXIS_CURSORX)));
		this.inputEngine.register("main", "mousey", new AxisAdapter(this.getMouse().getAxis(Mouse.AXIS_CURSORY)));
		this.inputEngine.register("main", "mouseleft", new ButtonActionAdapter(this.getMouse().getButton(Mouse.BUTTON_MOUSE_LEFT)));

		this.console = new Console(this.window.getView().getCurrentViewPort(),
				this.assetManager,
				this.assetManager.getAsset("texture_atlas", "font"),
				this.assetManager.getAsset("program", "simple"));

		this.screenManager = new ScreenManager();

		this.frame = new GuiFrame(this.window.getView().getCurrentViewPort());
		this.panel = new GuiPanel();
		panel.setOffset(10, 10);
		panel.setSize(100, 50);
		//panel.setRelativeHeight(0.5F);
		panel.setRelativeWidth(0.5F);
		this.panel.setAnchorDirection(Direction.NORTHEAST);
		this.frame.addGui(this.panel);

		this.guiRenderer = new GuiRenderer();
		this.guiRenderer.load(this.assetManager);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine)
	{
		this.screenManager.render(this.console.getView());
		this.console.render();

		this.frame.update();
		this.guiRenderer.render(this.frame, this.frame.getElements());
	}
}
