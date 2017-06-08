package net.jimboi.blob.torchlite;

import java.util.Collection;
import java.util.Iterator;

public final class Vec2f extends Vector
{
	/******INIT VARIABLES******/

	public float x;
	public float y;

	/******CONSTRUCTORS******/

	public Vec2f(final float x, final float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vec2f(final float xyz)
	{
		this.x = this.y = xyz;
	}

	public Vec2f()
	{
		this(0);
	}

	/******MUTABLE ARITHMETIC******/

	public Vec2f _add(final Vec2f vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vec2f _add(final float x, final float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}

	@Override
	public Vec2f _add(final float f)
	{
		this.x += f;
		this.y += f;
		return this;
	}

	@Override
	public Vec2f _addi(final int i)
	{
		this.x += i;
		this.y += i;
		return this;
	}

	public Vec2f _addX(final float x)
	{
		this.x += x;
		return this;
	}

	public Vec2f _addY(final float y)
	{
		this.y += y;
		return this;
	}

	@Override
	public Vec2f _add(final int index, final float f)
	{
		if (index == 0) this.x += f;
		else if (index == 1) this.y += f;
		return this;
	}

	@Override
	public Vec2f _addi(final int index, final int i)
	{
		if (index == 0) this.x += i;
		else if (index == 1) this.y += i;
		return this;
	}

	public Vec2f _sub(final Vec2f vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}

	public Vec2f _sub(final float x, final float y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}

	@Override
	public Vec2f _sub(final float f)
	{
		this.x -= f;
		this.y -= f;
		return this;
	}

	@Override
	public Vec2f _subi(final int i)
	{
		this.x -= i;
		this.y -= i;
		return this;
	}

	public Vec2f _subX(final float x)
	{
		this.x -= x;
		return this;
	}

	public Vec2f _subY(final float y)
	{
		this.y -= y;
		return this;
	}

	@Override
	public Vec2f _sub(final int index, final float f)
	{
		if (index == 0) this.x -= f;
		else if (index == 1) this.y -= f;
		return this;
	}

	@Override
	public Vec2f _subi(final int index, final int i)
	{
		if (index == 0) this.x -= i;
		else if (index == 1) this.y -= i;
		return this;
	}

	public Vec2f _mul(final float x, final float y)
	{
		this.x *= x;
		this.y *= y;
		return this;
	}

	@Override
	public Vec2f _mul(final float f)
	{
		this.x *= f;
		this.y *= f;
		return this;
	}

	@Override
	public Vec2f _muli(final int i)
	{
		this.x *= i;
		this.y *= i;
		return this;
	}

	public Vec2f _mulX(final float x)
	{
		this.x *= x;
		return this;
	}

	public Vec2f _mulY(final float y)
	{
		this.y *= y;
		return this;
	}

	@Override
	public Vec2f _mul(final int index, final float f)
	{
		if (index == 0) this.x *= f;
		else if (index == 1) this.y *= f;
		return this;
	}

	@Override
	public Vec2f _muli(final int index, final int i)
	{
		if (index == 0) this.x *= i;
		else if (index == 1) this.y *= i;
		return this;
	}

	public Vec2f _div(final float x, final float y)
	{
		this.x /= x;
		this.y /= y;
		return this;
	}

	@Override
	public Vec2f _div(final float f)
	{
		this.x /= f;
		this.y /= f;
		return this;
	}

	@Override
	public Vec2f _divi(final int i)
	{
		this.x /= i;
		this.y /= i;
		return this;
	}

	public Vec2f _divX(final float x)
	{
		this.x /= x;
		return this;
	}

	public Vec2f _divY(final float y)
	{
		this.y /= y;
		return this;
	}

	@Override
	public Vec2f _div(final int index, final float f)
	{
		if (index == 0) this.x /= f;
		else if (index == 1) this.y /= f;
		return this;
	}

	@Override
	public Vec2f _divi(final int index, final int i)
	{
		if (index == 0) this.x /= i;
		else if (index == 1) this.y /= i;
		return this;
	}

	public Vec2f _set(final Vec2f vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		return this;
	}

	public Vec2f _set(final float x, final float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Vec2f _set(final float f)
	{
		this.x = this.y = f;
		return this;
	}

	@Override
	public Vec2f _set(final int i)
	{
		this.x = this.y = i;
		return this;
	}

	public Vec2f _setX(final float x)
	{
		this.x = x;
		return this;
	}

	public Vec2f _setY(final float y)
	{
		this.y = y;
		return this;
	}

	@Override
	public Vec2f _setf(final int index, final float f)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		if (index == 0) this.x = f;
		if (index == 1) this.y = f;
		return this;
	}

	@Override
	public Vec2f _seti(final int index, final int i)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		if (index == 0) this.x = i;
		if (index == 1) this.y = i;
		return this;
	}

	/******MUTABLE MODIFIERS******/

	@Override
	public Vec2f _round()
	{
		this.x = this.x >= 0.5F ? (int)this.x + 1 : (int)this.x;
		this.y = this.y >= 0.5F ? (int)this.y + 1 : (int)this.y;
		return this;
	}

	@Override
	public Vec2f _floor()
	{
		this.x = this.x < 0 ? (int)(this.x - 1) : (int)this.x;
		this.y = this.y < 0 ? (int)(this.y - 1) : (int)this.y;
		return this;
	}

	@Override
	public Vec2f _ceil()
	{
		this.x = this.x < 0 ? (int)(this.x) : (int)(this.x + 1);
		this.y = this.y < 0 ? (int)(this.y) : (int)(this.y + 1);
		return this;
	}

	@Override
	public Vec2f _normalize()
	{
		final float length = this.length();
		if (length == 0) return this;

		this.x /= length;
		this.y /= length;
		return this;
	}

	@Override
	public Vec2f _abs()
	{
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		return this;
	}

	@Override
	public int _size()
	{
		return 2;
	}

	/******IMMUTABLE ARITHMETIC******/

	public Vec2f add(final Vec2f vec)
	{
		return new Vec2f(this.x + vec.x, this.y + vec.y);
	}

	public Vec2f add(final float x, final float y)
	{
		return new Vec2f(this.x + x, this.y + y);
	}

	@Override
	public Vec2f add(final float f)
	{
		return new Vec2f(this.x + f, this.y + f);
	}

	@Override
	public Vec2f addi(final int i)
	{
		return new Vec2f(this.x + i, this.y + i);
	}

	public Vec2f addX(final float x)
	{
		return new Vec2f(this.x + x, this.y);
	}

	public Vec2f addY(final float y)
	{
		return new Vec2f(this.x, this.y + y);
	}

	@Override
	public Vec2f add(final int index, final float f)
	{
		if (index == 0) return new Vec2f(this.x + f, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y + f);
		return new Vec2f(this.x, this.y);
	}

	@Override
	public Vec2f addi(final int index, final int i)
	{
		if (index == 0) return new Vec2f(this.x + i, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y + i);
		return new Vec2f(this.x, this.y);
	}

	public Vec2f sub(final Vec2f vec)
	{
		return new Vec2f(this.x - vec.x, this.y - vec.y);
	}

	public Vec2f sub(final float x, final float y)
	{
		return new Vec2f(this.x - x, this.y - y);
	}

	@Override
	public Vec2f sub(final float f)
	{
		return new Vec2f(this.x - f, this.y - f);
	}

	@Override
	public Vec2f subi(final int i)
	{
		return new Vec2f(this.x - i, this.y - i);
	}

	public Vec2f subX(final float x)
	{
		return new Vec2f(this.x - x, this.y);
	}

	public Vec2f subY(final float y)
	{
		return new Vec2f(this.x, this.y - y);
	}

	@Override
	public Vec2f sub(final int index, final float f)
	{
		if (index == 0) return new Vec2f(this.x - f, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y - f);
		return new Vec2f(this.x, this.y);
	}

	@Override
	public Vec2f subi(final int index, final int i)
	{
		if (index == 0) return new Vec2f(this.x - i, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y - i);
		return new Vec2f(this.x, this.y);
	}

	public Vec2f mul(final float x, final float y)
	{
		return new Vec2f(this.x * x, this.y * y);
	}

	@Override
	public Vec2f mul(final float f)
	{
		return new Vec2f(this.x * f, this.y * f);
	}

	@Override
	public Vec2f muli(final int i)
	{
		return new Vec2f(this.x * i, this.y * i);
	}

	public Vec2f mulX(final float x)
	{
		return new Vec2f(this.x * x, this.y);
	}

	public Vec2f mulY(final float y)
	{
		return new Vec2f(this.x, this.y * y);
	}

	@Override
	public Vec2f mul(final int index, final float f)
	{
		if (index == 0) return new Vec2f(this.x * f, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y * f);
		return new Vec2f(this.x, this.y);
	}

	@Override
	public Vec2f muli(final int index, final int i)
	{
		if (index == 0) return new Vec2f(this.x * i, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y * i);
		else if (index == 2) return new Vec2f(this.x, this.y);
		return new Vec2f(this.x, this.y);
	}

	public Vec2f div(final float x, final float y)
	{
		return new Vec2f(this.x / x, this.y / y);
	}

	@Override
	public Vec2f div(final float f)
	{
		return new Vec2f(this.x / f, this.y / f);
	}

	@Override
	public Vec2f divi(final int i)
	{
		return new Vec2f(this.x / i, this.y / i);
	}

	public Vec2f divX(final float x)
	{
		return new Vec2f(this.x / x, this.y);
	}

	public Vec2f divY(final float y)
	{
		return new Vec2f(this.x, this.y / y);
	}

	@Override
	public Vec2f div(final int index, final float f)
	{
		if (index == 0) return new Vec2f(this.x / f, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y / f);
		else if (index == 2) return new Vec2f(this.x, this.y);
		return new Vec2f(this.x, this.y);
	}

	@Override
	public Vec2f divi(final int index, final int i)
	{
		if (index == 0) return new Vec2f(this.x / i, this.y);
		else if (index == 1) return new Vec2f(this.x, this.y / i);
		else if (index == 2) return new Vec2f(this.x, this.y);
		return new Vec2f(this.x, this.y);
	}

	public Vec2f setX(final float x)
	{
		return new Vec2f(x, this.y);
	}

	public Vec2f setY(final float y)
	{
		return new Vec2f(this.x, y);
	}

	@Override
	public Vec2f setf(final int index, final float f)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return new Vec2f(index == 0 ? f : this.x, index == 1 ? f : this.y);
	}

	@Override
	public Vec2f seti(final int index, final int i)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return new Vec2f(index == 0 ? i : this.x, index == 1 ? i : this.y);
	}

	/******IMMUTABLE MODIFIERS******/

	@Override
	public Vec2f round()
	{
		return new Vec2f(this.x >= 0.5F ? (int)this.x + 1 : (int)this.x,
				this.y >= 0.5F ? (int)this.y + 1 : (int)this.y);
	}

	@Override
	public Vec2f floor()
	{
		return new Vec2f(this.x < 0 ? (int)(this.x - 1) : (int)this.x,
				this.y < 0 ? (int)(this.y - 1) : (int)this.y);
	}

	@Override
	public Vec2f ceil()
	{
		return new Vec2f(this.x > 0 ? (int)(this.x) : (int)(this.x + 1),
				this.y > 0 ? (int)(this.y) : (int)(this.y + 1));
	}

	@Override
	public Vec2f normalize()
	{
		final float length = this.length();
		if (length == 0) return new Vec2f(this.x, this.y);
		return new Vec2f(this.x / length, this.y / length);
	}

	@Override
	public Vec2f abs()
	{
		return new Vec2f(Math.abs(this.x), Math.abs(this.y));
	}

	/******IMMUTABLE ACCESSORS******/

	public float sum()
	{
		return this.x + this.y;
	}

	public float diff()
	{
		return this.x - this.y;
	}

	public float quot()
	{
		return this.x / this.y;
	}

	public float prod()
	{
		return this.x * this.y;
	}

	public float dot(final Vec2f vec)
	{
		return this.x * vec.x + this.y * vec.y;
	}

	public float max()
	{
		float max = this.x;
		if (max < this.y) max = this.y;
		return max;
	}

	public float min()
	{
		float min = this.x;
		if (min > this.y) min = this.y;
		return min;
	}

	@Override
	public float lengthSqu()
	{
		return this.x * this.x + this.y * this.y;
	}

	/******COMPONENTS******/

	@Override
	public float getf(final int index)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return index == 0 ? this.x : index == 1 ? this.y : 0;
	}

	@Override
	public int geti(final int index)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return index == 0 ? (int)this.x : index == 1 ? (int)this.y : 0;
	}

	@Override
	public float[] toFloatArray()
	{
		return new float[] {this.x, this.y};
	}

	@Override
	public int[] toIntArray()
	{
		return new int[] {(int)this.x, (int)this.y};
	}

	public Vec2i toVec2i()
	{
		return new Vec2i((int)this.x, (int)this.y);
	}

	/******EQUALITY******/

	public Vec2f copy()
	{
		return new Vec2f(this.x, this.y);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Vec2f)
		{
			final Vec2f vec = (Vec2f)o;
			return Float.compare(this.x, vec.x) == 0 && Float.compare(this.y, vec.y) == 0;
		}
		else if (o instanceof String)
		{
			return this.toString().equals(o);
		}

		return super.equals(o);
	}

	@Override
	public int hashCode()
	{
		return this.toString().hashCode();
	}

	@Override
	public String toString()
	{
		return "[" + this.x + "," + this.y + "]";
	}

	public static float angle(Vec2f vec, Vec2f other)
	{
		return (float) Math.acos(vec.dot(other) / (vec.length() * other.length()));
	}

	public static Vec2f polar(float dist, double degree)
	{
		return new Vec2f((float)(dist * Math.cos(Math.toRadians(degree))), (float)(dist * Math.sin(Math.toRadians(degree))));
	}

	public static float distSqu(Vec2f vec, Vec2f other)
	{
		return ((vec.x - other.x) * (vec.x - other.x)) + ((vec.y - other.y) * (vec.y - other.y)); 
	}

	public static float[] toFloatArray(Collection<Vec2f> collection)
	{
		float[] result = new float[collection.size() * 2];
		Iterator<Vec2f> iter = collection.iterator();

		int i = 0;
		while(iter.hasNext())
		{
			Vec2f vec = iter.next();
			result[(i * 2) + 0] = vec.x;
			result[(i * 2) + 1] = vec.y;

			i++;
		}

		return result;
	}

	public static float[] toFloatArray(Vec2f[] array)
	{
		float[] result = new float[array.length * 2];
		for(int i = 0; i < array.length; ++i)
		{
			result[(i * 2) + 0] = array[i].x;
			result[(i * 2) + 1] = array[i].y;
		}
		return result;
	}
}
