package net.jimboi.stage_b.glim.bounding;

import org.joml.Vector3fc;

public interface Bounding
{
	void update(float x, float y, float z);

	void offset(float x, float y, float z);

	Vector3fc position();
}
