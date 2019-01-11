package canary.zilar.meshbuilder.buffer;

/**
 * Created by Andy on 6/12/17.
 */
public class MeshBuffer3 extends MeshBuffer
{
	public MeshBuffer3(float[] positions, float[] texcoords, float[] normals, int[] indices, float furthest)
	{
		super(VertexType.XYZ, positions.length, VertexType.XY, texcoords.length, VertexType.XYZ, normals.length, indices.length, furthest);

		this.positions.put(positions);
		this.positions.flip();
		this.texcoords.put(texcoords);
		this.texcoords.flip();
		this.normals.put(normals);
		this.normals.flip();
		this.indices.put(indices);
		this.indices.flip();
	}
}
