package net.jimboi.mod.meshbuilder;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Created by Andy on 5/10/17.
 */
public class MeshBuilder extends MeshBuilderBase
{
	public MeshBuilder()
	{
	}

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

	public void addBox(Vector3fc from, Vector3fc to, Vector2fc texTopLeft, Vector2fc texBotRight, boolean bottom, boolean top, boolean front, boolean back, boolean left, boolean right)
	{
		this.addTexturedBox(from, to,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				bottom, top, front, back, left, right);
	}

	public void addTexturedBox(Vector3fc from, Vector3fc to, Vector2fc texFrontFrom, Vector2fc texFrontTo, Vector2fc texBackFrom, Vector2fc texBackTo, Vector2fc texTopFrom, Vector2fc texTopTo, Vector2fc texBotFrom, Vector2fc texBotTo, Vector2fc texLeftFrom, Vector2fc texLeftTo, Vector2fc texRightFrom, Vector2fc texRightTo, boolean bottom, boolean top, boolean front, boolean back, boolean left, boolean right)
	{
		int i = this.getVertexCount();

		final int normal = 1;

		//Bottom
		if (bottom)
		{
			this.addVertex(from.x(), from.y(), from.z(), texBotFrom.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), from.z(), texBotTo.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(from.x(), from.y(), to.z(), texBotFrom.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), from.z(), texBotTo.x(), texBotFrom.y(), 0, -normal, 0);
			this.addVertex(to.x(), from.y(), to.z(), texBotTo.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertex(from.x(), from.y(), to.z(), texBotFrom.x(), texBotTo.y(), 0, -normal, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Top
		if (top)
		{
			this.addVertex(from.x(), to.y(), from.z(), texTopFrom.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(from.x(), to.y(), to.z(), texTopFrom.x(), texTopTo.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), from.z(), texTopTo.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), from.z(), texTopTo.x(), texTopFrom.y(), 0, normal, 0);
			this.addVertex(from.x(), to.y(), to.z(), texTopFrom.x(), texTopTo.y(), 0, normal, 0);
			this.addVertex(to.x(), to.y(), to.z(), texTopTo.x(), texTopTo.y(), 0, normal, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Front
		if (front)
		{
			this.addVertex(from.x(), from.y(), to.z(), texFrontTo.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(to.x(), from.y(), to.z(), texFrontFrom.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(from.x(), to.y(), to.z(), texFrontTo.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertex(to.x(), from.y(), to.z(), texFrontFrom.x(), texFrontFrom.y(), 0, 0, normal);
			this.addVertex(to.x(), to.y(), to.z(), texFrontFrom.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertex(from.x(), to.y(), to.z(), texFrontTo.x(), texFrontTo.y(), 0, 0, normal);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Back
		if (back)
		{
			this.addVertex(from.x(), from.y(), from.z(), texBackFrom.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(from.x(), to.y(), from.z(), texBackFrom.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertex(to.x(), from.y(), from.z(), texBackTo.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(to.x(), from.y(), from.z(), texBackTo.x(), texBackFrom.y(), 0, 0, -normal);
			this.addVertex(from.x(), to.y(), from.z(), texBackFrom.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertex(to.x(), to.y(), from.z(), texBackTo.x(), texBackTo.y(), 0, 0, -normal);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Left
		if (left)
		{
			this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertex(from.x(), from.y(), from.z(), texLeftFrom.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), to.z(), texLeftTo.x(), texLeftTo.y(), -normal, 0, 0);
			this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Right
		if (right)
		{
			this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
			this.addVertex(to.x(), from.y(), from.z(), texRightTo.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
			this.addVertex(to.x(), to.y(), to.z(), texRightFrom.x(), texRightTo.y(), normal, 0, 0);
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}
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
