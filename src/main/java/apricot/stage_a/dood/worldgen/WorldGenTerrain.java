package apricot.stage_a.dood.worldgen;

import apricot.stage_a.blob.torchlite.MazeWorld;

import apricot.bstone.mogli.Mesh;
import apricot.zilar.meshbuilder.MeshData;
import apricot.zilar.meshbuilder.ModelUtil;

import java.util.Random;

/**
 * Created by Andy on 5/23/17.
 */
public class WorldGenTerrain
{
	private MazeWorld world;

	public void generate()
	{
		this.world = new MazeWorld(new Random(), 45);
		this.world.generateWorld();
	}

	public Mesh createMeshFromVertices()
	{
		MeshData md = MazeWorld.toMeshBoxData(world, 16, 16);
		return ModelUtil.createStaticMesh(md);
	}
}
