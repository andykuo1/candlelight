package canary.test.pye;

/**
 * Created by Andy on 10/15/17.
 */
public class NeuralNetwork
{
	private NeuralLayer inputs;
	private NeuralWeightLayer weightedInOut;
	private NeuralLayer outputs;

	public NeuralNetwork(int inputs, int outputs)
	{
		this.inputs = new NeuralLayer(inputs);
		this.outputs = new NeuralLayer(outputs);
		this.weightedInOut = new NeuralWeightLayer(this.inputs, this.outputs);
	}

	public NeuralLayer solve()
	{
		this.inputs.process(this.weightedInOut, this.outputs);
		return this.outputs;
	}

	public NeuralLayer getInput()
	{
		return this.inputs;
	}

	public NeuralLayer getOutput()
	{
		return this.outputs;
	}
}
