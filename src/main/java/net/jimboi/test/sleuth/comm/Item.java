package net.jimboi.test.sleuth.comm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 9/25/17.
 */
public class Item
{
	public final Set<String> attributes = new HashSet<>();
	public final Map<String, Byte> stats = new HashMap<>();
}
