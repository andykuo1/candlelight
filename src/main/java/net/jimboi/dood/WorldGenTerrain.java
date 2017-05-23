package net.jimboi.dood;

import net.jimboi.mod.meshbuilder.MeshData;
import net.jimboi.mod.meshbuilder.ModelUtil;
import net.jimboi.torchlite.MazeWorld;

import org.bstone.mogli.Mesh;

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
		return ModelUtil.createMesh(md);
	}
}
