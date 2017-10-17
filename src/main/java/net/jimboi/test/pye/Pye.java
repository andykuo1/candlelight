package net.jimboi.test.pye;

import org.joml.Vector2f;

import java.util.Arrays;

/**
 * Created by Andy on 10/16/17.
 */
public class Pye extends Matter
{
	protected final Appendage[] appendages;

	protected float maxEnergy = 5;
	protected float energy = 3;

	public Pye(Appendage... appendages)
	{
		super(0, 0, 0);
		this.appendages = appendages;
	}

	@Override
	public void onUpdate(PetriDish dish)
	{
		for(int i = 0; i < this.appendages.length; ++i)
		{
			Appendage appendage = this.appendages[i];
			if (appendage != null)
			{
				appendage.execute(dish, this, i, 0.01F);
			}
		}

		/*
		for (Appendage appendage : this.appendages)
		{
			if (appendage == null) continue;

			float f = appendage.poll(this);
			this.neurals.getInput().set(appendage.getID(), f);
		}
		NeuralLayer output = this.neurals.solve();
		for (int i = 0; i < output.size(); ++i)
		{
			float f = output.get(i);
			this.appendages[i].execute(this, f);
		}
		*/
	}

	public Vector2f getPosition(int index, Vector2f dst)
	{
		float dir = this.getDirection(index);
		return dst.set(this.posX, this.posY).add((float) Math.cos(dir) * this.radius, (float) Math.sin(dir) * this.radius);
	}

	public float getDirection(int index)
	{
		float f = index / (float) this.appendages.length;
		return f * PI2 + this.rotation;
	}

	public int indexOf(Appendage appendage)
	{
		for(int i = 0; i < this.appendages.length; ++i)
		{
			if (appendage.equals(this.appendages[i]))
			{
				return i;
			}
		}
		return -1;
	}

	public Iterable<Appendage> getAppendages()
	{
		return Arrays.asList(this.appendages);
	}

	public float getEnergyPercent()
	{
		return this.energy / this.maxEnergy;
	}
}
