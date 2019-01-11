package boron.bstone.util.listener;

import java.util.Vector;
import java.util.function.BiConsumer;

/**
 * Created by Andy on 5/22/17.
 */
public class Listenable<T>
{
	public enum Phase
	{
		EARLY,
		DEFAULT,
		LATE;
	}

	private Vector<T> early;
	private Vector<T> main;
	private Vector<T> late;
	private BiConsumer<T, Object[]> notifyFunction;

	/** Construct an Listenable with zero Listeners. */
	public Listenable(BiConsumer<T, Object[]> notifyFunction)
	{
		early = new Vector<>();
		main = new Vector<>();
		late = new Vector<>();
		this.notifyFunction = notifyFunction;
	}

	/**
	 * Adds a listener to the set of listeners for this object, provided
	 * that it is not the same as some listener already in the set.
	 * The order in which notifications will be delivered to multiple
	 * listeners is specified by phase.
	 *
	 * @param o a listener to be added.
	 * @param phase the phase to listen to
	 *
	 * @throws NullPointerException if the parameter o is null.
	 */
	public synchronized void addListener(T o, Phase phase)
	{
		if (o == null) throw new NullPointerException();
		if (main.contains(o) || early.contains(o) || late.contains(o)) return;

		switch (phase)
		{
			case EARLY:
				early.addElement(o);
				break;
			case LATE:
				late.addElement(o);
				break;
			case DEFAULT:
			default:
				main.addElement(o);
				break;
		}
	}

	/**
	 * Adds a listener to the set of listeners for this object, provided
	 * that it is not the same as some listener already in the set.
	 * The order in which notifications will be delivered to multiple
	 * listeners is not specified.
	 *
	 * @param o a listener to be added.
	 *
	 * @throws NullPointerException if the parameter o is null.
	 */
	public synchronized void addListener(T o)
	{
		if (o == null) throw new NullPointerException();
		if (!main.contains(o))
		{
			main.addElement(o);
		}
	}

	/**
	 * Deletes a listener from the set of listeners of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 *
	 * @param o the listener to be deleted.
	 */
	public synchronized void deleteListener(T o)
	{
		if (main.contains(o))
		{
			main.removeElement(o);
		}
		else if (early.contains(o))
		{
			early.removeElement(o);
		}
		else if (late.contains(o))
		{
			late.removeElement(o);
		}
	}

	/**
	 * Notify all of its listeners
	 * <p>
	 * Each listener has its <code>update</code> method called with two
	 * arguments: this observable object and the <code>arg</code> argument.
	 *
	 * @param args any object.
	 */
	@SuppressWarnings("unchecked")
	public void notifyListeners(Object... args)
	{
	    /*
	     * a temporary array buffer, used as a snapshot of the state of
         * current Listeners.
         */
		Object[] arrLocal;

		synchronized (this)
		{
			arrLocal = new Object[this.countListeners()];
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
			this.notifyFunction.accept((T) arrLocal[i], args);
		}
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
}
