package boron.base.material;

import boron.base.component.ComponentManager;

import java.util.Collection;

/**
 * Created by Andy on 6/8/17.
 */
@Deprecated
public class OldMaterialManager extends ComponentManager<OldMaterial, OldProperty>
{
	public OldMaterialManager()
	{
		super(OldProperty.class);
	}

	public OldMaterial createMaterial(OldProperty... properties)
	{
		return this.addEntity(new OldMaterial(), properties);
	}

	public OldProperty getDefaultProperty(Class<? extends OldProperty> componentType)
	{
		Collection<OldProperty> collection = this.getComponentMap(componentType).values();
		if (collection.isEmpty())
		{
			throw new IllegalStateException("Default property for " + componentType + " does not exist!");
		}

		return collection.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends OldProperty> T getComponentByEntity(Class<T> componentType, OldMaterial entity)
	{
		T t = super.getComponentByEntity(componentType, entity);
		if (t != null) return t;
		if (!entity.allowDefaultProperty(componentType)) return null;

		return (T) this.getDefaultProperty(componentType);
	}
}
