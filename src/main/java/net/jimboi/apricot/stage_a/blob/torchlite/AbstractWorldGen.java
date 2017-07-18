package net.jimboi.apricot.stage_a.blob.torchlite;

import java.util.Random;

public abstract class AbstractWorldGen
{
	public abstract boolean generate(Random rand, WorldGenData world);
	public abstract int getWeight();
}
