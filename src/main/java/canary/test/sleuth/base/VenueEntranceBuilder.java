package canary.test.sleuth.base;

import canary.test.sleuth.base.builder.SubBuilder;

import canary.bstone.util.pair.UnorderedPair;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueEntranceBuilder extends SubBuilder<VenueBuilder>
{
	public final UnorderedPair<Integer> rooms;

	public EntranceType type;

	public VenueEntranceBuilder(VenueBuilder parent, int from, int to)
	{
		super(parent);
		this.rooms = new UnorderedPair<>(from, to);
	}

	public VenueEntranceBuilder setEntranceType(EntranceType type)
	{
		this.type = type;
		return this;
	}

	@Override
	public VenueBuilder pack()
	{
		this.parent.entrances.add(this);
		return super.pack();
	}
}
