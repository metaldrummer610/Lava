package org.icechamps.lava.interfaces;

import org.icechamps.lava.callback.MatchOneCallback;
import org.icechamps.lava.callback.SelectOneCallback;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 11:48 PM
 *
 * @param <T> The type of the object in the collection
 * @param <K> The type of collection
 */
public interface ILavaCollection<T, K> {
	/**
	 * Creates a collection of objects of type {@code T} such that each object is unique
	 *
	 * @return A collection of objects
	 */
	public K distinct();

	/**
	 * Transforms the contents of the current container of type {@code K} using the {@code callback}
	 * function into a container of type {@code K} instance containing objects of type {@code E}
	 *
	 * @param callback The callback that transforms the objects
	 * @param <E>      The type of the transformed objects
	 * @return A collection of transformed objects
	 */
	public <E> K select(SelectOneCallback<T, E> callback);

	/**
	 * Searches a {@link java.util.List list} using the given {@link org.icechamps.lava.callback.MatchOneCallback callback} function.
	 *
	 * @param callback The callback function to search with
	 * @return A subset of the list where all of the objects return a match in the callback function.
	 */
	public K where(MatchOneCallback<T> callback);
}
