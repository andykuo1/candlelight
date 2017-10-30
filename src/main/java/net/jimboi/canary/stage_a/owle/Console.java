package net.jimboi.canary.stage_a.owle;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.camera.PerspectiveCamera;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.view.ScreenSpace;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.SpriteUtil;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

/**
 * Created by Andy on 10/20/17.
 */
public class Console implements Game
{
	private static Console INST;
	private static GameEngine ENGINE;
	private static Application APP;

	public static Console getConsole()
	{
		return INST;
	}

	public static GameEngine getEngine()
	{
		return ENGINE;
	}

	public static Application getApplication()
	{
		return APP;
	}

	public static void main(String[] args)
	{
		INST = new Console();
		ENGINE = new GameEngine(INST);
		APP = new Application(ENGINE);
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
	public void onFirstUpdate()
	{

	}

	@Override
	public void onRenderLoad()
	{
		final int width = 24;
		final int height = 16;
		final int halfWidth = width / 2;
		final int halfHeight = height / 2;
		final int dist = 20;

		this.camera = new PerspectiveCamera(halfWidth, halfHeight, dist, ENGINE.getWindow().getWidth(), ENGINE.getWindow().getHeight());
		this.screenSpace = new ScreenSpace(ENGINE.getWindow().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		ENGINE.getInputEngine().getDefaultContext()
				.registerEvent("mousex",
						ENGINE.getInputEngine().getMouse().getCursorX()::getRange);
		ENGINE.getInputEngine().getDefaultContext()
				.registerEvent("mousey",
						ENGINE.getInputEngine().getMouse().getCursorY()::getRange);

		this.renderer = new ConsoleProgramRenderer();

		this.bitmap = new Bitmap(new ResourceLocation("gordo:font.png"));
		this.texture = new Texture(this.bitmap, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);

		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.texture), this.texture.width(), this.texture.height());
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.textureAtlas = SpriteUtil.createTextureAtlas(tab.bake());

		this.consoleBase = new ConsoleBase(this, new Transform3(), Asset.wrap(this.textureAtlas), width, height);
	}

	@Override
	public void onRenderUpdate(double delta)
	{
		this.consoleBase.update();

		this.renderer.bind(this.camera.view(), this.camera.projection());
		{
			this.consoleBase.render(this.renderer);
		}
		this.renderer.unbind();
	}

	@Override
	public void onRenderUnload()
	{
		this.consoleBase.terminate();
		this.texture.close();
		this.bitmap.close();
		this.renderer.close();
	}
}
