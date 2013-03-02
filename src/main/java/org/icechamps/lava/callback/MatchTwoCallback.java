package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 2:52 PM
 * <p/>
 * A callback function that is used when matching elements in a {@link java.util.Map map} instance.
 * <p/>
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public interface MatchTwoCallback<K, V> {
	/**
	 * Determines if the supplied key/value pair should be included in the resulting map or not.
	 *
	 * @param key   The key of the current object
	 * @param value The value of the current object
	 * @return True if the the key/value pair should be added to the resulting map, false if it should be ignored
	 */
	public boolean matches(K key, V value);
}
