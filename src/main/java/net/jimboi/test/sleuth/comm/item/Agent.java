package net.jimboi.test.sleuth.comm.item;

import net.jimboi.test.sleuth.comm.goap.Action;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 10/5/17.
 */
public class Agent
{
	public final Set<Action> actions = new HashSet<>();
	public final Container inventory = new Container();
}
