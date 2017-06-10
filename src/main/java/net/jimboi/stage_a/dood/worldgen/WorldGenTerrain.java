package net.jimboi.stage_a.dood.worldgen;

import net.jimboi.stage_a.blob.torchlite.MazeWorld;
import net.jimboi.stage_b.gnome.meshbuilder.MeshData;
import net.jimboi.stage_b.gnome.meshbuilder.ModelUtil;

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
