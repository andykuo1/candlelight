package net.jimboi.test.multiuser;

import java.nio.ByteBuffer;

/**
 * Created by Andy on 8/14/17.
 */
public class LoopBackTimeStamp
{
	public long sendTimeStamp;
	public long receiveTimeStamp;

	public LoopBackTimeStamp()
	{
		this.sendTimeStamp = System.nanoTime();
	}

	public long getElapsedTimeInNanoseconds()
	{
		return this.receiveTimeStamp - this.sendTimeStamp;
	}

	public byte[] toByteArray()
	{
		final int bytesOfLong = Long.SIZE / Byte.SIZE;

		byte[] result = new byte[bytesOfLong * 2];
		byte[] time1 = ByteBuffer.allocate(bytesOfLong).putLong(this.sendTimeStamp).array();
		byte[] time2 = ByteBuffer.allocate(bytesOfLong).putLong(this.receiveTimeStamp).array();

		for(int i = 0; i < bytesOfLong; ++i)
		{
			result[i] = time1[i];
			result[i + bytesOfLong] = time2[i];
		}

		return result;
	}

	public void fromByteArray(byte[] data)
	{
		final int bytesOfLong = Long.SIZE / Byte.SIZE;
		final int length = data.length;

		if (length != bytesOfLong * 2)
		{
			System.out.println("ERROR: Invalid content length!");
			return;
		}

		ByteBuffer buffer1 = ByteBuffer.allocate(bytesOfLong).put(data, 0, bytesOfLong);
		ByteBuffer buffer2 = ByteBuffer.allocate(bytesOfLong).put(data, bytesOfLong, bytesOfLong);

		buffer1.rewind();
		buffer2.rewind();
		this.sendTimeStamp = buffer1.getLong();
		this.receiveTimeStamp = buffer2.getLong();
	}
}
