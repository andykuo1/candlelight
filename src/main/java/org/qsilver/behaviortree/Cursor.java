package org.qsilver.behaviortree;

import org.qsilver.behaviortree.data.BehaviorData;
import org.qsilver.behaviortree.data.LocalDataHelper;
import org.qsilver.behaviortree.node.Node;

import java.util.Stack;

/**
 * Contains contextual data for executing @BehaviorTree;
 * <br>Should exist only for a single run of a tree;
 * <br>Created only by @BehaviorTree
 */
public final class Cursor
{
	private static String repeat(String str, int iterations)
	{
		StringBuilder sb = new StringBuilder();
		for (; iterations >= 0; --iterations)
		{
			sb.append(str);
		}
		return sb.toString();
	}

	public static boolean DEBUG_MODE = false;

	private final BehaviorTree tree;
	private final BehaviorData data;
	private final LocalDataHelper dataHelper;

	private final Stack<Node> openNodes = new Stack<Node>();

	private int nodesEntered = 0;
	private int nodesOpened = 0;
	private int nodesClosed = 0;
	private int nodesExited = 0;

	protected Cursor(BehaviorTree tree, BehaviorData data)
	{
		this.tree = tree;
		this.data = data;

		this.dataHelper = new LocalDataHelper(this);
	}

	public void onNodeOpen(Node node)
	{
		this.nodesOpened++;
	}

	public void onNodeClose(Node node)
	{
		this.nodesClosed++;
	}

	public void onNodeEnter(Node node)
	{
		this.nodesEntered++;
		this.openNodes.push(node);
		if (DEBUG_MODE)
		{
			System.out.println(repeat("   |", this.openNodes.size() - 1) + node);
		}
	}

	public void onNodeExit(Node node)
	{
		this.nodesExited++;
		this.openNodes.pop();
	}

	public void onNodeTick(Node node)
	{

	}

	public void onTreeResult(Result result)
	{
		if (DEBUG_MODE)
		{
			System.out.println();
			System.out.println("================================================");
			System.out.println("RESULT : " + result);
			System.out.println("================================================");
			System.out.println("DEBUG_MODE: " + DEBUG_MODE);
			System.out.println("NODES_OPENED: " + this.nodesOpened);
			System.out.println("NODES_CLOSED: " + this.nodesClosed);
			System.out.println("NODES_ENTERED: " + this.nodesEntered);
			System.out.println("NODES_EXITED: " + this.nodesExited);
			if (this.nodesEntered != this.nodesExited)
			{
				System.out.println();
				System.out.println("ERROR! NODES ENTERED " + this.nodesEntered + " BUT ONLY " + this.nodesExited + " EXITED!");

				for (int i = this.openNodes.size() - 1; i >= 0; --i)
				{
					System.out.println(repeat("   |", this.openNodes.size() - i - 1) + this.openNodes.get(i));
				}
			}
			if (this.nodesOpened != this.nodesClosed)
			{
				System.out.println();
				System.out.print("OH NO! ");
				System.out.print("NODES OPENED " + this.nodesOpened + " BUT ONLY " + this.nodesClosed + " CLOSED!");
				System.out.println(" . . . BUT IT IS OKAY!");
				int i = this.nodesOpened - this.nodesClosed;
				if (result == Result.RUNNING)
				{
					if (i < 0)
					{
						System.out.print(". . . NO IT IS NOT. HOW CAN I OPEN " + i + " NODES?");
					}
					else
					{
						System.out.print(". . . SHOULD CLOSE " + i + " NODES NEXT RUN! (LOOK HERE LATER)");
					}
				}
				else
				{
					if (i > 0)
					{
						System.out.print(". . . NO IT IS NOT. HOW CAN I CLOSE " + (-i) + " NODES?");
					}
					else
					{
						System.out.print(". . . SHOULD BE CLOSING " + (-i) + " NODES FROM EARLIER! (LOOK ABOVE)");
					}
				}
				System.out.println();
			}
			System.out.println("================================================");
			System.out.println();
		}
	}

	public Node getPreviousNode()
	{
		return this.openNodes.get(this.openNodes.size() - 2);
	}

	public Node getCurrentNode()
	{
		return this.openNodes.peek();
	}

	public Node getTargetNode()
	{
		return this.openNodes.isEmpty() ? this.tree.getRoot() : this.openNodes.peek();
	}

	public Stack<Node> getOpenNodes()
	{
		return this.openNodes;
	}

	public BehaviorTree getBehaviorTree()
	{
		return this.tree;
	}

	public BehaviorData getBehaviorData()
	{
		return this.data;
	}

	public LocalDataHelper getDataHelper()
	{
		return this.dataHelper;
	}
}
