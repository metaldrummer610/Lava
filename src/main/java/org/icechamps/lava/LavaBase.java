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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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
        Preconditions.checkNotNull(collection);
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
        Preconditions.checkNotNull(collection);

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
        Preconditions.checkNotNull(collection);

        if (collection.isEmpty())
            throw new NoSuchElementException("The collection is empty");

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
        Preconditions.checkNotNull(collection);

        for (T t : collection) {
            if (func.callback(t))
                return t;
        }

        throw new NoSuchElementException("No element found that matches the callback function");
    }

    ///////////////
    // First Or Default
    ///////////////

    /**
     * Returns the first item in the collection, or null if there isn't one. Throws an exception if the collection is empty
     *
     * @param collection The collection to search
     * @param <T>        The type of the object in the collection
     * @return The first item in the collection, or null.
     */
    protected <T> T firstOrDefault(Collection<T> collection) {
        return first(collection);
    }

    /**
     * Returns the first item in the collection using the callback function, or null if there isn't one.
     *
     * @param collection The collection to use
     * @param func       The callback function to use
     * @param <T>        The type of the object
     * @return The first item in the collection, or null.
     */
    protected <T> T firstOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        if (collection.isEmpty()) {
            throw new NoSuchElementException("The collection is empty");
        }

        for (T t : collection) {
            if (func.callback(t))
                return t;
        }

        return null;
    }

    ///////////////
    // Intersect
    ///////////////

    /**
     * Creates an intersection between the two collections. The resulting LavaCollection implementation will be of the same type as the first collection.
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of object in the collections
     * @return The intersection of the two collections
     */
    protected <T> LavaCollection<T> intersect(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        LavaCollection<T> firstDistinct = distinct(first);
        LavaCollection<T> secondDistinct = distinct(second);

        LavaCollection<T> ret = buildLavaCollectionFromCollection(first);

        for (T f : firstDistinct) {
            for (T s : secondDistinct) {
                if (f.equals(s))
                    ret.add(f);
            }
        }

        return ret;
    }

    ///////////////
    // Join
    ///////////////

    //TODO: Finish the implementation of this method....

    /**
     * Joins the two collections on
     *
     * @param outerCollection
     * @param innerCollection
     * @param outerKeyFunc
     * @param innerKeyFunc
     * @param resultFunc
     * @param <Outer>
     * @param <Inner>
     * @param <Key>
     * @param <Result>
     * @return
     */
    protected <Outer, Inner, Key, Result> LavaCollection<Result> join(Collection<Outer> outerCollection,
                                                                      Collection<Inner> innerCollection,
                                                                      Func<Outer, Key> outerKeyFunc,
                                                                      Func<Inner, Key> innerKeyFunc,
                                                                      Func2<Outer, Inner, Result> resultFunc) {
        Preconditions.checkArgument(outerCollection != null);
        Preconditions.checkArgument(innerCollection != null);
        Preconditions.checkArgument(outerKeyFunc != null);
        Preconditions.checkArgument(innerKeyFunc != null);
        Preconditions.checkArgument(resultFunc != null);

        LavaCollection<Result> results = buildLavaCollectionFromCollection(outerCollection);
        ArrayList<JoinedKey> outerKeys = new ArrayList<JoinedKey>();
        ArrayList<JoinedKey> innerKeys = new ArrayList<JoinedKey>();

        // Collect the keys from each collection
        for (Outer outer : outerCollection) {
            Key key = outerKeyFunc.callback(outer);
            outerKeys.add(new JoinedKey(key, outer));
        }

        for (Inner inner : innerCollection) {
            Key key = innerKeyFunc.callback(inner);
            innerKeys.add(new JoinedKey(key, inner));
        }

        Iterator<JoinedKey> outerIterator = outerKeys.iterator();
        Iterator<JoinedKey> innerIterator = innerKeys.iterator();

//        while (outerIterator.hasNext() && innerIterator.hasNext()) {
//            results.add(resultFunc.callback(outerIterator.next(), innerIterator.next()));
//        }

        return results;
    }

    private class JoinedKey<K, V> {
        public K key;
        public V value;

        public JoinedKey(K k, V v) {
            key = k;
            value = v;
        }
    }

    ///////////////
    // Last
    ///////////////

    /**
     * Obtains the last element in the collection
     *
     * @param collection The collection to search
     * @param <T>        The type of the object
     * @return The last element in the collection
     */
    protected <T> T last(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        if (collection.isEmpty())
            throw new NoSuchElementException("Collection is empty");

        Object[] elements = collection.toArray();
        return (T) elements[collection.size() - 1];
    }

    /**
     * Obtains the last element in the collection that satisfies the given callback function
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the elements in the collection
     * @return The last element in the list that satisfies the callback
     * @throws NoSuchElementException If no elements are found
     */
    protected <T> T last(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        T ret = null;
        for (T t : collection) {
            if (func.callback(t))
                ret = t;
        }

        if (ret == null)
            throw new NoSuchElementException("Element not found");

        return ret;
    }

    ///////////////
    // Last Or Default
    ///////////////

    /**
     * Obtains the last element in the collection, or returns null.
     *
     * @param collection The collection to search
     * @param <T>        The type of the element
     * @return The last element or null
     */
    protected <T> T lastOrDefault(Collection<T> collection) {
        return last(collection);
    }

    /**
     * Obtains the last element in the collection that satisfies the given callback function, or null
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the element
     * @return The last element to satisfy the callback function, or null
     */
    protected <T> T lastOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        T ret = null;
        for (T t : collection) {
            if (func.callback(t))
                ret = t;
        }

        return ret;
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
        Preconditions.checkNotNull(collection);

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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

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

    //TODO: Add the following methods: Join, Max, Min, OrderByDescending, SelectMany, SequenceEqual, Skip, SkipWhile, Sum, Take, TakeWhile, ThenBy, ToMap, ToList, ToSet, Union, Zip

}
