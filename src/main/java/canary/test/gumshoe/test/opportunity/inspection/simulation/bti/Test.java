package canary.test.gumshoe.test.opportunity.inspection.simulation.bti;

/**
 * Created by Andy on 12/18/17.
 */
public class Test
{
	public static void main(String[] args)
	{
		DeciderGroup eat = new DeciderGroupPrioritized("Eat", new Decider[]{
				new DeciderGroupPrioritized("LookForFood", new Decider[]{
						new DeciderBehaviorFailTick("LookFoodToggle")
				}),
				new DeciderBehaviorFailTick("ToggleFood"),
				new DeciderBehavior("FindFood"),
				new DeciderBehavior("EatFood")
		});

		BehaviorTree tree = new BehaviorTree(eat);

		BehaviorTraverser traverser = new BehaviorTraverser(tree);

		traverser.tick();
		System.out.println("------");
		traverser.tick();
		System.out.println("------");
		traverser.tick();
		System.out.println("------");
		traverser.tick();
		System.exit(0);
	}
}
