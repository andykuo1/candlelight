package net.jimboi.boron.stage_a.gordo;

import org.bstone.game.GameEngine;
import org.bstone.game.GameHandler;
import org.bstone.mogli.Bitmap;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Texture;
import org.bstone.render.RenderEngine;
import org.bstone.render.renderer.SimpleProgramRenderer;
import org.bstone.transform.Transform3;
import org.bstone.util.direction.Direction;
import org.bstone.window.camera.PerspectiveCamera;
import org.bstone.window.view.ScreenSpace;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.qsilver.asset.Asset;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.resource.ResourceLocation;
import org.zilar.sprite.SpriteUtil;
import org.zilar.sprite.TextureAtlas;
import org.zilar.sprite.TextureAtlasBuilder;

/**
 * Created by Andy on 9/13/17.
 */
public class GordoTest implements GameHandler
{
	private static GameEngine ENGINE;
	private static GordoTest INSTANCE;

	public static GameEngine getEngine()
	{
		return ENGINE;
	}

	public static GordoTest getGordo()
	{
		return INSTANCE;
	}

	public static void main(String[] args)
	{
		INSTANCE = new GordoTest();
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

	public Gordo gordo;

	@Override
	public void onLoad(RenderEngine renderEngine)
	{
		final int width = 24;
		final int height = 16;
		final int halfWidth = width / 2;
		final int halfHeight = height / 2;
		final int dist = 20;

		this.camera = new PerspectiveCamera(halfWidth, halfHeight, dist, ENGINE.getWindow().getWidth(), ENGINE.getWindow().getHeight());
		this.screenSpace = new ScreenSpace(ENGINE.getWindow().getCurrentViewPort(), this.camera, Direction.CENTER, Direction.NORTHEAST);

		ENGINE.getInput().registerMousePosX("mousex");
		ENGINE.getInput().registerMousePosY("mousey");

		this.renderer = new SimpleProgramRenderer();

		this.bitmap = new Bitmap(new ResourceLocation("base:font.png"));
		this.texture = new Texture(this.bitmap, GL11.GL_NEAREST, GL12.GL_CLAMP_TO_EDGE);

		TextureAtlasBuilder tab = new TextureAtlasBuilder(Asset.wrap(this.texture), this.texture.width(), this.texture.height());
		tab.addTileSheet(0, 0, 16, 16, 0, 0, 16, 16);
		this.textureAtlas = SpriteUtil.createTextureAtlas(tab.bake());

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		this.mesh = ModelUtil.createStaticMesh(mb.bake(false, false));

		this.gordo = new Gordo(new Transform3(), Asset.wrap(this.mesh), Asset.wrap(this.textureAtlas), width, height);
	}

	@Override
	public void onRender(RenderEngine renderEngine, double delta)
	{
		this.gordo.update();

		this.renderer.bind(this.camera.view(), this.camera.projection());
		{
			this.gordo.render(this.renderer);
		}
		this.renderer.unbind();
	}

	@Override
	public void onUnload(RenderEngine renderEngine)
	{
		this.mesh.close();
		this.texture.close();
		this.bitmap.close();
		this.renderer.close();
	}
}
