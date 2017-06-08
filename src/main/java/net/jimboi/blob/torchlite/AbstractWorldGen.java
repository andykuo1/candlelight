package net.jimboi.blob.torchlite;

import java.util.Random;

public abstract class AbstractWorldGen
{
	public abstract boolean generate(Random rand, WorldGenData world);
	public abstract int getWeight();
}
