package org.bstone.util.dataformat.obj;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 6/11/17.
 */
final class FaceBuffer
{
	public List<Integer> geometry;
	public List<Integer> texture;
	public List<Integer> normal;

	public IntBuffer buffer;

	public FaceBuffer()
	{
		this.geometry = new ArrayList<>();
		this.texture = new ArrayList<>();
		this.normal = new ArrayList<>();
		this.buffer = IntBuffer.allocate(3);
	}

	public void pushBuffer()
	{
		this.buffer.flip();
		this.geometry.add(this.buffer.get());
		this.texture.add(this.buffer.get());
		this.normal.add(this.buffer.get());
		this.buffer.clear();
	}
}
