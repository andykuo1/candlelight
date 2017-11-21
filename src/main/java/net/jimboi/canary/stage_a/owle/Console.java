package net.jimboi.canary.stage_a.owle;

import net.jimboi.boron.base_ab.asset.Asset;
import net.jimboi.boron.base_ab.sprite.SpriteUtil;
import net.jimboi.boron.base_ab.sprite.TextureAtlas;
import net.jimboi.boron.base_ab.sprite.TextureAtlasBuilder;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.tick.TickEngine;
import org.bstone.transform.Transform3;
import org.bstone.util.Direction;
import org.bstone.window.view.ScreenSpace;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.ResourceLocation;

/**
 * Created by Andy on 10/20/17.
 */
public class Console extends GameEngine implements Game
{
	private static Console INST;
	private static Application APP;

	public static Console getConsole()
	{
		return INST;
	}

	public static Application getApplication()
	{
		return APP;
	}

	public static void main(String[] args)
	{
		APP = new Application()
				.setFramework(INST = new Console());
		APP.run();
	}

	public PerspectiveCamera camera;
	public ConsoleProgramRenderer renderer;

	public Bitmap bitmap;
	public Texture texture;
	public TextureAtlas textureAtlas;

	public ScreenSpace screenSpace;

	public ConsoleBase consoleBase;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{

	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		final int width = 24;
		final int height = 16;
		final int halfWidth = width / 2;
		final int halfHeight = height / 2;
		final int dist = 20;

		this.camera = new PerspectiveCamera(halfWidth, halfHeight, dist, this.getWindow().getWidth(), this.getWindow().getHeight());
		this.screenSpace = new ScreenSpace(this.getWindow().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		this.getInputEngine().getDefaultContext()
				.registerEvent("mousex",
						this.getInputEngine().getMouse().getCursorX()::getRange);
		this.getInputEngine().getDefaultContext()
				.registerEvent("mousey",
						this.getInputEngine().getMouse().getCursorY()::getRange);

		this.renderer = new ConsoleProgramRenderer();

		this.bitmap = new Bitmap(new ResourceLocation("gordo:font.png"));
		this.texture = new Texture(this.bitmap, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);

		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.texture), this.texture.width(), this.texture.height());
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.textureAtlas = SpriteUtil.createTextureAtlas(tab.bake());

		this.consoleBase = new ConsoleBase(this, new Transform3(), Asset.wrap(this.textureAtlas), width, height);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine, double delta)
	{
		this.consoleBase.update();

		this.renderer.bind(this.camera.view(), this.camera.projection());
		{
			this.consoleBase.render(this.renderer);
		}
		this.renderer.unbind();
	}

	@Override
	public void onRenderUnload(RenderEngine renderEngine)
	{
		this.consoleBase.terminate();

		try
		{
			this.texture.close();
			this.bitmap.close();
			this.renderer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
