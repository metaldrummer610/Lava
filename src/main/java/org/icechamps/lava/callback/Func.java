package org.icechamps.lava.callback;

/**
 * User: Robert.Diaz
 * Date: 3/2/13
 * Time: 10:43 AM
 *
 * @param <A> The type of the object that is passed to the callback function
 * @param <T> The type of the object that is returned from the callback function
 */
public interface Func<A, T> {

    /**
     * Generic callback function that takes one argument
     *
     * @param a The object to operate on
     * @return The result of the operation
     */
    public T callback(A a);
}
