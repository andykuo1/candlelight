package canary.smuc;

import canary.base.renderer.SimpleRenderer;
import canary.smuc.screen.ViewComponent;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.camera.Camera;
import canary.bstone.camera.OrthographicCamera;
import canary.bstone.mogli.Program;
import canary.bstone.sprite.textureatlas.TextureAtlas;
import canary.bstone.util.Direction;
import canary.bstone.window.view.ScreenSpace;
import canary.bstone.window.view.ViewPort;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 11/21/17.
 */
public class Console
{
	private final Camera camera;
	private final ScreenSpace screen;

	private final SimpleRenderer renderer;
	private final Matrix4f transformation = new Matrix4f();
	private final RasterizedView view;

	private List<ViewComponent> components = new ArrayList<>();

	public Console(ViewPort viewport, AssetManager assets, Asset<TextureAtlas> textureAtlas, Asset<Program> program)
	{
		this.camera = new OrthographicCamera(viewport.getWidth(), viewport.getHeight());
		this.screen = new ScreenSpace(viewport, this.camera, Direction.CENTER, Direction.NORTHEAST);

		int width = 15;
		int height = 15;
		this.view = new RasterizedView(width, height).setTextureAtlas(textureAtlas);
		this.renderer = new SimpleRenderer(program);
		this.transformation.translate(-width / 2F, -height / 2F, 0);

		this.view.load(assets);
	}

	public void render()
	{
		this.renderer.bind(this.camera.view(), this.camera.projection());
		{
			this.view.update();
			this.renderer.draw(this.view.getMesh(), this.view.getMaterial(), this.transformation);
		}
		this.renderer.unbind();
	}

	public RasterizedView getView()
	{
		return this.view;
	}
}
