package org.icechamps.lava.interfaces;

import org.icechamps.lava.LavaFlags;
import org.icechamps.lava.callback.MatchTwoCallback;
import org.icechamps.lava.callback.SelectTwoCallback;
import org.icechamps.lava.collection.LavaMap;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 11:52 PM
 */
public interface ILavaMap<K, V> {
	/**
	 * @param map
	 * @param callback
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public LavaMap<K, V> where(MatchTwoCallback<K, V> callback);

	/**
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public LavaMap<K, V> distinct();

	/**
	 * @param map
	 * @param flags
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public LavaMap<K, V> distinct(LavaFlags flags);

	/**
	 * Transforms the values of {@code map} using the {@code callback} function into a {@link LavaMap} instance containing objects of type {@code V}
	 *
	 * @param map      The source map
	 * @param callback The callback that transforms the objects
	 * @param <T>      The type of the original objects
	 * @param <K>      The type of the key
	 * @param <V>      The type of the transformed objects
	 * @return A map whose values are the transformed objects
	 */
	public <T> LavaMap<K, T> select(SelectTwoCallback<K, V, T> callback);
}
