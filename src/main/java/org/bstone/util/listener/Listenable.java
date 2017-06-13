package org.bstone.util.listener;

import java.util.Vector;
import java.util.function.BiConsumer;

/**
 * Created by Andy on 5/22/17.
 */
public class Listenable<T>
{
	private Vector<T> lis;
	private BiConsumer<T, Object[]> notifyFunction;

	/** Construct an Listenable with zero Listeners. */
	public Listenable(BiConsumer<T, Object[]> notifyFunction)
	{
		lis = new Vector<>();
		this.notifyFunction = notifyFunction;
	}

	/**
	 * Adds an listener to the set of listeners for this object, provided
	 * that it is not the same as some listener already in the set.
	 * The order in which notifications will be delivered to multiple
	 * listeners is not specified. See the class comment.
	 *
	 * @param o an listener to be added.
	 *
	 * @throws NullPointerException if the parameter o is null.
	 */
	public synchronized void addListener(T o)
	{
		if (o == null)
			throw new NullPointerException();
		if (!lis.contains(o))
		{
			lis.addElement(o);
		}
	}

	/**
	 * Deletes an listener from the set of listeners of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 *
	 * @param o the listener to be deleted.
	 */
	public synchronized void deleteListener(T o)
	{
		lis.removeElement(o);
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
			arrLocal = lis.toArray();
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
		lis.removeAllElements();
	}

	/**
	 * Returns the number of listeners of this <tt>Listenable</tt> object.
	 *
	 * @return the number of listeners of this object.
	 */
	public synchronized int countListeners()
	{
		return lis.size();
	}
}
