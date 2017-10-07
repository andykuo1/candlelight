package net.jimboi.boron.stage_c.shooter;

import org.bstone.game.GameEngine;
import org.bstone.game.GameHandler;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.util.direction.Direction;
import org.bstone.window.camera.PerspectiveCamera;
import org.bstone.window.view.ScreenSpace;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.TextureAtlas;

/**
 * Created by Andy on 10/6/17.
 */
public class ShooterGame implements GameHandler
{
	private static ShooterGame INSTANCE;
	private static GameEngine ENGINE;

	public static ShooterGame getGame()
	{
		return INSTANCE;
	}

	public static GameEngine getEngine()
	{
		return ENGINE;
	}

	private ShooterGame()
	{}

	public static void main(String[] args)
	{
		INSTANCE = new ShooterGame();
		ENGINE = new GameEngine(INSTANCE);
		ENGINE.start();
	}

	@Override
	public void onFirstUpdate()
	{

	}

	@Override
	public void onPreUpdate()
	{

	}

	@Override
	public void onUpdate()
	{

	}

	@Override
	public void onLastUpdate()
	{

	}

	public PerspectiveCamera camera;
	public SimpleProgramRenderer renderer;

	public Bitmap bitmap;
	public Texture texture;
	public TextureAtlas textureAtlas;
	public Mesh mesh;

	public ScreenSpace screenSpace;

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
		this.camera = new PerspectiveCamera(640, 480);
		this.screenSpace = new ScreenSpace(ENGINE.getWindow()
				.getCurrentViewPort(), this.camera, Direction.CENTER,
				Direction.NORTHEAST);

		ENGINE.getInput().registerMousePosX("mousex");
		ENGINE.getInput().registerMousePosY("mousey");

		this.renderer = new SimpleProgramRenderer();

		this.bitmap = new Bitmap(new ResourceLocation("base:font.png"));

	}

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
	{
		this.renderer.bind(this.camera.view(), this.camera.projection());
		{
			//this.gordo.render(this.renderer);
		}
		this.renderer.unbind();
	}

	@Override
	public void onUnload(RenderEngine renderEngine)
	{

	}
}
