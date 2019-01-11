package apricot.stage_a.blob.torchlite;

public abstract class Vector
{
	/******CONSTRUCTORS******/

	public Vector(final float[] array)
	{
		for(int i = 0; i < array.length; ++i)
		{
			this._setf(i, array[i]);
		}
	}

	public Vector(final int[] array)
	{
		for(int i = 0; i < array.length; ++i)
		{
			this._seti(i, array[i]);
		}
	}

	public Vector() {}

	/******MUTABLE ARITHMETIC******/

	public abstract Vector _add(final float f);

	public abstract Vector _addi(final int i);

	public abstract Vector _add(final int index, final float f);

	public abstract Vector _addi(final int index, final int i);

	public abstract Vector _sub(final float f);

	public abstract Vector _subi(final int i);

	public abstract Vector _sub(final int index, final float f);

	public abstract Vector _subi(final int index, final int i);

	public abstract Vector _mul(final float f);

	public abstract Vector _muli(final int i);

	public abstract Vector _mul(final int index, final float f);

	public abstract Vector _muli(final int index, final int i);

	public abstract Vector _div(final float f);

	public abstract Vector _divi(final int i);

	public abstract Vector _div(final int index, final float f);

	public abstract Vector _divi(final int index, final int i);

	public abstract Vector _set(final float f);

	public abstract Vector _set(final int i);

	public abstract Vector _setf(final int index, final float f);

	public abstract Vector _seti(final int index, final int i);

	/******MUTABLE MODIFIERS******/

	public abstract Vector _round();

	public abstract Vector _floor();

	public abstract Vector _ceil();

	public abstract Vector _normalize();

	public abstract Vector _abs();

	public abstract int _size();

	/******IMMUTABLE ARITHMETIC******/

	public abstract Vector add(final float f);

	public abstract Vector addi(final int i);

	public abstract Vector add(final int index, final float f);

	public abstract Vector addi(final int index, final int i);

	public abstract Vector sub(final float f);

	public abstract Vector subi(final int i);

	public abstract Vector sub(final int index, final float f);

	public abstract Vector subi(final int index, final int i);

	public abstract Vector mul(final float f);

	public abstract Vector muli(final int i);

	public abstract Vector mul(final int index, final float f);

	public abstract Vector muli(final int index, final int i);

	public abstract Vector div(final float f);

	public abstract Vector divi(final int i);

	public abstract Vector div(final int index, final float f);

	public abstract Vector divi(final int index, final int i);

	public abstract Vector setf(final int index, final float f);

	public abstract Vector seti(final int index, final int i);

	/******IMMUTABLE MODIFIERS******/

	public abstract Vector round();

	public abstract Vector floor();

	public abstract Vector ceil();

	public abstract Vector normalize();

	public abstract Vector abs();

	/******IMMUTABLE ACCESSORS******/

	public abstract float lengthSqu();

	public float length()
	{
		return (float)Math.sqrt(this.lengthSqu());
	}

	public abstract float getf(final int index);
	public abstract int geti(final int index);

	public abstract float[] toFloatArray();
	public abstract int[] toIntArray();
}
