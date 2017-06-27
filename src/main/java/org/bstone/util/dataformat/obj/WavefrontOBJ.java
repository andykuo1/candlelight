package org.bstone.util.dataformat.obj;

import org.bstone.mogli.Mesh;
import org.zilar.meshbuilder.ModelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 6/11/17.
 */
public class WavefrontOBJ
{
	private final List<Float> geometryVertices = new ArrayList<>();
	private final List<Float> textureVertices = new ArrayList<>();
	private final List<Float> normalVertices = new ArrayList<>();
	private final List<Float> paramSpaceVertices = new ArrayList<>();

	private final List<Integer> geometryIndices = new ArrayList<>();
	private final List<Integer> textureIndices = new ArrayList<>();
	private final List<Integer> normalIndices = new ArrayList<>();

	private final int geometryVertexSize;
	private final int normalVertexSize;
	private final int textureVertexSize;
	private final int paramSpaceVertexSize;

	public WavefrontOBJ(VertexBuffer vb, FaceBuffer fb)
	{
		this.geometryVertexSize = vb.getVertexSize(VertexBuffer.Vertex.GEOMETRY);
		this.textureVertexSize = vb.getVertexSize(VertexBuffer.Vertex.TEXTURE);
		this.normalVertexSize = vb.getVertexSize(VertexBuffer.Vertex.NORMAL);
		this.paramSpaceVertexSize = vb.getVertexSize(VertexBuffer.Vertex.PARAMSPACE);

		this.geometryVertices.addAll(vb.getVertexList(VertexBuffer.Vertex.GEOMETRY));
		this.textureVertices.addAll(vb.getVertexList(VertexBuffer.Vertex.TEXTURE));
		this.normalVertices.addAll(vb.getVertexList(VertexBuffer.Vertex.NORMAL));
		this.paramSpaceVertices.addAll(vb.getVertexList(VertexBuffer.Vertex.PARAMSPACE));

		this.geometryIndices.addAll(fb.geometry);
		this.textureIndices.addAll(fb.texture);
		this.normalIndices.addAll(fb.normal);
	}

	public Mesh createMesh()
	{
		int indices = this.geometryIndices.size();

		int i, j, k, l;
		float[] verticesArray = new float[indices * this.geometryVertexSize];
		float[] texturesArray = new float[indices * this.textureVertexSize];
		float[] normalsArray = new float[indices * this.normalVertexSize];
		int[] indicesArray = new int[indices];
		i = j = k = l = 0;

		int faces = this.geometryIndices.size();
		for (int face = 0; face < faces; ++face)
		{
			int geometry = this.geometryIndices.get(face) - 1;
			int texture = this.textureIndices.get(face) - 1;
			int normal = this.normalIndices.get(face) - 1;

			int geometryVertexIndex = geometry * this.geometryVertexSize;
			int textureVertexIndex = texture * this.textureVertexSize;
			int normalVertexIndex = normal * this.normalVertexSize;

			for (int m = 0; m < VertexBuffer.MAX_VERTEX_SIZE; ++m)
			{
				if (m < this.geometryVertexSize)
				{
					verticesArray[i++] = this.geometryVertices.get(geometryVertexIndex + m);
				}
				if (m < this.textureVertexSize)
				{
					texturesArray[j++] = this.textureVertices.get(textureVertexIndex + m);
				}
				if (m < this.normalVertexSize)
				{
					normalsArray[k++] = this.normalVertices.get(normalVertexIndex + m);
				}
			}

			indicesArray[l++] = face;
		}

		Mesh mesh = new Mesh();
		ModelUtil.putVertexBuffer(mesh, verticesArray, 0, 3, 0);
		ModelUtil.putVertexBuffer(mesh, texturesArray, 1, 2, 0);
		ModelUtil.putVertexBuffer(mesh, normalsArray, 2, 3, 0);
		ModelUtil.putIndexBuffer(mesh, indicesArray);
		return mesh;
	}
}
