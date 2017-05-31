package net.jimboi.dood;

import net.jimboi.dood.render.RenderBillboard;
import net.jimboi.dood.render.RenderDiffuse;
import net.jimboi.dood.worldgen.WorldGenHills;
import net.jimboi.dood.worldgen.WorldGenTerrain;
import net.jimboi.mod.Light;

import org.bstone.camera.PerspectiveCamera;
import org.qsilver.render.Model;
import org.qsilver.render.RenderManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/27/17.
 */
public class ResourcesDood
{
	public static final ResourcesDood INSTANCE = new ResourcesDood();

	public final List<Light> lights = new ArrayList<>();
	public PerspectiveCamera camera;

	private ResourcesDood()
	{
	}

	public WorldGenTerrain terrain;
	public WorldGenHills hills;

	public void load(RenderManager renderManager)
	{
		Resources.load();

		//Register Renders
		renderManager.register("diffuse", new RenderDiffuse(Resources.getProgram("diffuse")));
		renderManager.register("billboard", new RenderBillboard(Resources.getProgram("billboard"),
				RenderBillboard.Type.CYLINDRICAL));

		this.hills = new WorldGenHills();
		hills.generate();
		Resources.register("hills", hills.createMeshFromVertices());
		Resources.register("hills", new Model(Resources.getMesh("hills")));

		this.terrain = new WorldGenTerrain();
		terrain.generate();
		Resources.register("dungeon", terrain.createMeshFromVertices());
		Resources.register("dungeon", new Model(Resources.getMesh("dungeon")));
	}
}
