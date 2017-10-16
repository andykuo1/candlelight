package net.jimboi.test.pye;

/**
 * Created by Andy on 10/15/17.
 */
public class Pye extends GameEntity
{
	public Pye(float x, float y)
	{
		super(x, y);

		this.vspeed = 5 - (float) (Math.random() * 10);
		this.hspeed = 5 - (float) (Math.random() * 10);
		this.dir = (float) Math.random();
		this.rotspeed = 1 - (float) (Math.random() * 2);
	}
	/*

	Every Pye is a collection of stats that is the result of a neural network.

	Since a neural network solves systems, the system must be complex enough and allow
	for random variation to be interesting.

	Pye should grow.

	Pye can gain more abilities.
	Must enforce different play styles:
		- Tank
		- Scout
		- Sneaky
		- Range

	You are given a list of inputs and outputs.
	You are only allowed a certain number of inputs/outputs on a match.

	INPUTS:
	- SIGHT: SOLID
	- SIGHT: COLOR
	- SIGHT: DISTANCE
	- SIGHT: SPATIAL AWARENESS (HOW MUCH SPACE)
	- MOTION: SPEED
	- MOTION: ROTATION
	- BODY: ENERGY AMT
	- BODY: LIFE AMT
	- BODY: SIZE
	- BODY: COLOR
	- SMELL: TYPE
	- SMELL: DISTANCE
	- SMELL: DIRECTION
	- TOUCH: SOLID
	- TOUCH: TYPE
	- TOUCH: COLOR

	OUTPUTS:
	- MOVE
	- ROTATE
	- DO ACTION
		- Poison spit
		- Punch
		- Split
	- Targeting System:
		- Move Towards / Away
		- Sidestep


	OUTPUTS:



	Genetic Tamagachi meets MOBAs

	Every Pye starts with some inputs and some outputs.

	Outputs would be like motor functions. OR abstract memory slots.

	Basically teaching data science through designing the neural network.

	FIRST: Feed-Forward Networks or Recurrent
		Does not contain loops or cycles
	SECOND: Hidden Layers?
		Hidden layers to solve.


	 */
}
