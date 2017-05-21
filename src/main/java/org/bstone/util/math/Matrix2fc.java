package org.bstone.util.math;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Andy on 5/18/17.
 */
public interface Matrix2fc
{
	/**
	 * Return the value of the matrix element at column 0 and row 0.
	 *
	 * @return the value of the matrix element
	 */
	float m00();

	/**
	 * Return the value of the matrix element at column 0 and row 1.
	 *
	 * @return the value of the matrix element
	 */
	float m01();

	/**
	 * Return the value of the matrix element at column 1 and row 0.
	 *
	 * @return the value of the matrix element
	 */
	float m10();

	/**
	 * Return the value of the matrix element at column 1 and row 1.
	 *
	 * @return the value of the matrix element
	 */
	float m11();

	/**
	 * Multiply this matrix by the supplied <code>right</code> matrix and store the result in <code>dest</code>.
	 * <p>
	 * If <code>M</code> is <code>this</code> matrix and <code>R</code> the <code>right</code> matrix,
	 * then the new matrix will be <code>M * R</code>. So when transforming a
	 * vector <code>v</code> with the new matrix by using <code>M * R * v</code>, the
	 * transformation of the right matrix will be applied first!
	 *
	 * @param right
	 *          the right operand of the matrix multiplication
	 * @param dest
	 *          the destination matrix, which will hold the result
	 * @return dest
	 */
	Matrix2f mul(Matrix2fc right, Matrix2f dest);

	/**
	 * Component-wise add <code>this</code> and <code>other</code> and store the result in <code>dest</code>.
	 *
	 * @param other
	 *          the other addend
	 * @param dest
	 *          will hold the result
	 * @return dest
	 */
	Matrix2f add(Matrix2fc other, Matrix2f dest);

	/**
	 * Component-wise subtract <code>subtrahend</code> from <code>this</code> and store the result in <code>dest</code>.
	 *
	 * @param subtrahend
	 *          the subtrahend
	 * @param dest
	 *          will hold the result
	 * @return dest
	 */
	Matrix2f sub(Matrix2fc subtrahend, Matrix2f dest);

	Matrix2f abs(Matrix2f dest);

	/**
	 * Invert this matrix and write the result into <code>dest</code>.
	 *
	 * @param dest
	 *          will hold the result
	 * @return dest
	 */
	Matrix2f invert(Matrix2f dest);

	/**
	 * Transpose this matrix and store the result in <code>dest</code>.
	 *
	 * @param dest
	 *             will hold the result
	 * @return dest
	 */
	Matrix2f transpose(Matrix2f dest);

	/**
	 * Get the current values of <code>this</code> matrix and store them into
	 * <code>dest</code>.
	 *
	 * @param dest
	 *            the destination matrix
	 * @return the passed in destination
	 */
	Matrix2f get(Matrix2f dest);


	/**
	 * Store this matrix in column-major order into the supplied {@link FloatBuffer} at the current
	 * buffer {@link FloatBuffer#position() position}.
	 * <p>
	 * This method will not increment the position of the given FloatBuffer.
	 * <p>
	 * In order to specify the offset into the FloatBuffer at which
	 * the matrix is stored, use {@link #get(int, FloatBuffer)}, taking
	 * the absolute position as parameter.
	 *
	 * @see #get(int, FloatBuffer)
	 *
	 * @param buffer
	 *            will receive the values of this matrix in column-major order at its current position
	 * @return the passed in buffer
	 */
	FloatBuffer get(FloatBuffer buffer);

	/**
	 * Store this matrix in column-major order into the supplied {@link FloatBuffer} starting at the specified
	 * absolute buffer position/index.
	 * <p>
	 * This method will not increment the position of the given FloatBuffer.
	 *
	 * @param index
	 *            the absolute position into the FloatBuffer
	 * @param buffer
	 *            will receive the values of this matrix in column-major order
	 * @return the passed in buffer
	 */
	FloatBuffer get(int index, FloatBuffer buffer);

	/**
	 * Store this matrix in column-major order into the supplied {@link ByteBuffer} at the current
	 * buffer {@link ByteBuffer#position() position}.
	 * <p>
	 * This method will not increment the position of the given ByteBuffer.
	 * <p>
	 * In order to specify the offset into the ByteBuffer at which
	 * the matrix is stored, use {@link #get(int, ByteBuffer)}, taking
	 * the absolute position as parameter.
	 *
	 * @see #get(int, ByteBuffer)
	 *
	 * @param buffer
	 *            will receive the values of this matrix in column-major order at its current position
	 * @return the passed in buffer
	 */
	ByteBuffer get(ByteBuffer buffer);

	/**
	 * Store this matrix in column-major order into the supplied {@link ByteBuffer} starting at the specified
	 * absolute buffer position/index.
	 * <p>
	 * This method will not increment the position of the given ByteBuffer.
	 *
	 * @param index
	 *            the absolute position into the ByteBuffer
	 * @param buffer
	 *            will receive the values of this matrix in column-major order
	 * @return the passed in buffer
	 */
	ByteBuffer get(int index, ByteBuffer buffer);

	/**
	 * Store the transpose of this matrix in column-major order into the supplied {@link FloatBuffer} at the current
	 * buffer {@link FloatBuffer#position() position}.
	 * <p>
	 * This method will not increment the position of the given FloatBuffer.
	 * <p>
	 * In order to specify the offset into the FloatBuffer at which
	 * the matrix is stored, use {@link #getTransposed(int, FloatBuffer)}, taking
	 * the absolute position as parameter.
	 *
	 * @see #getTransposed(int, FloatBuffer)
	 *
	 * @param buffer
	 *            will receive the values of this matrix in column-major order at its current position
	 * @return the passed in buffer
	 */
	FloatBuffer getTransposed(FloatBuffer buffer);

	/**
	 * Store the transpose of this matrix in column-major order into the supplied {@link FloatBuffer} starting at the specified
	 * absolute buffer position/index.
	 * <p>
	 * This method will not increment the position of the given FloatBuffer.
	 *
	 * @param index
	 *            the absolute position into the FloatBuffer
	 * @param buffer
	 *            will receive the values of this matrix in column-major order
	 * @return the passed in buffer
	 */
	FloatBuffer getTransposed(int index, FloatBuffer buffer);

	/**
	 * Store the transpose of this matrix in column-major order into the supplied {@link ByteBuffer} at the current
	 * buffer {@link ByteBuffer#position() position}.
	 * <p>
	 * This method will not increment the position of the given ByteBuffer.
	 * <p>
	 * In order to specify the offset into the ByteBuffer at which
	 * the matrix is stored, use {@link #getTransposed(int, ByteBuffer)}, taking
	 * the absolute position as parameter.
	 *
	 * @see #getTransposed(int, ByteBuffer)
	 *
	 * @param buffer
	 *            will receive the values of this matrix in column-major order at its current position
	 * @return the passed in buffer
	 */
	ByteBuffer getTransposed(ByteBuffer buffer);

	/**
	 * Store the transpose of this matrix in column-major order into the supplied {@link ByteBuffer} starting at the specified
	 * absolute buffer position/index.
	 * <p>
	 * This method will not increment the position of the given ByteBuffer.
	 *
	 * @param index
	 *            the absolute position into the ByteBuffer
	 * @param buffer
	 *            will receive the values of this matrix in column-major order
	 * @return the passed in buffer
	 */
	ByteBuffer getTransposed(int index, ByteBuffer buffer);

	/**
	 * Store this matrix into the supplied float array in column-major order at the given offset.
	 *
	 * @param arr
	 *          the array to write the matrix values into
	 * @param offset
	 *          the offset into the array
	 * @return the passed in array
	 */
	float[] get(float[] arr, int offset);

	/**
	 * Store this matrix into the supplied float array in column-major order.
	 * <p>
	 * In order to specify an explicit offset into the array, use the method {@link #get(float[], int)}.
	 *
	 * @see #get(float[], int)
	 *
	 * @param arr
	 *          the array to write the matrix values into
	 * @return the passed in array
	 */
	float[] get(float[] arr);

	/**
	 * Transform/multiply the given vector by this matrix and store the result in that vector.
	 * <p>
	 * Represents Vector2f.mul(Matrix2fc).
	 *
	 * @param v
	 *          the vector to transform and to hold the final result
	 * @return v
	 */
	Vector2f transform(Vector2f v);

	/**
	 * Transform/multiply the given vector by this matrix and store the result in <code>dest</code>.
	 * <p>
	 * Represents Vector2f.mul(Matrix2fc, Vector2f)
	 *
	 * @param v
	 *          the vector to transform
	 * @param dest
	 *          will contain the result
	 * @return dest
	 */
	Vector2f transform(Vector2fc v, Vector2f dest);

	/**
	 * Transform/multiply the vector <tt>(x, y)</tt> by this matrix and store the result in <code>dest</code>.
	 *
	 * @param x
	 *          the x coordinate of the vector to transform
	 * @param y
	 *          the y coordinate of the vector to transform
	 * @param dest
	 *          will contain the result
	 * @return dest
	 */
	Vector2f transform(float x, float y, Vector2f dest);

	/**
	 * Get the row at the given <code>row</code> index, starting with <code>0</code>.
	 *
	 * @param row
	 *          the row index in <tt>[0..3]</tt>
	 * @param dest
	 *          will hold the row components
	 * @return the passed in destination
	 * @throws IndexOutOfBoundsException if <code>row</code> is not in <tt>[0..3]</tt>
	 */
	Vector2f getRow(int row, Vector2f dest) throws IndexOutOfBoundsException;

	/**
	 * Get the column at the given <code>column</code> index, starting with <code>0</code>.
	 *
	 * @param column
	 *          the column index in <tt>[0..3]</tt>
	 * @param dest
	 *          will hold the column components
	 * @return the passed in destination
	 * @throws IndexOutOfBoundsException if <code>column</code> is not in <tt>[0..3]</tt>
	 */
	Vector2f getColumn(int column, Vector2f dest) throws IndexOutOfBoundsException;

	Vector2f getAxisX(Vector2f dst);
	Vector2f getAxisY(Vector2f dst);
}
