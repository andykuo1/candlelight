package canary.test.sleuth.data;

/**
 * Created by Andy on 9/23/17.
 */
public enum RelationshipStatus
{
	SPOUSE,
	PARENT,
	CHILD,
	SIBLING,
	RELATIVE,
	ACQUAINTANCE,
	FRIEND,
	COUPLE,
	NEIGHBOR,
	EMPLOYER,
	EMPLOYEE,
	STRANGER;

	public static RelationshipStatus getOtherStatus(RelationshipStatus status)
	{
		return status == PARENT ? CHILD
				: status == CHILD ? PARENT
				: status == EMPLOYER ? EMPLOYEE
				: status == EMPLOYEE ? EMPLOYER
				: status;
	}
}
