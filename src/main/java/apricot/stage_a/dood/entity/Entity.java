package apricot.stage_a.dood.entity;

/**
 * Created by Andy on 5/21/17.
 */
public final class Entity
{
	private EntityManager entityManager;
	int id;

	private boolean dead = false;

	Entity(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	public Entity addComponent(Component component)
	{
		this.entityManager.addComponentToEntity(this, component);
		return this;
	}

	public <T extends Component> T removeComponent(Class<T> componentType)
	{
		return this.entityManager.removeComponentFromEntity(this, componentType);
	}

	public <T extends Component> T getComponent(Class<T> componentType)
	{
		return this.entityManager.getComponentByEntity(componentType, this);
	}

	public boolean hasComponent(Class<? extends Component> componentType)
	{
		return this.entityManager.hasComponentByEntity(componentType, this);
	}

	public void setDead()
	{
		this.dead = true;
	}

	public boolean isDead()
	{
		return this.dead;
	}

	public int getEntityID()
	{
		return this.id;
	}

	public boolean equals(Object o)
	{
		if (o instanceof Entity)
		{
			return this.entityManager == ((Entity) o).entityManager && this.id == ((Entity) o).id;
		}
		return false;
	}
}
