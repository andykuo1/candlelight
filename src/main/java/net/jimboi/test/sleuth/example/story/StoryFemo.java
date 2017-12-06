package net.jimboi.test.sleuth.example.story;

import net.jimboi.test.sleuth.PrintableStruct;
import net.jimboi.test.sleuth.example.story.venue.Entrance;
import net.jimboi.test.sleuth.example.story.venue.Room;
import net.jimboi.test.sleuth.example.story.venue.Venue;
import net.jimboi.test.sleuth.example.story.venue.VenueBuilder;

import org.zilar.console.Console;
import org.zilar.console.ConsoleStyle;
import org.zilar.console.board.ConsoleBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Andy on 10/7/17.
 */
public class StoryFemo extends ConsoleBoard
{
	private final List<String> inventory = new ArrayList<>();

	private Stack<Room> roomlog = new Stack<>();
	private Venue venue;

	public StoryFemo()
	{
		for(int i = 0; i < 4; ++i)
		{
			this.inventory.add(null);
		}

		for(int i = 0; i < 3; ++i)
		{
			this.addItem("ITEM #" + i);
		}

		VenueBuilder vb = new VenueBuilder()
				.setName("Mansion")
				.addRoom(0).setName("Outside")
				.addItem("GardenHose")
				.pack()
				.addEntrance(0, 17).pack()
				.addRoom(1).setName("LivingRoom")
				.addItem("Couch")
				.addItem("Couch")
				.addItem("EndTable")
				.addItem("EndTable")
				.addItem("FlowerVase")
				.addItem("CoffeeTable")
				.addItem("Lamp")
				.pack()
				.addRoom(2).setName("Kitchen")
				.addItem("CookingPot")
				.addItem("FryingPan")
				.addItem("Fork")
				.addItem("Knife")
				.addItem("Spoon")
				.addItem("Spatula")
				.addItem("Refrigerator")
				.addItem("FlowerVase")
				.addItem("CookingStove")
				.addItem("Microwave")
				.addItem("Oven")
				.addItem("Plate")
				.addItem("Bowl")
				.addItem("Cup")
				.pack()
				.addEntrance(2, 3).pack()
				.addEntrance(2, 4).pack()
				.addRoom(3).setName("Pantry").pack()
				.addRoom(4).setName("DiningRoom").pack()
				.addRoom(5).setName("Hallway").pack()
				.addEntrance(5, 1).pack()
				.addEntrance(5, 2).pack()
				.addEntrance(5, 4).pack()
				.addEntrance(5, 6).pack()
				.addEntrance(5, 7).pack()
				.addRoom(6).setName("Closet").pack()
				.addRoom(7).setName("Bathroom").pack()
				.addRoom(8).setName("MasterBedroom").pack()
				.addEntrance(8, 9).pack()
				.addEntrance(8, 10).pack()
				.addEntrance(8, 11).pack()
				.addRoom(9).setName("MasterBathroom").pack()
				.addRoom(10).setName("MasterCloset").pack()
				.addRoom(11).setName("Patio").pack()
				.addRoom(12).setName("GuestBedroom").pack()
				.addEntrance(12, 13).pack()
				.addEntrance(12, 14).pack()
				.addRoom(13).setName("GuestCloset").pack()
				.addRoom(14).setName("GuestBathroom").pack()
				.addRoom(15).setName("Staircase").pack()
				.addEntrance(15, 16).pack()
				.addRoom(16).setName("UpperHallway").pack()
				.addEntrance(16, 8).pack()
				.addEntrance(16, 12).pack()
				.addRoom(17).setName("Foyer").pack()
				.addEntrance(17, 5).pack()
				.addEntrance(17, 15).pack();
		this.venue = vb.bake();
		this.roomlog.push(this.venue.getOutside());
	}

	@Override
	public void onStart(Console con)
	{
		ConsoleStyle.title(con, "The Story of Richard Femo");

		ConsoleStyle.message(con, "You arrive at the crime scene.");
		ConsoleStyle.newline(con);

		ConsoleStyle.button(con, "Approach", () -> this.next(con, this::theVenue));
		ConsoleStyle.button(con, "Exit", con::quit);
	}

	private void theVenue(Console con)
	{
		ConsoleStyle.title(con, this.venue.getName());

		Room currentRoom = this.roomlog.peek();
		if (currentRoom == this.venue.getOutside())
		{
			ConsoleStyle.message(con, "You are outside.");
		}
		else
		{
			ConsoleStyle.message(con, "You are at the " + currentRoom.getName() + ".");
		}
		ConsoleStyle.newline(con);

		for(Entrance entrance : this.venue.getAvailableEntrances(currentRoom))
		{
			Room room = entrance.getDestination(currentRoom);
			if (this.roomlog.size() > 1 && room == this.roomlog.get(this.roomlog.size() - 2))
			{
				continue;
			}
			ConsoleStyle.button(con, room.getName(), () -> this.goToRoom(con, room));
		}

		if (this.roomlog.size() > 1)
		{
			ConsoleStyle.button(con, "Back", () -> {
				this.roomlog.pop();
				this.goToRoom(con, this.roomlog.peek());
			});
		}
		else
		{
			ConsoleStyle.button(con, "Leave", () -> this.back(con));
		}
	}

	private void goToRoom(Console con, Room room)
	{
		con.clear();
		if (this.roomlog.peek() == room)
		{
			ConsoleStyle.message(con, "You go back to the " + room.getName() + "...");
		}
		else
		{
			ConsoleStyle.message(con, "You go to the " + room.getName() + "...");
			this.roomlog.push(room);
		}
		ConsoleStyle.newline(con);
		this.refresh(con);
	}

	private void lookAround(Console con)
	{
		ConsoleStyle.title(con, "Mansion");
	}

	public void openInventory(Console con)
	{
		ConsoleStyle.title(con, "Inventory");

		for(int i = 0; i < this.inventory.size(); ++i)
		{
			if (i == 0)
			{
				ConsoleStyle.message(con, "Equipped:");
				ConsoleStyle.newline(con);
			}

			String item = this.inventory.get(i);

			int index = i;
			if (item == null)
			{
				ConsoleStyle.button(con,
						ConsoleStyle.repeatUntil("-", con.getMaxLength() - 4),
						() -> {
					this.moveItem(0, index);
					this.refresh(con);
				});
			}
			else
			{
				ConsoleStyle.button(con,
						ConsoleStyle.paddingUntil(item + " ", "-", con.getMaxLength() - 4),
						() -> {
					this.moveItem(index, 0);
					this.refresh(con);
				});
			}

			if (i == 0)
			{
				ConsoleStyle.message(con, "Pocketed:");
				ConsoleStyle.newline(con);
			}
		}

		ConsoleStyle.divider(con, "- ");
		ConsoleStyle.newline(con);

		ConsoleStyle.button(con, "Back", this::back);
	}

	public void moveItem(int from, int to)
	{
		String item = this.inventory.get(to);
		this.inventory.set(to, this.inventory.get(from));
		this.inventory.set(from, item);
	}

	public boolean addItem(String item)
	{
		for(int i = 1; i < this.inventory.size(); ++i)
		{
			if (this.inventory.get(i) == null)
			{
				this.inventory.set(i, item);
				return true;
			}
		}
		return false;
	}

	private abstract class Item extends Nameable
	{
		public Item(String name)
		{
			super(name);
		}

		public abstract void onInteract();

		public boolean canPickup()
		{
			return true;
		}
	}

	private static abstract class Nameable extends PrintableStruct
	{
		private final String name;

		public Nameable(String name)
		{
			this.name = name;
		}

		public final String getName()
		{
			return this.name;
		}
	}
}
