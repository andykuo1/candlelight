package apricot.bstone.util.math;

import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Andy on 5/18/17.
 */
public class Matrix2f implements Matrix2fc
{
	float m00, m01;
	float m10, m11;

	/**
	 * Create a new {@link Matrix2f} and set it to {@link #identity() identity}.
	 */
	public Matrix2f() {
		m00 = 1.0f;
		m11 = 1.0f;
	}

	/**
	 * Create a new {@link Matrix2f} and make it a copy of the given matrix.
	 *
	 * @param mat
	 *          the {@link Matrix4fc} to copy the values from
	 */
	public Matrix2f(Matrix2fc mat) {
		if (mat instanceof Matrix2f) {
			Matrix2f m = (Matrix2f) mat;
			m00 = m.m00;
			m01 = m.m01;
			m10 = m.m10;
			m11 = m.m11;
		} else {
			m00 = mat.m00();
			m01 = mat.m01();
			m10 = mat.m10();
			m11 = mat.m11();
		}
	}

	@Override
	public float m00()
	{
		return m00;
	}

	@Override
	public float m01()
	{
		return m01;
	}

	@Override
	public float m10()
	{
		return m10;
	}

	@Override
	public float m11()
	{
		return m11;
	}

	/**
	 * Set the value of the matrix element at column 0 and row 0
	 *
	 * @param m00
	 *          the new value
	 * @return the value of the matrix element
	 */
	public Matrix2f m00(float m00) {
		this.m00 = m00;
		return this;
	}
	/**
	 * Set the value of the matrix element at column 0 and row 1
	 *
	 * @param m01
	 *          the new value
	 * @return the value of the matrix element
	 */
	public Matrix2f m01(float m01) {
		this.m01 = m01;
		return this;
	}
	/**
	 * Set the value of the matrix element at column 1 and row 0
	 *
	 * @param m10
	 *          the new value
	 * @return the value of the matrix element
	 */
	public Matrix2f m10(float m10) {
		this.m10 = m10;
		return this;
	}
	/**
	 * Set the value of the matrix element at column 1 and row 1
	 *
	 * @param m11
	 *          the new value
	 * @return the value of the matrix element
	 */
	public Matrix2f m11(float m11) {
		this.m11 = m11;
		return this;
	}



	/**
	 * Reset this matrix to the identity.
	 *
	 * @return this
	 */
	public Matrix2f identity() {
		m00 = 1;
		m01 = 0;
		m10 = 0;
		m11 = 1;
		return this;
	}

	/**
	 * Set this matrix to the radians matrix of given radians.
	 *
	 * @return this
	 */
	public Matrix2f rotation(float radians)
	{
		float c = (float) Math.cos(radians);
		float s = (float) Math.sin(radians);

		m00 = c;
		m01 = -s;
		m10 = s;
		m11 = c;

		return this;
	}

	/**
	 * Store the values of the given matrix <code>m</code> into <code>this</code> matrix.
	 *
	 * @see #Matrix2f(Matrix2fc)
	 * @see #get(Matrix2f)
	 *
	 * @param m
	 *          the matrix to copy the values from
	 * @return this
	 */
	public Matrix2f set(Matrix2fc m) {
		if (m instanceof Matrix2f) {
			Matrix2f mat = (Matrix2f) m;
			m00 = mat.m00;
			m01 = mat.m01;
			m10 = mat.m10;
			m11 = mat.m11;
		} else {
			m00 = m.m00();
			m01 = m.m01();
			m10 = m.m10();
			m11 = m.m11();
		}
		return this;
	}

	private Matrix2f _set(float m00, float m01, float m10, float m11) {
		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;
		return this;
	}

	@Override
	public Matrix2f mul(Matrix2fc right, Matrix2f dest)
	{
		dest._set(m00 * right.m00() + m01 * right.m10(),
		m00 * right.m01() + m01 * right.m11(),
		m10 * right.m00() + m11 * right.m10(),
		m10 * right.m01() + m11 * right.m11());
		return dest;
	}

	@Override
	public Matrix2f add(Matrix2fc other, Matrix2f dest)
	{
		dest.m00 = m00 + other.m00();
		dest.m01 = m01 + other.m01();
		dest.m10 = m10 + other.m10();
		dest.m11 = m11 + other.m11();
		return dest;
	}

	@Override
	public Matrix2f sub(Matrix2fc subtrahend, Matrix2f dest)
	{
		dest.m00 = m00 - subtrahend.m00();
		dest.m01 = m01 - subtrahend.m01();
		dest.m10 = m10 - subtrahend.m10();
		dest.m11 = m11 - subtrahend.m11();
		return dest;
	}

	@Override
	public Matrix2f abs(Matrix2f dest)
	{
		dest.m00 = Math.abs(m00);
		dest.m01 = Math.abs(m01);
		dest.m10 = Math.abs(m10);
		dest.m11 = Math.abs(m11);
		return dest;
	}

	@Override
	public Matrix2f invert(Matrix2f dest)
	{
		float det = m00 * m11 - m01 * m10;
		_set(m11 / det, -m01 / det, -m10 / det, m00 / det);
		return dest;
	}

	@Override
	public Matrix2f transpose(Matrix2f dest)
	{
		dest._set(m00, m10, m01, m11);
		return dest;
	}

	@Override
	public Matrix2f get(Matrix2f dest)
	{
		dest._set(m00, m01, m10, m11);
		return dest;
	}

	@Override
	public FloatBuffer get(FloatBuffer buffer)
	{
		return get(buffer.position(), buffer);
	}

	@Override
	public FloatBuffer get(int index, FloatBuffer buffer)
	{
		if (index == 0)
		{
			buffer.put(0, m00);
			buffer.put(1, m01);
			buffer.put(2, m10);
			buffer.put(3, m11);
		}
		else
		{
			buffer.put(index + 0, m00);
			buffer.put(index + 1, m01);
			buffer.put(index + 2, m10);
			buffer.put(index + 3, m11);
		}
		return buffer;
	}

	@Override
	public ByteBuffer get(ByteBuffer buffer)
	{
		return get(buffer.position(), buffer);
	}

	@Override
	public ByteBuffer get(int index, ByteBuffer buffer)
	{
		if (index == 0)
		{
			buffer.putFloat(0, m00);
			buffer.putFloat(4, m01);
			buffer.putFloat(8, m10);
			buffer.putFloat(12, m11);
		}
		else
		{
			buffer.putFloat(index + 0, m00);
			buffer.putFloat(index + 4, m01);
			buffer.putFloat(index + 8, m10);
			buffer.putFloat(index + 12, m11);
		}
		return buffer;
	}

	@Override
	public FloatBuffer getTransposed(FloatBuffer buffer)
	{
		return getTransposed(buffer.position(), buffer);
	}

	@Override
	public FloatBuffer getTransposed(int index, FloatBuffer buffer)
	{
		if (index == 0)
		{
			buffer.put(0, m00);
			buffer.put(1, m10);
			buffer.put(2, m01);
			buffer.put(3, m11);
		}
		else
		{
			buffer.put(index + 0, m00);
			buffer.put(index + 1, m10);
			buffer.put(index + 2, m01);
			buffer.put(index + 3, m11);
		}
		return buffer;
	}

	@Override
	public ByteBuffer getTransposed(ByteBuffer buffer)
	{
		return getTransposed(buffer.position(), buffer);
	}

	@Override
	public ByteBuffer getTransposed(int index, ByteBuffer buffer)
	{
		if (index == 0)
		{
			buffer.putFloat(0, m00);
			buffer.putFloat(4, m10);
			buffer.putFloat(8, m01);
			buffer.putFloat(12, m11);
		}
		else
		{
			buffer.putFloat(index + 0, m00);
			buffer.putFloat(index + 4, m10);
			buffer.putFloat(index + 8, m01);
			buffer.putFloat(index + 12, m11);
		}
		return buffer;
	}

	@Override
	public float[] get(float[] arr, int offset)
	{
		if (offset == 0)
		{
			arr[0] = m00;
			arr[1] = m10;
			arr[2] = m01;
			arr[3] = m11;
		}
		else
		{
			arr[offset] = m00;
			arr[offset + 1] = m10;
			arr[offset + 2] = m01;
			arr[offset + 3] = m11;
		}
		return arr;
	}

	@Override
	public float[] get(float[] arr)
	{
		return get(arr, 0);
	}

	@Override
	public Vector2f transform(Vector2f v)
	{
		return v.set(m00 * v.x + m01 * v.y, m10 * v.x + m11 * v.y);
	}

	@Override
	public Vector2f transform(Vector2fc v, Vector2f dest)
	{
		return dest.set(m00 * v.x() + m01 * v.y(), m10 * v.x() + m11 * v.y());
	}

	@Override
	public Vector2f transform(float x, float y, Vector2f dest)
	{
		return dest.set(m00 * x + m01 * y, m10 * x + m11 * y);
	}

	@Override
	public Vector2f getRow(int row, Vector2f dest) throws IndexOutOfBoundsException
	{
		switch(row)
		{
			case 0:
				return dest.set(m00, m01);
			case 1:
				return dest.set(m10, m11);
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Vector2f getColumn(int column, Vector2f dest) throws IndexOutOfBoundsException
	{
		switch (column)
		{
			case 0:
				return dest.set(m00, m10);
			case 1:
				return dest.set(m01, m11);
			default:
				throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public Vector2f getAxisX(Vector2f dest)
	{
		dest.x = m00;
		dest.y = m10;
		return dest;
	}

	@Override
	public Vector2f getAxisY(Vector2f dest)
	{
		dest.x = m01;
		dest.y = m11;
		return dest;
	}
}
