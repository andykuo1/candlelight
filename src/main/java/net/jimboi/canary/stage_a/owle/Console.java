package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.base.TextureAtlasBuilder;
import net.jimboi.canary.stage_a.base.renderer.SimpleRenderer;

import org.bstone.application.Application;
import org.bstone.application.game.Game;
import org.bstone.application.game.GameEngine;
import org.bstone.asset.Asset;
import org.bstone.camera.OrthographicCamera;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.sprite.textureatlas.TextureAtlas;
import org.bstone.tick.TickEngine;
import org.bstone.transform.Transform3;
import org.bstone.util.Direction;
import org.bstone.window.view.ScreenSpace;
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

	public OrthographicCamera camera;
	public SimpleRenderer renderer;

	public Asset<Bitmap> bitmap;
	public Asset<Texture> texture;
	public Asset<TextureAtlas> textureAtlas;

	public ScreenSpace screenSpace;

	public ConsoleBase consoleBase;

	@Override
	public void onFirstUpdate(TickEngine tickEngine)
	{

	}

	@Override
	public void onRenderLoad(RenderEngine renderEngine)
	{
		this.window.setSize(480, 640);

		final int width = 16;
		final int height = 16;
		final int halfWidth = width / 2;
		final int halfHeight = height / 2;
		final int dist = 20;

		this.camera = new OrthographicCamera(halfWidth, halfHeight, dist, this.getWindow().getWidth(), this.getWindow().getHeight());
		this.screenSpace = new ScreenSpace(this.getWindow().getView().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		this.getInputEngine().getDefaultContext()
				.registerEvent("mousex",
						this.getInputEngine().getMouse().getCursorX()::getRange);
		this.getInputEngine().getDefaultContext()
				.registerEvent("mousey",
						this.getInputEngine().getMouse().getCursorY()::getRange);

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
		this.bitmap = this.assetManager.getAsset("bitmap", "font");
		this.texture = this.assetManager.getAsset("texture", "font");
		TextureAtlasBuilder tab = new TextureAtlasBuilder(this.texture,
				this.texture.get().width(), this.texture.get().height());
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.assetManager.cacheResource("texture_atlas", "font", tab.bake());
		this.textureAtlas = this.assetManager.getAsset("texture_atlas", "font");

		this.renderer = new SimpleRenderer(this.assetManager.getAsset("program", "simple"));

		this.consoleBase = new ConsoleBase(this, new Transform3(), this.textureAtlas, width, height);
		this.consoleBase.load(this.assetManager);
	}

	@Override
	public void onRenderUpdate(RenderEngine renderEngine)
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
	}
}
