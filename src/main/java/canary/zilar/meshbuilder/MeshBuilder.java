package canary.zilar.meshbuilder;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/10/17.
 */
public class MeshBuilder extends BoxMeshBuilder
{
	public void addPlane(Vector2fc from, Vector2fc to, float z, Vector2fc texTopLeft, Vector2fc texBotRight)
	{
		int i = this.getVertexCount();
		int normal = 1;

		this.addVertex(from.x(), from.y(), z, texBotRight.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(to.x(), from.y(), z, texTopLeft.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(from.x(), to.y(), z, texBotRight.x(), texBotRight.y(), 0, 0, normal);
		this.addVertex(to.x(), from.y(), z, texTopLeft.x(), texTopLeft.y(), 0, 0, normal);
		this.addVertex(to.x(), to.y(), z, texTopLeft.x(), texBotRight.y(), 0, 0, normal);
		this.addVertex(from.x(), to.y(), z, texBotRight.x(), texBotRight.y(), 0, 0, normal);

		this.addVertexIndex(i++, i++, i++);
		this.addVertexIndex(i++, i++, i++);
	}

	public void addPlane(Vector3fc botLeft, Vector3fc topLeft, Vector3fc topRight, Vector3fc botRight, Vector2fc texTopLeft, Vector2fc texBotRight)
	{
		int i = this.getVertexCount();

		this.addVertex(botLeft.x(), botLeft.y(), botLeft.z(), texBotRight.x(), texTopLeft.y());
		this.addVertex(botRight.x(), botRight.y(), botRight.z(), texTopLeft.x(), texTopLeft.y());
		this.addVertex(topLeft.x(), topLeft.y(), topLeft.z(), texBotRight.x(), texBotRight.y());
		this.addVertex(botRight.x(), botRight.y(), botRight.z(), texTopLeft.x(), texTopLeft.y());
		this.addVertex(topRight.x(), topRight.y(), topRight.z(), texTopLeft.x(), texBotRight.y());
		this.addVertex(topLeft.x(), topLeft.y(), topLeft.z(), texBotRight.x(), texBotRight.y());

		this.addVertexIndex(i++, i++, i++);
		this.addVertexIndex(i++, i++, i++);
	}

	public void addFaceVertical(Vector3fc bottomLeft, Vector3fc topRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(0F, 1F));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), bottomLeft.z()), new Vector2f(0F, 0F));
		this.addVertex(topRight, new Vector2f(1F, 0F));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), topRight.z()), new Vector2f(1F, 1F));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceVertical(Vector3fc bottomLeft, Vector3fc topRight, Vector2fc texCoordTopLeft, Vector2fc texCoordBottomRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(texCoordTopLeft.x(), texCoordBottomRight.y()));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), bottomLeft.z()), texCoordTopLeft);
		this.addVertex(topRight, new Vector2f(texCoordBottomRight.x(), texCoordTopLeft.y()));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), topRight.z()), texCoordBottomRight);

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceHorizontal(Vector3f bottomLeft, Vector3f topRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, new Vector2f(0F, 0F));
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), bottomLeft.z()), new Vector2f(1F, 0F));
		this.addVertex(topRight, new Vector2f(1F, 1F));
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), topRight.z()), new Vector2f(0F, 1F));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}

	public void addFaceHorizontal(Vector3fc bottomLeft, Vector3fc topRight, Vector2fc texCoordTopLeft, Vector2fc texCoordBottomRight)
	{
		int i = this.vertices.size();

		this.addVertex(bottomLeft, texCoordTopLeft);
		this.addVertex(new Vector3f(topRight.x(), bottomLeft.y(), bottomLeft.z()), new Vector2f(texCoordBottomRight.x(), texCoordTopLeft.y()));
		this.addVertex(topRight, texCoordBottomRight);
		this.addVertex(new Vector3f(bottomLeft.x(), topRight.y(), topRight.z()), new Vector2f(texCoordTopLeft.x(), texCoordBottomRight.y()));

		this.addVertexIndex(i, i + 1, i + 2);
		this.addVertexIndex(i, i + 2, i + 3);
	}
}
