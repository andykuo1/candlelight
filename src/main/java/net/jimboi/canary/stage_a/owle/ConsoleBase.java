package net.jimboi.canary.stage_a.owle;

import net.jimboi.canary.stage_a.base.renderer.SimpleRenderer;
import net.jimboi.canary.stage_a.smuc.RasterizedView;

import org.bstone.asset.Asset;
import org.bstone.asset.AssetManager;
import org.bstone.input.TextHandler;
import org.bstone.input.event.ActionEvent;
import org.bstone.sprite.textureatlas.TextureAtlas;
import org.bstone.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/20/17.
 */
public class ConsoleBase
{
	private final Console console;
	private final Transform transform;
	private final RasterizedView view;
	private final RasterizedView background;
	private final Matrix4f backgroundOffset;

	private TextHandler textHandler;

	private final List<ViewComponent> components = new ArrayList<>();

	public ConsoleBase(Console console, Transform transform, Asset<TextureAtlas> textureAtlas, int width, int height)
	{
		this.console = console;
		this.transform = transform;

		this.view = new RasterizedView(width, height).setTextureAtlas(textureAtlas);
		this.background = new RasterizedView(width, height).setTextureAtlas(textureAtlas);
		this.backgroundOffset = new Matrix4f().translate(0.0625F, -0.0625F, 0);

		this.view.forEach((vector2ic, character, integer) ->
				this.view.draw(vector2ic.x(), vector2ic.y(), 'A', 0xFFFFFF));
		this.background.forEach((vector2ic, character, integer) ->
				this.background.draw(vector2ic.x(), vector2ic.y(), 'A', 0x888888));
		//this.view.clear((byte) 0, 0xFFFFFF);
		//this.background.clear((byte) 0, 0x888888);

		Console.getConsole().getInputEngine().getDefaultContext()
				.registerEvent("newline",
						Console.getConsole().getInputEngine().getKeyboard().getButton(GLFW.GLFW_KEY_ENTER)::getAction);
		this.textHandler = Console.getConsole().getInputEngine().getText();

		this.initialize();
	}

	public void load(AssetManager assets)
	{
		this.background.load(assets);
		this.view.load(assets);
	}

	public void initialize()
	{
		this.components.add(0, new ViewComponentTextPanel(0, 1, this.view.getWidth(), this.view.getHeight() - 1));
		this.components.add(1, new ViewComponentPanel(0, 0, this.view.getWidth(), 1));
		this.components.add(2, new ViewComponentText(0, 0, ": ").setMaxLength(this.view.getWidth()));
	}

	public void terminate()
	{
	}

	public void update()
	{
		final StringBuffer sb = this.textHandler.getBuffer();

		((ViewComponentText) this.components.get(2)).setText(": " + sb);
		ActionEvent newline = Console.getConsole().getInputEngine().getDefaultContext().getAction("newline");
		if (newline.isPressed() && sb.length() > 0)
		{
			newline.consume();

			((ViewComponentTextPanel) this.components.get(0)).append("\n" + sb);
			this.textHandler.clear();
		}

		for(ViewComponent component : this.components)
		{
			component.update();
		}
	}

	private final Matrix4f _MAT = new Matrix4f();
	public void render(SimpleRenderer renderer)
	{
		for(ViewComponent component : this.components)
		{
			component.render(this.view);
		}

		float mouseX = Console.getConsole().getInputEngine().getDefaultContext().getRange("mousex").getRange();
		float mouseY = Console.getConsole().getInputEngine().getDefaultContext().getRange("mousey").getRange();

		Vector2f vec = this.console.screenSpace.getPoint2DFromScreen(mouseX, mouseY, new Vector2f());
		this.view.glyph((int)Math.floor(vec.x()), (int)Math.floor(vec.y()), 'X');

		for(int i = 0; i < this.view.getGlyphs().array().length; ++i)
		{
			this.background.getGlyphs().array()[i] = this.view.getGlyphs().array()[i];
		}

		this.transform.getTransformation(_MAT);

		this.view.update();
		this.background.update();
		renderer.draw(this.view.getMesh(), this.view.getMaterial(), _MAT);
		renderer.draw(this.view.getMesh(), this.view.getMaterial(), _MAT.mul(this.backgroundOffset));
	}
}
