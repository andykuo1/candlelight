package net.jimboi.mod3.meshbuilder;

/**
 * Created by Andy on 5/10/17.
 */
public final class MeshData
{
	public final float[] positions;
	public final float[] texcoords;
	public final float[] normals;
	public final int[] indices;
	public final float furthest;

	MeshData(float[] positions, float[] texcorods, float[] normals, int[] indices, float furthest)
	{
		this.positions = positions;
		this.texcoords = texcorods;
		this.normals = normals;
		this.indices = indices;
		this.furthest = furthest;
	}
}
