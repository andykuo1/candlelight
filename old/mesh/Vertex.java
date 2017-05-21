package net.jimboi.mesh;

import org.joml.Vector3fc;

/**
 * Created by Andy on 4/28/17.
 */
public class Vertex
{
	public final Vector3fc position;
	public final Vector3fc normal;

	public final float[][] attributes;

	public Vertex(Vector3fc position, Vector3fc normal, float[]... attributes)
	{
		this.position = position;
		this.normal = normal;

		this.attributes = attributes;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Vertex)
		{
			if (this == o) return true;

			Vertex other = (Vertex) o;

			if (this.position.equals(other.position) &&
					this.normal.equals(other.normal) &&
					this.attributes.length == other.attributes.length)
			{
				for(int i = 0; i < this.attributes.length; ++i)
				{
					float[] value = this.attributes[i];
					float[] otherValue = other.attributes[i];
					if (value.length != otherValue.length) return false;

					for(int j = 0; j < value.length; ++j)
					{
						if (Float.compare(value[j], otherValue[j]) != 0)
						{
							return false;
						}
					}
				}

				return true;
			}
		}

		return false;
	}
}
