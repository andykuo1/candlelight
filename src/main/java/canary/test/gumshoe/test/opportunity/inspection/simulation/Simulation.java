package canary.test.gumshoe.test.opportunity.inspection.simulation;

import canary.test.gumshoe.test.opportunity.inspection.Actor;
import canary.test.gumshoe.test.opportunity.inspection.Inspection;
import canary.test.gumshoe.test.opportunity.inspection.simulation.action.Action;
import canary.test.gumshoe.test.opportunity.inspection.simulation.action.ActionConversation;
import canary.test.gumshoe.test.opportunity.inspection.simulation.action.ActionLocationGoto;
import canary.test.gumshoe.test.venue.Venue;
import canary.test.gumshoe.test.venue.layout.Entrance;
import canary.test.sleuth.data.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 12/8/17.
 */
public class Simulation extends Inspection
{
	private final Venue venue;

	public Simulation(Venue venue)
	{
		this.venue = venue;
	}

	public void run()
	{
		this.venue.addActor(new Actor("Bobbaline")
				.setLocation(this.venue.getLayout().getLocation("Foyer")));
		this.venue.addActor(new Actor("Bobby")
				.setLocation(this.venue.getLayout().getLocation("Foyer")));
		this.venue.addActor(new Actor("Robert")
				.setLocation(this.venue.getLayout().getLocation("Foyer")));
		this.venue.addActor(new Actor("Bob")
				.setLocation(this.venue.getLayout().getLocation("Foyer")));
		this.venue.addActor(new Actor("Billy")
				.setLocation(this.venue.getLayout().getLocation("Foyer")));

		Time startTime = new Time(16);
		Time time = new Time();
		for(int i = 0; i < 12; ++i)
		{
			time.set(startTime).add(i, time);
			for(Actor actor : this.venue.getActors())
			{
				this.processHour(time, actor);
			}
		}
	}

	private void processHour(Time time, Actor actor)
	{
		//this.recordAction(time, actor, actor, "was at", "the " + actor.getLocation());

		if (Math.random() > 0.5)
		{
			List<Entrance> entrances = new ArrayList<>();
			this.venue.getLayout().getAvailableEntrances(actor.getLocation(), entrances);
			this.moveToRoom(time, actor, entrances.get(new Random().nextInt(entrances.size())));
		}
		else
		{
			List<Actor> actors = new ArrayList<>();
			for(Actor other : this.venue.getActorsByLocation(actor.getLocation()))
			{
				actors.add(other);
			}
			this.talkTo(time, actor, actors.get(new Random().nextInt(actors.size())));
		}
	}

	private void talkTo(Time time, Actor actor, Actor other)
	{
		if (actor == other) return;
		if (actor.getLocation() != other.getLocation()) return;

		Action action = new ActionConversation(other);
		action.execute(this.venue, time, actor);
	}

	private void moveToRoom(Time time, Actor actor, Entrance entrance)
	{
		if (!entrance.hasLocation(actor.getLocation())) return;

		Action action = new ActionLocationGoto(entrance);
		action.execute(this.venue, time, actor);
	}

	/*
	private List<String> planDiningHour()
	{
		final List<Action> actions = new LinkedList<>();
		final Environment goal = new Environment();
		final Environment world = new Environment();
		final GOAP planner = newa Ô≤ GOAP();

		goal.set("EatingDinner", true);

		actions.add(new Action("GoToDiningRoom", 2)
				.addEffect("AtDiningRoom", true));

		actions.add(new Action("EatDinner", 1)
				.addCondition("AtDiningRoom", true)
				.addEffect("EatingDinner", true));

		List<Action> plan = planner.plan(env -> actions, world, goal);
		List<String> acts = new ArrayList<>();
		for(Action action : plan)
		{
			acts.add(action.getName());
		}
		return acts;
	}
	*/

	/*

		Verifiable Clues?
			- Time of Murder
			- Social Memory
			- Action Memory
			- Hard Evidence


	  Enter the mansion
	    Leave outside
	  Attend the dinner
	    Go to the dining room
	  Go to sleep
	    Go to my room

	  versus

	  Enter the mansion
	    Look for a weapon
	    Pick up weapon
	  Attend the dinner
	  Go to sleep
	  Murder!
	    Go to the room with victim
	    Kill victim

	*/

	//Events
	//The party begins at 5.
	//Dinner is at 6-8.
	//Activity is at 10-11.
	//Lights off by 12.
	//Butler wakes up at 5 in the morning.
	//Breakfast at 7.

	//Goals
	//Every action must be calculable to a number
	//How do you represent actions, etc.


	//Know location of Person
	//Ask person for location of Person

	/*


	Goal Planner:
		- Attend the dinner party
		- Convince everyone I am normal
		- Murder the Old Man
	Task Planner:
		- Attend the dinner party
			- Explore the house
				- Visit each room
				- Take note of weapons
			- Attend the dinner at 6
		- Convince everyone I am normal
			- Talk to people
			- Hide a weapon

		- Murder the Old Man
			- Wait until lights are out
			- Kill the Old Man with the Weapon
			- Dispose of the Weapon
			- Return to Room

	Every plan has a goal.

	Certain circumstances can trigger a new plan.
	Every hour, the goals are updated.
	And the plan is reevaluated for the next hour.
	Every action takes time.

	Should there be double planning?
	An hourly planner, and an overall planner?
	The overall planner would determine the cost and bias weights

	Available Actions:
	Go to the dinner at 6.



	Can move to locations (with known locations given a bonus)
	Each plan determines where to go and then what to do.

	Goals
	-
	What do you want to do?
	-


Bathroom Meter. (will make people to go the bathroom for longer periods of time)
Stamina Meter. (will make people sleep or rest)
Stress Meter. (will make people more nervous, etc. and get tired, but can't rest)
Emotion Meter. (will make people react differently)
    Anger vs Fear
    Sad
    Happiness (fun.)
Hunger Meter. (will make people go eat)
    Peckish -> Hungry

(Perceived) Guilt Meter.
	When failing social norms
	When encountering guilty topics
	Possession of discoverable evidence of crime
Relationship Meter. (per person)

FUTURE:
Cleanliness Meter. (will make people use the bathroom)
    Clean -> Dirty


Traits:
- Kleptomaniac
    - Take random small objects
- Sociable
    - Love to talk to large groups
- Anti-Social
    - Avoid talking

Secrets:
- Break a relationship between PERSON and PERSON
- Steal an OBJECT
- Have Fun
- Eat Dinner

Tasks:
Find and look at an OBJECT.
    - Someone was talking about OBJECT, and I needed to see it.
Use the bathroom
    - Need to use the bathroom
Make a snack
    - Peckish
Wash up in the bathroom
    - Escape an awkward conversation
    - After some activity
Discuss TOPIC with PERSON
    - Interested in TOPIC
    - Small talk
    - Interested in PERSON
Ask about later plans...
    - Interested in PERSON
    - Gossip
Avoid TOPIC
    - Guilty past
    - Dislike TOPIC
Avoid PERSON
    - Dislike PERSON
    - Scared of PERSON
Discuss with PERSON in private
    - Close relationship
    - Talk about sensitive matters
        - Intimate life
        - Secrets
        - Embarrassing
        - Relationship
Fix a plumbing fixture
    - Helpful
    - Wants to use
Make drinks
    - Thirsty



All possible guilty actions:
- Explore each room to look for weapons
    - Explore the house
    - Visit the ??? room
- Steal a weapon
    - Use a ??? for something
    - Kleptomaniac
- Have a weapon on hand
    - Use a ??? for something
- Hide a weapon
    - Secret motive
- Setup a weapon
    - alternative motive
- Discover each other's plans, in order to avoid them
    - Talk to another person
- Take note of where the victim is going, in order to meet them later
    - Talk to another person about someone
    - Follow someone and talk to them
- Meet with victim in private
    - Discuss sensitive matters
    - Confront about something
- Avoid people
    - Dislike people
    - Unsociable trait
- (instinct) argue with people
    - Dislike people
    - Angry state


	 */
}
