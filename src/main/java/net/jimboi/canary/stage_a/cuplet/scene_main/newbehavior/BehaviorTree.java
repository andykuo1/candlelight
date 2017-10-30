package net.jimboi.canary.stage_a.cuplet.scene_main.newbehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Andy on 8/12/17.
 */
public class BehaviorTree
{
	public UUID uuid;
	public Node rootNode;

	public BehaviorTree()
	{
		this.uuid = UUID.randomUUID();
	}

	public void tick(Object target, Blackboard data)
	{
		Cursor cursor = new Cursor();
		cursor.target = target;
		cursor.tree = this;
		cursor.data = data;

		this.rootNode.execute(cursor);

		List<Node> prevOpenNodes = data.getLocalData(this).get("_open_nodes");
		List<Node> currOpenNodes = new ArrayList<>();
		currOpenNodes.addAll(cursor.openNodes);

		int start = 0;
		for(int i = 0; i < Math.min(prevOpenNodes.size(), currOpenNodes.size()); ++i)
		{
			start = i + 1;
			if (prevOpenNodes.get(i) != currOpenNodes.get(i))
			{
				break;
			}
		}

		for(int i = prevOpenNodes.size() - 1; i >= start; --i)
		{
			prevOpenNodes.get(i).close(cursor);
		}

		data.getLocalData(this).set("_open_nodes", currOpenNodes);
		data.getLocalData(this).set("_node_count", cursor.nodeCount);
	}
}
