package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 3/1/13
 * Time: 5:49 PM
 *
 * @param <T> The source type
 * @param <V> The destination type
 */
public interface AggregateOneCallback<T, V> {

    /**
     * Used in the aggregation function to accumulate values of type T into a single instance of T
     *
     * @param dest The accumulation of objects so far
     * @param next The next object in the collection
     * @return The object that is the result of the accumulation
     */
    public V aggregate(V dest, T next);
}
