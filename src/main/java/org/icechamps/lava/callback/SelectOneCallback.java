package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 3:58 PM
 *
 * @param <T> The type of the object that is passed in
 * @param <E> The type of the object we should return
 */
public interface SelectOneCallback<T, E> {
	public E select(T object);
}
