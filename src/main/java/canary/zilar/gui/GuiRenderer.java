package canary.zilar.gui;

import canary.base.MaterialProperty;

import canary.bstone.asset.Asset;
import canary.bstone.asset.AssetManager;
import canary.bstone.mogli.Mesh;
import canary.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import canary.qsilver.ResourceLocation;

import java.util.Iterator;

/**
 * Created by Andy on 11/28/17.
 */
public class GuiRenderer
{
	private static final Vector4fc DEFAULT_PROPERTY_COLOR = new Vector4f(1, 1, 1, 1);
	private static final Vector2fc DEFAULT_PROPERTY_SPRITE_OFFSET = new Vector2f();
	private static final Vector2fc DEFAULT_PROPERTY_SPRITE_SCALE = new Vector2f(1, 1);
	private static final boolean DEFAULT_PROPERTY_TRANSPARENCY = true;

	private Asset<Program> program;
	private Asset<Mesh> mesh;

	public void load(AssetManager assets)
	{
		assets.registerResourceLocation("program.gui",
				new ResourceLocation("gui:program_gui.res"));
		assets.registerResourceLocation("vertex_shader.gui",
				new ResourceLocation("gui:gui.vsh"));
		assets.registerResourceLocation("fragment_shader.gui",
				new ResourceLocation("gui:gui.fsh"));
		assets.registerResourceLocation("mesh.gui",
				new ResourceLocation("gui:rect.obj"));

		this.program = assets.getAsset("program", "gui");
		this.mesh = assets.getAsset("mesh", "gui");
	}

	public void render(GuiFrame frame, Iterator<GuiBase> iter)
	{
		Program program = this.program.get();
		program.bind();
		{
			Mesh mesh = this.mesh.get();
			mesh.bind();

			Matrix4f transformation = new Matrix4f();
			while(iter.hasNext())
			{
				GuiBase gui = iter.next();
				if (gui.isVisible())
				{
					float x = gui.getScreenX() / frame.getScreenWidth();
					float y = gui.getScreenY() / frame.getScreenHeight();
					float w = gui.getScreenWidth() / frame.getScreenWidth();
					float h = gui.getScreenHeight() / frame.getScreenHeight();

					transformation.identity();
					transformation.translate(-1, -1, 0);
					transformation.scale(2, 2, 1);

					transformation.translate(x, 1 - y - h, 0);
					transformation.scale(w, h, 1);

					MaterialProperty.MODELVIEWPROJECTION.apply(program, transformation);

					Vector4fc color = DEFAULT_PROPERTY_COLOR;
					if (gui.getState() == GuiState.HOVER)
					{
						color = new Vector4f(1, 1, 0, 1);
					}
					else if (gui.getState() == GuiState.CLICK)
					{
						color = new Vector4f(1, 0, 1, 1);
					}
					MaterialProperty.COLOR.apply(program, color);

					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
			}

			mesh.unbind();
		}
		program.unbind();
	}
}
