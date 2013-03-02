package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 4:20 PM
 *
 * @param <T> The type of the object
 */
public interface MatchOneCallback<T> {

	/**
	 * Used in where calls to determine if the passed in object should be returned as a part of the collection.
	 *
	 * @param object The object to test
	 * @return True if the object should be included in the resulting collection, false if it should be ignored.
	 */
	public boolean matches(T object);
}
