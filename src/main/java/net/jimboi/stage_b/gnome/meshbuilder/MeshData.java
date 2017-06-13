package net.jimboi.stage_b.gnome.meshbuilder;

import net.jimboi.stage_b.gnome.meshpack.MeshBuffer3;

/**
 * Created by Andy on 5/10/17.
 */
public final class MeshData extends MeshBuffer3
{
	MeshData(float[] positions, float[] texcoords, float[] normals, int[] indices, float furthest)
	{
		super(positions, texcoords, normals, indices, furthest);
	}
}
