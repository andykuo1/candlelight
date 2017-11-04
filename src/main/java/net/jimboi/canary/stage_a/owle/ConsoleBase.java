package net.jimboi.canary.stage_a.owle;

import net.jimboi.boron.base_ab.asset.Asset;

import org.bstone.input.TextHandler;
import org.bstone.input.event.ActionEvent;
import org.bstone.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.zilar.sprite.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 10/20/17.
 */
public class ConsoleBase
{
	private final Console console;
	private final Transform transform;
	private final RasterView view;
	private final RasterView background;
	private final Matrix4f backgroundOffset;

	private TextHandler textHandler;

	private final List<ViewComponent> components = new ArrayList<>();

	public ConsoleBase(Console console, Transform transform, Asset<TextureAtlas> textureAtlas, int width, int height)
	{
		this.console = console;
		this.transform = transform;

		this.view = new RasterView(width, height, textureAtlas);
		this.background = new RasterView(width, height, textureAtlas);
		this.backgroundOffset = new Matrix4f().translate(0.0625F, -0.0625F, 0);

		this.view.clear((byte) 0, 0xFFFFFF);
		this.background.clear((byte) 0, 0x888888);

		Console.getConsole().getInputEngine().getDefaultContext()
				.registerEvent("newline",
						Console.getConsole().getInputEngine().getKeyboard().getButton(GLFW.GLFW_KEY_ENTER)::getAction);
		this.textHandler = Console.getConsole().getInputEngine().getText();

		this.initialize();
	}

	public void initialize()
	{
		this.components.add(0, new ViewComponentTextPanel(0, 1, this.view.getWidth(), this.view.getHeight() - 1));
		this.components.add(1, new ViewComponentPanel(0, 0, this.view.getWidth(), 1));
		this.components.add(2, new ViewComponentText(0, 0, ": ").setMaxLength(this.view.getWidth()));
	}

	public void terminate()
	{
		try
		{
			this.view.close();
			this.background.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
	public void render(ConsoleProgramRenderer renderer)
	{
		for(ViewComponent component : this.components)
		{
			component.render(this.view);
		}

		float mouseX = Console.getConsole().getInputEngine().getDefaultContext().getRange("mousex").getRange();
		float mouseY = Console.getConsole().getInputEngine().getDefaultContext().getRange("mousey").getRange();

		Vector2f vec = this.console.screenSpace.getPoint2DFromScreen(mouseX, mouseY, new Vector2f());
		this.view.setPixelType((int)Math.floor(vec.x()), (int)Math.floor(vec.y()), (byte) 'X');

		this.background.setPixelTypes(this.view);

		this.transform.getTransformation(_MAT);
		this.view.doRender(_MAT, renderer);
		this.background.doRender(_MAT.mul(this.backgroundOffset), renderer);
	}
}
