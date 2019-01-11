package boron.stage_a.goblet.newbehavior;

/**
 * Created by Andy on 8/12/17.
 */
public class Blackboard
{
	protected BlackboardData globalData;
	protected BlackboardData localData;

	public BlackboardData getGlobalData()
	{
		return this.globalData;
	}

	public BlackboardData getLocalData(BehaviorTree tree)
	{
		String id = tree.uuid.toString();
		if (!this.localData.contains(id))
		{
			BlackboardData data = new BlackboardData();
			this.localData.set(id, data);
			return data;
		}
		return (BlackboardData) this.localData.get(id);
	}

	public BlackboardData getLocalData(BehaviorTree tree, Node node)
	{
		BlackboardData treeData = this.getLocalData(tree);

		String id = node.uuid.toString();
		if (!treeData.contains(id))
		{
			BlackboardData data = new BlackboardData();
			treeData.set(id, data);
			return data;
		}
		return (BlackboardData) treeData.get(id);
	}
}
