package net.jimboi.mod.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Andy on 5/14/17.
 */
public class ComponentHandler
{
	protected final List<Component> components = new ArrayList<>();

	public void setupComponents()
	{
		for(Component component : this.components)
		{
			component.onComponentSetup(this);
		}
	}

	public <T extends Component> T addComponent(int index, T component)
	{
		if (index >= this.components.size()) this.components.add(null);
		Component comp = this.components.set(index, component);
		if (comp != null) this.addComponent(index + 1, comp);
		return component;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> componentType)
	{
		for(Component comp : this.components)
		{
			if (componentType.isInstance(comp))
			{
				return (T) comp;
			}
		}

		throw new NoSuchElementException();
	}

	public Iterator<Component> getComponents()
	{
		return this.components.iterator();
	}
}
