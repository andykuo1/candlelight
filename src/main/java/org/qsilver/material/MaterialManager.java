package org.qsilver.material;

import org.bstone.component.ComponentManager;

import java.util.Collection;

/**
 * Created by Andy on 6/8/17.
 */
public class MaterialManager extends ComponentManager<Material, Property>
{
	public MaterialManager()
	{
		super(Property.class);
	}

	public Material createMaterial(Property... properties)
	{
		return this.addEntity(new Material(), properties);
	}

	public Property getDefaultProperty(Class<? extends Property> componentType)
	{
		Collection<Property> collection = this.getComponentMap(componentType).values();
		if (collection.isEmpty())
		{
			throw new IllegalStateException("Default property for " + componentType + " does not exist!");
		}

		return collection.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Property> T getComponentByEntity(Class<T> componentType, Material entity)
	{
		T t = super.getComponentByEntity(componentType, entity);
		if (t != null) return t;
		if (!entity.allowDefaultProperty(componentType)) return null;

		return (T) this.getDefaultProperty(componentType);
	}
}
