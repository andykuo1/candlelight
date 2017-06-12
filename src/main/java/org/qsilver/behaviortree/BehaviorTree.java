package org.qsilver.behaviortree;

import org.qsilver.behaviortree.data.BehaviorData;
import org.qsilver.behaviortree.node.Node;

import java.util.Stack;

/**
 * The Behavior Tree;
 * <br>Call {@link #tick(BehaviorTree, Node, BehaviorData)} to begin
 */
public class BehaviorTree
{
	public final String id = Node.createUUID();
	private final Node root;

	public BehaviorTree(Node root)
	{
		this.root = root;
	}

	public void onStart(BehaviorData data)
	{
	}

	public static Result tick(BehaviorTree tree, BehaviorData data)
	{
		Cursor cursor = new Cursor(tree, data);

		tree.onStart(data);
		Result r = tree.getRoot().execute(cursor);
		cursor.onTreeResult(r);

		Stack<Node> prevOpenNodes = cursor.getBehaviorData().getData(cursor.getBehaviorTree().id, null).get("_open_nodes", new Stack<Node>());
		Stack<Node> openNodes = new Stack<Node>();
		openNodes.addAll(cursor.getOpenNodes());

		int start = 0;
		for (int i = 0; i < Math.min(prevOpenNodes.size(), openNodes.size()); ++i)
		{
			start = i + 1;
			if (prevOpenNodes.get(i) != openNodes.get(i))
			{
				break;
			}
		}

		for (int i = prevOpenNodes.size() - 1; i >= start; --i)
		{
			prevOpenNodes.get(i).close(cursor);
		}

		cursor.getBehaviorData().getData(cursor.getBehaviorTree().id, null).set("_open_nodes", openNodes);
		return r;
	}

	public Node getRoot()
	{
		return this.root;
	}

	public Node toNode()
	{
		return this.root.copy();
	}
}
