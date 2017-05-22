package net.jimboi.mod.component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andy on 5/21/17.
 */
public abstract class System
{
	private Set<Entity> entities = new HashSet<>();

	@SuppressWarnings("unchecked")
	public Class<? extends Component>[] getRequiredComponents()
	{
		return new Class[0];
	}

	/**Gets a set of entities with all required components; changes are not reflected!*/
	public Set<Entity> getEntities(EntityManager entityManager)
	{
		entityManager.getEntitiesByComponent(this.entities, this.getRequiredComponents());
		return this.entities;
	}
}
