package org.icechamps.lava.interfaces;

import org.icechamps.lava.callback.Func;

import java.util.Collection;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 11:48 PM
 *
 * @param <T> The type of the object in the collection
 */
public interface LavaCollection<T> extends Collection<T> {
    /**
     * Creates a collection of objects of type {@code T} such that each object is unique
     *
     * @return A collection of objects
     */
    public LavaCollection<T> distinct();

    /**
     * Transforms the contents of the current collection of type {@code K} using the {@code callback}
     * function into a container of type {@code K} instance containing objects of type {@code E}
     *
     * @param func The callback that transforms the objects
     * @param <E>  The type of the transformed objects
     * @return A collection of transformed objects
     */
    public <E> LavaCollection<E> select(Func<T, E> func);

    /**
     * Searches the current collection using the given {@link org.icechamps.lava.callback.Func callback} function.
     *
     * @param func The callback function to search with
     * @return A subset of the list where all of the objects return a match in the callback function.
     */
    public LavaCollection<T> where(Func<T, Boolean> func);
}
