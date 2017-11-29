package net.jimboi.canary.stage_a.smuc.gui;

import net.jimboi.canary.stage_a.base.MaterialProperty;

import org.bstone.asset.Asset;
import org.bstone.asset.AssetManager;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL11;
import org.qsilver.ResourceLocation;

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
				new ResourceLocation("base:rect.obj"));

		this.program = assets.getAsset("program", "gui");
		this.mesh = assets.getAsset("mesh", "gui");
	}

	public void render(GuiFrame frame, Iterable<GuiBase> guis)
	{
		Program program = this.program.get();
		program.bind();
		{
			Mesh mesh = this.mesh.get();
			mesh.bind();

			Matrix4f transformation = new Matrix4f();
			for(GuiBase gui : guis)
			{
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
