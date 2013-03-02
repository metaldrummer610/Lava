package org.icechamps.lava;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.collection.LavaSet;
import org.icechamps.lava.exception.MultipleElementsFoundException;
import org.icechamps.lava.interfaces.LavaCollection;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 7:34 PM
 * <p/>
 * Contains all of the magic that makes this library happen
 */
public class LavaBase {

    ///////////////
    // Aggregate
    ///////////////

    /**
     * Aggregates the objects using the callback function
     *
     * @param collection The collection to iterate
     * @param func       The callback that aggregates the objects
     * @param <T>        The type of the object in the collection
     * @param <V>        The type of the object to return
     * @return The aggregated object from the collection
     */
    protected <T, V> V aggregate(Collection<T> collection, Func2<T, V, V> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        V ret = null;
        for (T t : collection) {
            ret = func.callback(t, ret);
        }

        return ret;
    }

    ///////////////
    // All
    ///////////////

    /**
     * Iterates over the given collection and checks each element using the callback function.
     *
     * @param collection The collection to search
     * @param func       The func function to use on the collection's elements
     * @param <T>        The type of element in the collection
     * @return Returns true if all of the elements return true for the func. Returns false if a single element doesn't match.
     */
    protected <T> boolean all(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        for (T obj : collection) {
            if (!func.callback(obj))
                return false;
        }

        return true;
    }

    ///////////////
    // Any
    ///////////////

    /**
     * Checks if there are any elements in the collection.
     *
     * @param collection The collection to check
     * @param <T>        The type of object in the collection
     * @return True if there are any elements in the collection, false if not.
     */
    protected <T> boolean any(Collection<T> collection) {
        Preconditions.checkArgument(collection != null);
        return !collection.isEmpty();
    }

    ///////////////
    // Distinct
    ///////////////

    /**
     * Returns a LavaCollection containing only distinct elements.
     *
     * @param collection The collection to search
     * @param <T>        The type of the object
     * @return A subset of the inital list containing no duplicates.
     */
    protected <T> LavaCollection<T> distinct(Collection<T> collection) {
        Preconditions.checkArgument(collection != null);

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);

        for (T obj : collection) {
            ret.add(obj);
        }

        return ret;
    }

    ///////////////
    // First
    ///////////////

    /**
     * Returns the first object in the collection.
     *
     * @param collection The collection to use
     * @param <T>        The type of the object in the collection
     * @return The first object in the collection
     */
    protected <T> T first(Collection<T> collection) {
        Preconditions.checkArgument(collection != null);

        if (collection.isEmpty()) {
            throw new NoSuchElementException("The collection is empty");
        }
        return collection.iterator().next();
    }

    /**
     * Returns the first object in the collection that matches the callback function. Throws an exception if nothing is found.
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the object in the collection
     * @return The first match the callback function finds
     */
    protected <T> T first(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkArgument(collection != null);

        for (T t : collection) {
            if (func.callback(t))
                return t;
        }

        throw new NoSuchElementException("No element found that matches the callback function");
    }

    ///////////////
    // Order By
    ///////////////

    /**
     * Orders the given collection using a default comparator
     *
     * @param collection The collection to order
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderBy(Collection<T> collection) {
        return orderByListInternal(collection, null);
    }

    /**
     * Orders the given collection using the given comparator
     *
     * @param collection The collection to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderBy(Collection<T> collection, Comparator<T> comparator) {
        return orderByListInternal(collection, comparator);
    }

    /**
     * Internal function that orders the given collection using the given comparator. The comparator can be null.
     *
     * @param collection The collection to order
     * @param comparator The comparator to use (can be null)
     * @param <T>        The type of the object in the collection
     * @return The ordered collection
     */
    private <T extends Comparable<? super T>> LavaList<T> orderByListInternal(Collection<T> collection, Comparator<T> comparator) {
        Preconditions.checkArgument(collection != null);

        LavaList<T> list = new LavaList<T>(collection);

        if (comparator != null)
            Collections.sort(list, comparator);
        else
            Collections.sort(list);

        return list;
    }

    ///////////////
    // Select
    ///////////////

    /**
     * Transforms the contents of {@code list} using the {@code func} function into a {@link LavaList} instance containing objects of type {@code E}
     *
     * @param collection The source list
     * @param func       The function that transforms the objects
     * @param <T>        The type of the original objects
     * @param <E>        The type of the transformed objects
     * @return A collection of transformed objects
     */
    protected <T, E> LavaCollection<E> select(Collection<T> collection, Func<T, E> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        LavaCollection<E> ret = buildLavaCollectionFromCollection(collection);

        for (T obj : collection) {
            E transformed = func.callback(obj);
            ret.add(transformed);
        }

        return ret;
    }

    ///////////////
    // Single
    ///////////////

    /**
     * Searches for a single element that matches using the callback function. If there are multiple objects that match, an exception is thrown.
     *
     * @param collection The collection to search
     * @param func       The function used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function.
     * @throws NoSuchElementException         If no element in the collection returns true for the func.
     * @throws MultipleElementsFoundException If there are multiple elements that match the func.
     */
    protected <T> T single(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        T ret = null;

        for (T t : collection) {
            if (func.callback(t)) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = t;
            }
        }

        if (ret == null)
            throw new NoSuchElementException("Element not found");

        return ret;
    }

    ///////////////
    // Single Or Default
    ///////////////

    /**
     * Searches for a single element that matches using the callback function.
     * If there are multiple objects that match, an exception is thrown.
     * If there are no elements that match, null is returned.
     *
     * @param collection The collection to search
     * @param func       The callback used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function or null if none are found.
     * @throws MultipleElementsFoundException If there are multiple elements that match the callback.
     */
    protected <T> T singleOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        T ret = null;

        for (T t : collection) {
            if (func.callback(t)) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = t;
            }
        }

        return ret;
    }

    ///////////////
    // Where
    ///////////////

    /**
     * Searches a {@link java.util.List list} using the given {@link Func callback} function.
     *
     * @param collection The list to search through
     * @param func       The callback function to search with
     * @param <T>        The type of the object in the list
     * @return A subset of the list where all of the objects return a match in the callback function.
     */
    protected <T> LavaCollection<T> where(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(func != null);

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);

        for (T obj : collection) {
            if (func.callback(obj))
                ret.add(obj);
        }

        return ret;
    }

    /**
     * Constructs a LavaCollection based on the given Collection. This way, we can maintain the type of object that has been given to us
     *
     * @param collection The collection to check
     * @param <T>        The type of object in the resulting LavaCollection
     * @return A new LavaCollection whose implementing type is the same as the collection
     */
    private <T> LavaCollection<T> buildLavaCollectionFromCollection(Collection collection) {
        LavaCollection<T> ret;
        if (collection instanceof List) {
            ret = new LavaList<T>();
        } else if (collection instanceof Set) {
            ret = new LavaSet<T>();
        } else {
            throw new UnsupportedOperationException("Collection is neither a list nor a set!");
        }

        return ret;
    }

    //TODO: Add the following methods: First, FirstOrDefault, Intersect, Join, Last, LastOrDefault, Max, Min, OrderByDescending, SelectMany, SequenceEqual, Skip, SkipWhile, Sum, Take, TakeWhile, ThenBy, ToMap, ToList, ToSet, Union, Zip

}
