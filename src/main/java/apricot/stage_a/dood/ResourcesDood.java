package apricot.stage_a.dood;

import apricot.stage_a.dood.render.RenderBillboard;
import apricot.stage_a.dood.render.RenderDiffuse;
import apricot.stage_a.dood.worldgen.WorldGenHills;
import apricot.stage_a.dood.worldgen.WorldGenTerrain;
import apricot.stage_a.mod.ModLight;
import apricot.stage_a.mod.model.Model;
import apricot.stage_a.mod.render.RenderManager;

import apricot.bstone.camera.PerspectiveCamera;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 5/27/17.
 */
public class ResourcesDood
{
	public static final ResourcesDood INSTANCE = new ResourcesDood();

	public final List<ModLight> lights = new ArrayList<>();
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
