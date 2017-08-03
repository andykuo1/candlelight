package net.jimboi.boron.stage_a.shroom.woot;

import net.jimboi.boron.stage_a.shroom.RenderShroomBase;
import net.jimboi.boron.stage_a.shroom.Shroom;

import org.bstone.mogli.Mesh;
import org.bstone.mogli.Program;
import org.bstone.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.qsilver.asset.Asset;
import org.qsilver.render.RenderEngine;
import org.qsilver.resource.MeshLoader;
import org.zilar.collision.CollisionRenderer;
import org.zilar.meshbuilder.MeshBuilder;

/**
 * Created by Andy on 7/17/17.
 */
public class RenderWoot extends RenderShroomBase
{
	protected CollisionRenderer collisionRenderer;

	private Matrix4f boundingOffsetViewMatrix = new Matrix4f().rotateX(Transform.HALF_PI);

	@Override
	protected void onLoad(RenderEngine renderEngine)
	{
		super.onLoad(renderEngine);

		MeshBuilder mb = new MeshBuilder();
		mb.addPlane(new Vector2f(-0.5F, -0.5F), new Vector2f(0.5F, 0.5F), 0, new Vector2f(0, 0), new Vector2f(1, 1));
		Shroom.ENGINE.getAssetManager().registerAsset(Mesh.class, "billboard", new MeshLoader.MeshParameter(mb.bake(false, false)));
		mb.clear();

		final Asset<Program> wireframeProgram = Shroom.ENGINE.getAssetManager().getAsset(Program.class, "wireframe");

		this.collisionRenderer = new CollisionRenderer(this.getScene().getWorld().getCollisionManager(), wireframeProgram);
		renderEngine.startService(this.collisionRenderer);
	}

	@Override
	protected void onRender(RenderEngine renderEngine)
	{
		super.onRender(renderEngine);

		this.collisionRenderer.render(this.getMainCamera(), this.boundingOffsetViewMatrix);
	}

	@Override
	protected void onUnload(RenderEngine renderEngine)
	{
		super.onUnload(renderEngine);

		renderEngine.stopService(this.collisionRenderer);
	}

	@Override
	protected SceneWoot getScene()
	{
		return (SceneWoot) super.getScene();
	}
}
