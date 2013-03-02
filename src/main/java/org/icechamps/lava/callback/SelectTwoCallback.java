package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 4:18 PM
 *
 * @param <K> The type of the key
 * @param <V> The type of the source object
 * @param <T> The type of the transformed object
 */
public interface SelectTwoCallback<K, V, T> {

	/**
	 * Used to transform a map's key/value pair's value to another type
	 *
	 * @param key   The key for the pair
	 * @param value The value for the pair
	 * @return The newly transformed object that will replace the original value
	 */
	public T select(K key, V value);
}
