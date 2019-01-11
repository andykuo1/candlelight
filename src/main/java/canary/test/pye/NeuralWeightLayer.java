package canary.test.pye;

/**
 * Created by Andy on 10/15/17.
 */
public class NeuralWeightLayer
{
	private final NeuralLayer src;
	private final NeuralLayer dst;

	private final float[][] data;

	public NeuralWeightLayer(NeuralLayer src, NeuralLayer dst)
	{
		this.src = src;
		this.dst = dst;

		this.data = new float[dst.size()][src.size()];
	}

	public void setWeight(int src, int dst, float value)
	{
		this.data[dst][src] = value;
	}

	public float getWeight(int src, int dst)
	{
		return this.data[dst][src];
	}

	public boolean isSource(NeuralLayer layer)
	{
		return this.src.equals(layer);
	}

	public boolean isDestination(NeuralLayer layer)
	{
		return this.dst.equals(layer);
	}
}
