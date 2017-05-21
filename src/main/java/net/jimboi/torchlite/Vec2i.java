package net.jimboi.torchlite;

public final class Vec2i extends Vector
{
	/******INIT VARIABLES******/

	public int x;
	public int y;

	/******CONSTRUCTORS******/

	public Vec2i(final int x, final int y)
	{
		this.x = x;
		this.y = y;
	}

	public Vec2i(final int xyz)
	{
		this.x = this.y = xyz;
	}

	public Vec2i()
	{
		this(0);
	}

	/******MUTABLE ARITHMETIC******/

	public Vec2i _add(final Vec2i vec)
	{
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vec2i _add(final float x, final float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}

	@Override
	public Vec2i _add(final float f)
	{
		this.x += f;
		this.y += f;
		return this;
	}

	@Override
	public Vec2i _addi(final int i)
	{
		this.x += i;
		this.y += i;
		return this;
	}

	public Vec2i _addX(final float x)
	{
		this.x += x;
		return this;
	}

	public Vec2i _addY(final float y)
	{
		this.y += y;
		return this;
	}

	@Override
	public Vec2i _add(final int index, final float f)
	{
		if (index == 0) this.x += f;
		else if (index == 1) this.y += f;
		return this;
	}

	@Override
	public Vec2i _addi(final int index, final int i)
	{
		if (index == 0) this.x += i;
		else if (index == 1) this.y += i;
		return this;
	}

	public Vec2i _sub(final Vec2i vec)
	{
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}

	public Vec2i _sub(final float x, final float y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}

	@Override
	public Vec2i _sub(final float f)
	{
		this.x -= f;
		this.y -= f;
		return this;
	}

	@Override
	public Vec2i _subi(final int i)
	{
		this.x -= i;
		this.y -= i;
		return this;
	}

	public Vec2i _subX(final float x)
	{
		this.x -= x;
		return this;
	}

	public Vec2i _subY(final float y)
	{
		this.y -= y;
		return this;
	}

	@Override
	public Vec2i _sub(final int index, final float f)
	{
		if (index == 0) this.x -= f;
		else if (index == 1) this.y -= f;
		return this;
	}

	@Override
	public Vec2i _subi(final int index, final int i)
	{
		if (index == 0) this.x -= i;
		else if (index == 1) this.y -= i;
		return this;
	}

	public Vec2i _mul(final float x, final float y)
	{
		this.x *= x;
		this.y *= y;
		return this;
	}

	@Override
	public Vec2i _mul(final float f)
	{
		this.x *= f;
		this.y *= f;
		return this;
	}

	@Override
	public Vec2i _muli(final int i)
	{
		this.x *= i;
		this.y *= i;
		return this;
	}

	public Vec2i _mulX(final float x)
	{
		this.x *= x;
		return this;
	}

	public Vec2i _mulY(final float y)
	{
		this.y *= y;
		return this;
	}

	@Override
	public Vec2i _mul(final int index, final float f)
	{
		if (index == 0) this.x *= f;
		else if (index == 1) this.y *= f;
		return this;
	}

	@Override
	public Vec2i _muli(final int index, final int i)
	{
		if (index == 0) this.x *= i;
		else if (index == 1) this.y *= i;
		return this;
	}

	public Vec2i _div(final float x, final float y)
	{
		this.x /= x;
		this.y /= y;
		return this;
	}

	@Override
	public Vec2i _div(final float f)
	{
		this.x /= f;
		this.y /= f;
		return this;
	}

	@Override
	public Vec2i _divi(final int i)
	{
		this.x /= i;
		this.y /= i;
		return this;
	}

	public Vec2i _divX(final float x)
	{
		this.x /= x;
		return this;
	}

	public Vec2i _divY(final float y)
	{
		this.y /= y;
		return this;
	}

	@Override
	public Vec2i _div(final int index, final float f)
	{
		if (index == 0) this.x /= f;
		else if (index == 1) this.y /= f;
		return this;
	}

	@Override
	public Vec2i _divi(final int index, final int i)
	{
		if (index == 0) this.x /= i;
		else if (index == 1) this.y /= i;
		return this;
	}

	public Vec2i _set(final Vec2i vec)
	{
		this.x = vec.x;
		this.y = vec.y;
		return this;
	}

	public Vec2i _setg(final int x, final int y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Vec2i _set(final float f)
	{
		this.x = this.y = (int)f;
		return this;
	}

	@Override
	public Vec2i _set(final int i)
	{
		this.x = this.y = i;
		return this;
	}

	public Vec2i _setX(final int x)
	{
		this.x = x;
		return this;
	}

	public Vec2i _setY(final int y)
	{
		this.y = y;
		return this;
	}

	@Override
	public Vec2i _setf(final int index, final float f)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		if (index == 0) this.x = (int)f;
		if (index == 1) this.y = (int)f;
		return this;
	}

	@Override
	public Vec2i _seti(final int index, final int i)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		if (index == 0) this.x = i;
		if (index == 1) this.y = i;
		return this;
	}

	/******MUTABLE MODIFIERS******/

	@Override
	public Vec2i _round()
	{
		this.x = this.x >= 0.5F ? this.x + 1 : this.x;
		this.y = this.y >= 0.5F ? this.y + 1 : this.y;
		return this;
	}

	@Override
	public Vec2i _floor()
	{
		this.x = this.x < 0 ? this.x - 1 : this.x;
		this.y = this.y < 0 ? this.y - 1 : this.y;
		return this;
	}

	@Override
	public Vec2i _ceil()
	{
		this.x = this.x < 0 ? this.x : this.x + 1;
		this.y = this.y < 0 ? this.y : this.y + 1;
		return this;
	}

	@Override
	public Vec2i _normalize()
	{
		final float length = this.length();
		if (length == 0) return this;

		this.x /= length;
		this.y /= length;
		return this;
	}

	@Override
	public Vec2i _abs()
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

	public Vec2i add(final Vec2i vec)
	{
		return new Vec2i(this.x + vec.x, this.y + vec.y);
	}

	public Vec2i add(final int x, final int y)
	{
		return new Vec2i(this.x + x, this.y + y);
	}

	@Override
	public Vec2i add(final float f)
	{
		return new Vec2i((int)(this.x + f), (int)(this.y + f));
	}

	@Override
	public Vec2i addi(final int i)
	{
		return new Vec2i(this.x + i, this.y + i);
	}

	public Vec2i addX(final int x)
	{
		return new Vec2i(this.x + x, this.y);
	}

	public Vec2i addY(final int y)
	{
		return new Vec2i(this.x, this.y + y);
	}

	@Override
	public Vec2i add(final int index, final float f)
	{
		if (index == 0) return new Vec2i((int)(this.x + f), this.y);
		else if (index == 1) return new Vec2i(this.x, (int)(this.y + f));
		return new Vec2i(this.x, this.y);
	}

	@Override
	public Vec2i addi(final int index, final int i)
	{
		if (index == 0) return new Vec2i(this.x + i, this.y);
		else if (index == 1) return new Vec2i(this.x, this.y + i);
		return new Vec2i(this.x, this.y);
	}

	public Vec2i sub(final Vec2i vec)
	{
		return new Vec2i(this.x - vec.x, this.y - vec.y);
	}

	public Vec2i sub(final int x, final int y)
	{
		return new Vec2i(this.x - x, this.y - y);
	}

	@Override
	public Vec2i sub(final float f)
	{
		return new Vec2i((int)(this.x - f), (int)(this.y - f));
	}

	@Override
	public Vec2i subi(final int i)
	{
		return new Vec2i(this.x - i, this.y - i);
	}

	public Vec2i subX(final int x)
	{
		return new Vec2i(this.x - x, this.y);
	}

	public Vec2i subY(final int y)
	{
		return new Vec2i(this.x, this.y - y);
	}

	@Override
	public Vec2i sub(final int index, final float f)
	{
		if (index == 0) return new Vec2i((int)(this.x - f), this.y);
		else if (index == 1) return new Vec2i(this.x, (int)(this.y - f));
		return new Vec2i(this.x, this.y);
	}

	@Override
	public Vec2i subi(final int index, final int i)
	{
		if (index == 0) return new Vec2i(this.x - i, this.y);
		else if (index == 1) return new Vec2i(this.x, this.y - i);
		return new Vec2i(this.x, this.y);
	}

	public Vec2i mul(final int x, final int y)
	{
		return new Vec2i(this.x * x, this.y * y);
	}

	@Override
	public Vec2i mul(final float f)
	{
		return new Vec2i((int)(this.x * f), (int)(this.y * f));
	}

	@Override
	public Vec2i muli(final int i)
	{
		return new Vec2i(this.x * i, this.y * i);
	}

	public Vec2i mulX(final int x)
	{
		return new Vec2i(this.x * x, this.y);
	}

	public Vec2i mulY(final int y)
	{
		return new Vec2i(this.x, this.y * y);
	}

	@Override
	public Vec2i mul(final int index, final float f)
	{
		if (index == 0) return new Vec2i((int)(this.x * f), this.y);
		else if (index == 1) return new Vec2i(this.x, (int)(this.y * f));
		return new Vec2i(this.x, this.y);
	}

	@Override
	public Vec2i muli(final int index, final int i)
	{
		if (index == 0) return new Vec2i(this.x * i, this.y);
		else if (index == 1) return new Vec2i(this.x, this.y * i);
		return new Vec2i(this.x, this.y);
	}

	public Vec2i div(final int x, final int y)
	{
		return new Vec2i(this.x / x, this.y / y);
	}

	@Override
	public Vec2i div(final float f)
	{
		return new Vec2i((int)(this.x / f), (int)(this.y / f));
	}

	@Override
	public Vec2i divi(final int i)
	{
		return new Vec2i(this.x / i, this.y / i);
	}

	public Vec2i divX(final int x)
	{
		return new Vec2i(this.x / x, this.y);
	}

	public Vec2i divY(final int y)
	{
		return new Vec2i(this.x, this.y / y);
	}

	@Override
	public Vec2i div(final int index, final float f)
	{
		if (index == 0) return new Vec2i((int)(this.x / f), this.y);
		else if (index == 1) return new Vec2i(this.x, (int)(this.y / f));
		return new Vec2i(this.x, this.y);
	}

	@Override
	public Vec2i divi(final int index, final int i)
	{
		if (index == 0) return new Vec2i(this.x / i, this.y);
		else if (index == 1) return new Vec2i(this.x, this.y / i);
		return new Vec2i(this.x, this.y);
	}

	public Vec2i setX(final int x)
	{
		return new Vec2i(x, this.y);
	}

	public Vec2i setY(final int y)
	{
		return new Vec2i(this.x, y);
	}

	@Override
	public Vec2i setf(final int index, final float f)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return new Vec2i(index == 0 ? (int)f : this.x, index == 1 ? (int)f : this.y);
	}

	@Override
	public Vec2i seti(final int index, final int i)
	{
		Log.ASSERT(index >= 0 && index < this._size());

		return new Vec2i(index == 0 ? i : this.x, index == 1 ? i : this.y);
	}

	/******IMMUTABLE MODIFIERS******/

	@Override
	public Vec2i round()
	{
		return new Vec2i(this.x >= 0.5F ? this.x + 1 : this.x,
				this.y >= 0.5F ? this.y + 1 : this.y);
	}

	@Override
	public Vec2i floor()
	{
		return new Vec2i(this.x < 0 ? this.x - 1 : this.x,
				this.y < 0 ? this.y - 1 : this.y);
	}

	@Override
	public Vec2i ceil()
	{
		return new Vec2i(this.x > 0 ? this.x : this.x + 1,
				this.y > 0 ? this.y : this.y + 1);
	}

	@Override
	public Vec2i normalize()
	{
		final float length = this.length();
		if (length == 0) return new Vec2i(this.x, this.y);
		return new Vec2i((int)(this.x / length), (int)(this.y / length));
	}

	@Override
	public Vec2i abs()
	{
		return new Vec2i(Math.abs(this.x), Math.abs(this.y));
	}

	/******IMMUTABLE ACCESSORS******/

	public int sum()
	{
		return this.x + this.y;
	}

	public int diff()
	{
		return this.x - this.y;
	}

	public float quot()
	{
		return this.x / (float)this.y;
	}

	public int quoti()
	{
		return this.x / this.y;
	}

	public float prod()
	{
		return this.x * (float)this.y;
	}

	public int prodi()
	{
		return this.x * this.y;
	}

	public float dot(final Vec2i vec)
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

		return index == 0 ? this.x : index == 1 ? this.y : 0;
	}

	@Override
	public float[] toFloatArray()
	{
		return new float[] {this.x, this.y};
	}

	@Override
	public int[] toIntArray()
	{
		return new int[] {this.x, this.y};
	}

	public Vec2f toVec2f()
	{
		return new Vec2f(this.x, this.y);
	}

	/******EQUALITY******/

	public Vec2i copy()
	{
		return new Vec2i(this.x, this.y);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Vec2i)
		{
			final Vec2i vec = (Vec2i)o;
			return Integer.compare(this.x, vec.x) == 0 && Integer.compare(this.y, vec.y) == 0;
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
}
