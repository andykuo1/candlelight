package org.bstone.util.listener;

import java.util.Vector;

/**
 * Created by Andy on 5/22/17.
 */
public class Listenable
{
	public enum Phase
	{
		EARLY,
		DEFAULT,
		LATE;
	}

	private final Object handler;
	private final Vector<Listener> early;
	private final Vector<Listener> main;
	private final Vector<Listener> late;

	/** Construct an Listenable with zero Listeners. */
	public Listenable(Object handler)
	{
		this.early = new Vector<>();
		this.main = new Vector<>();
		this.late = new Vector<>();
		this.handler = handler;
	}

	/**
	 * Adds a listener to the set of listeners for this object, provided
	 * that it is not the same as some listener already in the set.
	 * The order in which notifications will be delivered to multiple
	 * listeners is specified by phase.
	 *
	 * @param l a listener to be added.
	 * @param phase the phase to listen to
	 *
	 * @throws NullPointerException if the parameter l is null.
	 */
	public synchronized void addListener(Listener l, Phase phase)
	{
		if (l == null) throw new NullPointerException();
		if (main.contains(l) || early.contains(l) || late.contains(l)) return;

		switch (phase)
		{
			case EARLY:
				early.addElement(l);
				break;
			case LATE:
				late.addElement(l);
				break;
			case DEFAULT:
			default:
				main.addElement(l);
				break;
		}
	}

	/**
	 * Adds a listener to the set of listeners for this object, provided
	 * that it is not the same as some listener already in the set.
	 * The order in which notifications will be delivered to multiple
	 * listeners is not specified.
	 *
	 * @param l a listener to be added.
	 *
	 * @throws NullPointerException if the parameter l is null.
	 */
	public synchronized void addListener(Listener l)
	{
		if (l == null) throw new NullPointerException();
		if (!main.contains(l))
		{
			main.addElement(l);
		}
	}

	/**
	 * Deletes a listener from the set of listeners of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 *
	 * @param l the listener to be deleted.
	 */
	public synchronized void deleteListener(Listener l)
	{
		if (main.contains(l))
		{
			main.removeElement(l);
		}
		else if (early.contains(l))
		{
			early.removeElement(l);
		}
		else if (late.contains(l))
		{
			late.removeElement(l);
		}
	}

	/**
	 * Notify all of its listeners
	 * <p>
	 * Each listener has its <code>update</code> method called with two
	 * arguments: this observable object and the <code>arg</code> argument.
	 *
	 * @param arg any object.
	 */
	@SuppressWarnings("unchecked")
	public void notifyListeners(Object arg)
	{
	    /*
	     * a temporary array buffer, used as a snapshot of the state of
         * current Listeners.
         */
		Listener[] arrLocal;

		synchronized (this)
		{
			arrLocal = new Listener[this.countListeners()];
			int j = 0;
			for(int i = late.size() - 1; i >= 0; --i)
			{
				arrLocal[j++] = late.get(i);
			}
			for(int i = main.size() - 1; i >= 0; --i)
			{
				arrLocal[j++] = main.get(i);
			}
			for(int i = early.size() - 1; i >= 0; --i)
			{
				arrLocal[j++] = early.get(i);
			}
		}

		for (int i = arrLocal.length - 1; i >= 0; i--)
		{
			arrLocal[i].update(this, arg);
		}
	}

	/**
	 * Notify all of its listeners without an argument
	 */
	public void notifyListeners()
	{
		this.notifyListeners(null);
	}

	/**
	 * Clears the listener list so that this object no longer has any listeners.
	 */
	public synchronized void deleteListeners()
	{
		main.removeAllElements();
		early.removeAllElements();
		late.removeAllElements();
	}

	/**
	 * Returns the number of listeners of this <tt>Listenable</tt> object.
	 *
	 * @return the number of listeners of this object.
	 */
	public synchronized int countListeners()
	{
		return main.size() + early.size() + late.size();
	}

	public Object getHandler()
	{
		return this.handler;
	}
}
