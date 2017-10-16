package net.jimboi.boron.stage_a.gordo;

import net.jimboi.boron.base.window.input.TextHandler;

import org.bstone.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.qsilver.asset.Asset;
import org.zilar.sprite.TextureAtlas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 9/13/17.
 */
public class Gordo
{
	private final Transform transform;
	private final RasterView view;
	private final RasterView background;
	private final Matrix4f backgroundOffset;

	private TextHandler textHandler;

	private final List<ViewComponent> components = new ArrayList<>();

	public Gordo(Transform transform, Asset<TextureAtlas> textureAtlas, int width, int height)
	{
		this.transform = transform;

		this.view = new RasterView(width, height, textureAtlas);
		this.background = new RasterView(width, height, textureAtlas);
		this.backgroundOffset = new Matrix4f().translate(0.0625F, -0.0625F, 0);

		this.view.clear((byte) 0, 0xFFFFFF);
		this.background.clear((byte) 0, 0x888888);

		GordoTest.getEngine().getWindow().getInputEngine().getKeyboard().addTextHandler(this.textHandler = new TextHandler());
		GordoTest.getEngine().getInput().registerKey("newline", GLFW.GLFW_KEY_ENTER);

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
		this.view.close();
		this.background.close();
	}

	public void update()
	{
		((ViewComponentText) this.components.get(2)).setText(": " + this.textHandler.get());
		if (GordoTest.getEngine().getInput().isInputPressed("newline"))
		{
			((ViewComponentTextPanel) this.components.get(0)).append("\n" + this.textHandler.get());
			this.textHandler.clear();
		}

		for(ViewComponent component : this.components)
		{
			component.update();
		}
	}

	private final Matrix4f _MAT = new Matrix4f();
	public void render(GordoProgramRenderer renderer)
	{
		for(ViewComponent component : this.components)
		{
			component.render(this.view);
		}

		float mouseX = GordoTest.getEngine().getInput().getInputAmount("mousex");
		float mouseY = GordoTest.getEngine().getInput().getInputAmount("mousey");
		Vector2f vec = GordoTest.getGordo().screenSpace.getPoint2DFromScreen(mouseX, mouseY, new Vector2f());
		this.view.setPixelType((int)Math.floor(vec.x()), (int)Math.floor(vec.y()), (byte) 'X');

		this.background.setPixelTypes(this.view);

		this.transform.getTransformation(_MAT);
		this.view.doRender(_MAT, renderer);
		this.background.doRender(_MAT.mul(this.backgroundOffset), renderer);
	}
}
