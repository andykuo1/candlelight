package net.jimboi.mesh;

/**
 * Created by Andy on 4/28/17.
 */
public class MeshData
{
	public float[] positions;
	public int[] indices;

	public float[] normals;
	public float[][] attributes;

	public MeshData(float[] positions, int[] indices, float[] normals, float[]... attributes)
	{
		this.positions = positions;
		this.indices = indices;
		this.normals = normals;
		this.attributes = attributes;
	}
}