package org.zilar.meshbuilder;

import org.bstone.transform.Transform;
import org.joml.Vector2fc;
import org.joml.Vector3fc;

/**
 * Created by Andy on 6/11/17.
 */
public class BoxMeshBuilder extends MeshBuilderBase
{
	public void addBox(Vector3fc from, Vector3fc to,
	                   Vector2fc texTopLeft, Vector2fc texBotRight,
	                   boolean bottom,
	                   boolean top,
	                   boolean front,
	                   boolean back,
	                   boolean left,
	                   boolean right)
	{
		this.addTexturedBox(from, to,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				texTopLeft, texBotRight,
				bottom, top, front, back, left, right, false);
	}

	public void addTexturedBox(Vector3fc from, Vector3fc to,
	                           Vector2fc texFrontFrom, Vector2fc texFrontTo,
	                           Vector2fc texBackFrom, Vector2fc texBackTo,
	                           Vector2fc texTopFrom, Vector2fc texTopTo,
	                           Vector2fc texBotFrom, Vector2fc texBotTo,
	                           Vector2fc texLeftFrom, Vector2fc texLeftTo,
	                           Vector2fc texRightFrom, Vector2fc texRightTo,
	                           boolean bottom,
	                           boolean top,
	                           boolean front,
	                           boolean back,
	                           boolean left,
	                           boolean right,
	                           boolean mirrorSides)
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
			if (mirrorSides)
			{
				this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftFrom.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftTo.y(), -normal, 0, 0);
				this.addVertex(from.x(), from.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);

				this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftFrom.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftTo.y(), -normal, 0, 0);
			}
			else
			{
				this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
				this.addVertex(from.x(), from.y(), from.z(), texLeftFrom.x(), texLeftFrom.y(), -normal, 0, 0);

				this.addVertex(from.x(), from.y(), to.z(), texLeftFrom.x(), texLeftTo.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), to.z(), texLeftTo.x(), texLeftTo.y(), -normal, 0, 0);
				this.addVertex(from.x(), to.y(), from.z(), texLeftTo.x(), texLeftFrom.y(), -normal, 0, 0);
			}
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}

		//Right
		if (right)
		{
			if (mirrorSides)
			{
				this.addVertex(to.x(), from.y(), to.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
				this.addVertex(to.x(), from.y(), from.z(), texRightTo.x(), texRightFrom.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), from.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);

				this.addVertex(to.x(), from.y(), to.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), from.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), to.z(), texRightFrom.x(), texRightTo.y(), normal, 0, 0);
			}
			else
			{
				this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
				this.addVertex(to.x(), from.y(), from.z(), texRightTo.x(), texRightFrom.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);

				this.addVertex(to.x(), from.y(), to.z(), texRightTo.x(), texRightTo.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), from.z(), texRightFrom.x(), texRightFrom.y(), normal, 0, 0);
				this.addVertex(to.x(), to.y(), to.z(), texRightFrom.x(), texRightTo.y(), normal, 0, 0);
			}
			this.addVertexIndex(i++, i++, i++);
			this.addVertexIndex(i++, i++, i++);
		}
	}

	public void addQuad(float x, float y, float width, float height)
	{
		int index = this.getVertexCount();
		float w2 = width / 2F;
		float h2 = height / 2F;
		this.addVertex(x - w2, y - h2, 0, 0, 0, 0, 0, 1);
		this.indices.add(index++);
		this.addVertex(x + w2, y - h2, 0, 0, 0, 0, 0, 1);
		this.indices.add(index++);
		this.addVertex(x + w2, y + h2, 0, 0, 0, 0, 0, 1);
		this.indices.add(index++);
		this.addVertex(x - w2, y + h2, 0, 0, 0, 0, 0, 1);
		this.indices.add(index);
	}

	public void addCircle(float x, float y, float radius, int lines)
	{
		int index = this.getVertexCount();
		int i;

		for (i = 0; i < lines; i++)
		{
			this.addVertex(x + (float) (radius * Math.cos(i * Transform.PI2 / lines)), y + (float) (radius * Math.sin(i * Transform.PI2 / lines)), 0, 0, 0, 0, 0, 1);
			this.indices.add(index + i);
		}
	}

	public void addPoint(float x, float y)
	{
		int index = this.getVertexCount();
		this.addVertex(x, y, 0, 0, 0, 0, 0, 1);
		this.indices.add(index);
	}

	public void addSegment(float x, float y, float dx, float dy)
	{
		int index = this.getVertexCount();
		this.addVertex(x, y, 0, 0, 0, 0, 0, 1);
		this.indices.add(index++);
		this.addVertex(x + dx, y + dy, 0, 0, 0, 0, 0, 1);
		this.indices.add(index);
	}
}
