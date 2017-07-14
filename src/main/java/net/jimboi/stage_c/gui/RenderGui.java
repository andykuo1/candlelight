package net.jimboi.stage_c.gui;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.util.direction.Direction;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.qsilver.asset.Asset;
import org.zilar.sprite.NineSheet;
import org.zilar.sprite.Sprite;

import java.util.Iterator;

/**
 * Created by Andy on 7/12/17.
 */
public class RenderGui
{
	private final Asset<Program> program;
	private final Asset<Mesh> mesh;

	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();

	public RenderGui(Asset<Program> program, Asset<Mesh> mesh)
	{
		this.program = program;
		this.mesh = mesh;
	}

	public void render(Camera camera, Iterator<Gui> guis)
	{
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);
			program.setUniform("u_transparency", true);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			program.setUniform("u_sampler", 0);

			final Mesh mesh = this.mesh.getSource();

			mesh.bind();
			{
				while (guis.hasNext())
				{
					Gui gui = guis.next();
					if (!gui.isVisible()) continue;

					int x = gui.getX();
					int y = gui.getY();
					int z = gui.getDepth();

					if (gui.getBorderTexture() == null)
					{

					}
					else
					{
						float guiScale = 1;
						float guiWidth = gui.getWidth();
						float guiHeight = gui.getHeight();

						NineSheet sheet = gui.getBorderTexture();
						Sprite west = sheet.get(Direction.WEST);
						Sprite north = sheet.get(Direction.NORTH);
						Sprite east = sheet.get(Direction.EAST);
						Sprite south = sheet.get(Direction.SOUTH);
						Sprite center = sheet.get(Direction.CENTER);

						float nineWidth = west.getWidth() + center.getWidth() + east.getWidth();
						float nineHeight = north.getHeight() + center.getHeight() + south.getHeight();

						float westNine = (west.getWidth() / nineWidth);
						float centerNine = (center.getWidth() / nineWidth);
						float eastNine = (east.getWidth() / nineWidth);

						float westWidth = (west.getWidth() / nineWidth) * guiWidth;
						float centerWidth = (center.getWidth() / nineWidth) * guiWidth;
						float eastWidth = (east.getWidth() / nineWidth) * guiWidth;

						float northHeight = (north.getHeight() / nineHeight) * guiHeight;
						float centerHeight = (center.getHeight() / nineHeight) * guiHeight;
						float southHeight = (south.getHeight() / nineHeight) * guiHeight;

						this.renderQuad(program, view, proj, mesh,
								x, y, z,
								westNine * guiScale, guiScale,
								0, 0,
								sheet.get(Direction.NORTHWEST));
						this.renderQuad(program, view, proj, mesh,
								x, y, z,
								centerNine, northHeight,
								west.getWidth() / nineWidth, 0,
								sheet.get(Direction.NORTH));
						this.renderQuad(program, view, proj, mesh,
								x, y, z,
								eastWidth, northHeight,
								(west.getWidth() + center.getWidth()) / nineWidth, 0,
								sheet.get(Direction.NORTHEAST));

						/*
						this.renderQuad(program, view, proj, mesh,
								x, y - northHeight, z,
								westWidth, centerHeight,
								sheet.get(Direction.WEST));
						this.renderQuad(program, view, proj, mesh,
								x + westWidth, y - northHeight, z,
								centerWidth, centerHeight,
								sheet.get(Direction.CENTER));
						this.renderQuad(program, view, proj, mesh,
								x + westWidth + centerWidth, y - northHeight, z,
								eastWidth, centerHeight,
								sheet.get(Direction.EAST));

						this.renderQuad(program, view, proj, mesh,
								x, y - northHeight - centerHeight, z,
								westWidth, southHeight,
								sheet.get(Direction.SOUTHWEST));
						this.renderQuad(program, view, proj, mesh,
								x + westWidth, y - northHeight - centerHeight, z,
								centerWidth, southHeight,
								sheet.get(Direction.SOUTH));
						this.renderQuad(program, view, proj, mesh,
								x + westWidth + centerWidth, y - northHeight - centerHeight, z,
								eastWidth, southHeight,
								sheet.get(Direction.SOUTHEAST));
						*/
					}
				}
			}
			mesh.unbind();
		}
		program.unbind();
	}

	private void renderQuad(Program program, Matrix4fc view, Matrix4fc proj, Mesh mesh, float x, float y, float z, float width, float height, float u, float v, Sprite sprite)
	{
		final Matrix4fc transformation = this.modelMatrix.identity().translate(x, y, z).scale(width, height, 1).translate(0.5F, -0.5F, 0).translate(u, v, 0);
		final Matrix4fc modelView = view.mul(transformation, this.modelViewMatrix);
		final Matrix4fc modelViewProj = proj.mul(modelView, this.modelViewProjMatrix);
		program.setUniform("u_model", transformation);
		program.setUniform("u_model_view_projection", modelViewProj);

		program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
		program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));

		final Texture texture = sprite.getTexture().getSource();
		texture.bind();
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		texture.unbind();
	}
}
