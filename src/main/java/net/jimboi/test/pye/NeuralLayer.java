package net.jimboi.test.pye;

/**
 * Created by Andy on 10/15/17.
 */
public class NeuralLayer
{
	private final float[] data;

	public NeuralLayer(int size)
	{
		this.data = new float[size];
	}

	public NeuralLayer process(WeightedLayer weights, NeuralLayer dst)
	{
		if (!weights.isSource(this)) throw new IllegalArgumentException("invalid weighted layer");
		if (!weights.isDestination(dst)) throw new IllegalArgumentException("invalid destination neural layer");

		for(int i = 0; i < dst.size(); ++i)
		{
			float f = 0;
			for(int j = 0; j < this.size(); ++j)
			{
				f += this.get(j) * weights.getWeight(j, i);
			}
			dst.set(i, sigmoid(f));
		}

		return dst;
	}

	public void set(int index, float value)
	{
		this.data[index] = value;
	}

	public float get(int index)
	{
		return this.data[index];
	}

	public int size()
	{
		return this.data.length;
	}

	private static float sigmoid(float f)
	{
		return (float) (1F / (1F + Math.exp(-f)));
	}
}
