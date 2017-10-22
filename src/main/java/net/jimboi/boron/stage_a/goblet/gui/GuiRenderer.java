package net.jimboi.boron.stage_a.goblet.gui;

import net.jimboi.apricot.base.material.OldMaterial;
import net.jimboi.apricot.base.renderer.property.OldPropertyTexture;
import net.jimboi.boron.base_ab.render.OldRenderEngine;
import net.jimboi.boron.base_ab.render.OldishRenderService;
import net.jimboi.boron.stage_a.goblet.gui.base.Gui;
import net.jimboi.boron.stage_a.goblet.gui.base.GuiManager;

import org.bstone.camera.Camera;
import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.mogli.Texture;
import org.bstone.window.view.ScreenSpace;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.qsilver.asset.Asset;
import org.qsilver.util.ColorUtil;
import org.zilar.meshbuilder.MeshBuilder;
import org.zilar.meshbuilder.ModelUtil;
import org.zilar.sprite.Sprite;

import java.util.Iterator;

/**
 * Created by Andy on 7/15/17.
 */
public class GuiRenderer extends OldishRenderService
{
	private final GuiManager guiManager;
	private final Asset<Program> program;

	private final Matrix4f modelMatrix = new Matrix4f();
	private final Matrix4f viewProjMatrix = new Matrix4f();
	private final Matrix4f modelViewProjMatrix = new Matrix4f();
	private final Quaternionf invertedRotation = new Quaternionf();

	private ScreenSpace screenSpace;

	private Mesh QUAD;

	public GuiRenderer(OldRenderEngine renderEngine, GuiManager guiManager, Asset<Program> program)
	{
		super(renderEngine);
		this.guiManager = guiManager;
		this.program = program;

		this.screenSpace = ScreenSpace.deriveOpenGLFormat(this.guiManager.screenSpace);
	}

	@Override
	protected void onServiceStart(OldRenderEngine handler)
	{
		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(0, 0), new Vector2f(1, 1), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		QUAD = ModelUtil.createStaticMesh(mb.bake(false, false));
	}

	@Override
	protected void onServiceStop(OldRenderEngine handler)
	{
		QUAD.close();
	}

	@Override
	protected void onRenderUpdate(OldRenderEngine renderEngine, double delta)
	{

	}

	public void render()
	{
		//TODO: Perspective is not yet working...
		Iterator<Gui> iterator = this.guiManager.elements.iterator();
		final Camera camera = this.guiManager.getCamera();

		Vector3f vec = this.screenSpace.getPoint2DFromScreen(0, 0, new Vector3f());
		final float offsetX = vec.x();
		final float offsetY = vec.y();

		camera.transform().getRotation(this.invertedRotation).invert();
		final Matrix4fc proj = camera.projection();
		final Matrix4fc view = camera.view();
		proj.mul(view, this.viewProjMatrix);

		GL11.glDisable(GL11.GL_DEPTH_TEST);

		final Program program = this.program.getSource();
		program.bind();
		{
			while (iterator.hasNext())
			{
				final Gui gui = iterator.next();
				Mesh mesh = QUAD;

				Texture texture = null;
				Sprite sprite = null;
				if (gui instanceof GuiMaterial)
				{
					OldMaterial material = ((GuiMaterial) gui).material;

					if (material.hasComponent(OldPropertyTexture.class))
					{
						OldPropertyTexture propertyTexture = material.getComponent(OldPropertyTexture.class);
						texture = propertyTexture.getTexture().getSource();
						sprite = propertyTexture.getSprite();
					}
				}
				else if (gui instanceof GuiText)
				{
					TextMesh textMesh = TextMesh.getText(((GuiText) gui).getText());
					mesh = textMesh.getMesh();
					texture = textMesh.getTexture().getSource();
				}

				if (texture != null)
				{
					program.setUniform("u_sampler", 0);
					program.setUniform("u_diffuse_color", new Vector4f(1, 1, 1, 0));
					program.setUniform("u_transparency", true);
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					texture.bind();
				}
				else
				{
					program.setUniform("u_diffuse_color", new Vector4f(ColorUtil.getNormalizedRGB(gui.getColor(), new Vector3f()), 1));
				}

				if (sprite != null)
				{
					program.setUniform("u_tex_offset", new Vector2f(sprite.getU(), sprite.getV()));
					program.setUniform("u_tex_scale", new Vector2f(sprite.getWidth(), sprite.getHeight()));
				}

				Matrix4fc transformation = this.modelMatrix.translation(offsetX + gui.getX(), offsetY - gui.height - gui.getY(), 0).rotate(this.invertedRotation).scale(gui.width, gui.height, 1);
				Matrix4fc modelViewProj = this.viewProjMatrix.mul(transformation, this.modelViewProjMatrix);
				program.setUniform("u_model", transformation);
				program.setUniform("u_model_view_projection", modelViewProj);

				mesh.bind();
				{
					GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				}
				mesh.unbind();

				if (texture != null)
				{
					texture.unbind();
				}
			}
		}
		program.unbind();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
