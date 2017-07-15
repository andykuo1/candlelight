package net.jimboi.stage_c.gui;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.qsilver.asset.Asset;
import org.qsilver.util.ColorUtil;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;

import java.util.Iterator;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiRenderer implements AutoCloseable
{
	private final GuiManager guiManager;
	private final Asset<Program> program;
	private final Matrix4f projViewMatrix = new Matrix4f();
	private final Matrix4f modelMatrix = new Matrix4f();
	private final Matrix4f modelViewProjMatrix = new Matrix4f();

	private final Mesh QUAD;

	public GuiRenderer(GuiManager guiManager, Asset<Program> program)
	{
		super();
		this.guiManager = guiManager;
		this.program = program;

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		QUAD = ModelUtil.createMesh(mb.bake(false, false));
	}

	@Override
	public void close() throws Exception
	{
		QUAD.close();
	}

	public void render(Camera camera)
	{
		Iterator<Gui> iterator = this.guiManager.guis.iterator();
		Matrix4fc proj = camera.projection();
		Matrix4fc view = camera.view();
		Matrix4fc projView = proj.mul(view, this.projViewMatrix);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final Program program = this.program.getSource();
		program.bind();
		{
			program.setUniform("u_projection", proj);
			program.setUniform("u_view", view);

			while (iterator.hasNext())
			{
				final Gui gui = iterator.next();
				final Mesh mesh = QUAD;
				program.setUniform("u_color", ColorUtil.getNormalizedRGB(gui.color, new Vector3f()));

				Matrix4fc transformation = this.modelMatrix.translation(gui.x, gui.y, 0).scale(gui.width, gui.height, 1);
				Matrix4fc modelViewProj = projView.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				mesh.unbind();
			}
		}
		program.unbind();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
