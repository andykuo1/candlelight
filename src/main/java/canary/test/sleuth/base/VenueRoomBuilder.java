package canary.test.sleuth.base;

import canary.test.sleuth.base.builder.SubBuilder;
import canary.test.sleuth.cluedo.RoomType;

/**
 * Created by Andy on 10/7/17.
 */
public class VenueRoomBuilder extends SubBuilder<VenueBuilder>
{
	public final int id;

	public RoomType type;

	public VenueRoomBuilder(VenueBuilder parent, int id)
	{
		super(parent);
		this.id = id;
	}

	public VenueRoomBuilder setRoomType(RoomType type)
	{
		this.type = type;
		return this;
	}

	@Override
	public VenueBuilder pack()
	{
		this.parent.rooms.put(this.id, this);
		return super.pack();
	}
}
