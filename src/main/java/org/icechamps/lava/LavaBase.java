package org.icechamps.lava;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.*;
import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.collection.LavaMap;
import org.icechamps.lava.collection.LavaSet;
import org.icechamps.lava.exception.MultipleElementsFoundException;

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
     * @param callback   The callback that aggregates the objects
     * @param <T>        The type of the object in the collection
     * @param <V>        The type of the object to return
     * @return The aggregated object from the collection
     */
    protected <T, V> V aggregate(Collection<T> collection, AggregateOneCallback<T, V> callback) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);

        V ret = null;
        for (T t : collection) {
            ret = callback.aggregate(ret, t);
        }

        return ret;
    }

    /**
     * Aggregates the keys of a map using the callback function
     *
     * @param map      The map containing the keys
     * @param callback The callback that aggregates the keys
     * @param <T>      The return type of the aggregated object
     * @param <K>      The type of the key
     * @param <V>      The type of the value
     * @return The aggregated object from the map
     */
    protected <T, K, V> T aggregateKeys(Map<K, V> map, AggregateOneCallback<K, T> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        T ret = null;
        for (K k : map.keySet()) {
            ret = callback.aggregate(ret, k);
        }

        return ret;
    }

    /**
     * Aggregates the values of a map using the callback function
     *
     * @param map      The map containing the values
     * @param callback The callback that aggregates the values
     * @param <T>      The return type of the aggregated object
     * @param <K>      The type of the key
     * @param <V>      The type of the value
     * @return The aggregated object from the map
     */
    protected <T, K, V> T aggregateValues(Map<K, V> map, AggregateOneCallback<V, T> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        T ret = null;
        for (V v : map.values()) {
            ret = callback.aggregate(ret, v);
        }

        return ret;
    }

    ///////////////
    // All
    ///////////////

    /**
     * Iterates over the given collection and checks each element using the callback.
     *
     * @param collection The collection to search
     * @param callback   The callback function to use on the collection's elements
     * @param <T>        The type of element in the collection
     * @return Returns true if all of the elements return true for the callback. Returns false if a single element doesn't match.
     */
    protected <T> boolean all(Collection<T> collection, MatchOneCallback<T> callback) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);

        for (T obj : collection) {
            if (!callback.matches(obj))
                return false;
        }

        return true;
    }

    /**
     * Iterates over the given map and checks each element using the callback.
     *
     * @param map      The map to search
     * @param callback The callback used to test each pair
     * @param <K>      The type of the key
     * @param <V>      The type of the value
     * @return Returns true if all pairs pass the callback function. Returns false if a single pair fails the test.
     */
    protected <K, V> boolean all(Map<K, V> map, MatchTwoCallback<K, V> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        for (Map.Entry<K, V> pair : map.entrySet()) {
            if (!callback.matches(pair.getKey(), pair.getValue()))
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

    /**
     * Checks if there are any elements in the map.
     *
     * @param map The map to check
     * @param <K> The type of the key
     * @param <V> The type of the value
     * @return True if there are any elements in the map, false if not.
     */
    protected <K, V> boolean any(Map<K, V> map) {
        Preconditions.checkArgument(map != null);
        return !map.isEmpty();
    }

    ///////////////
    // Distinct
    ///////////////

    /**
     * Returns a list containing only distinct elements.
     *
     * @param list The list to search
     * @param <T>  The type of the object
     * @return A subset of the inital list containing no duplicates.
     */
    protected <T> LavaList<T> distinct(List<T> list) {
        return distinctInternalList(list, LavaList.class);
    }

    /**
     * Returns a set containing only distinct elements. This is pretty much a no-op because Sets do this by default,
     * so we just wrap the set in a LavaSet.
     *
     * @param set The set to search
     * @param <T> The type of the object in the set.
     * @return The initial set.
     */
    protected <T> LavaSet<T> distinct(Set<T> set) {
        return new LavaSet<T>(set);
    }

    /**
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    protected <K, V> LavaMap<K, V> distinct(Map<K, V> map) {
        return distinctInternalMap(map, LavaFlags.KEY);
    }

    /**
     * @param map
     * @param flags
     * @param <K>
     * @param <V>
     * @return
     */
    protected <K, V> LavaMap<K, V> distinct(Map<K, V> map, LavaFlags flags) {
        return distinctInternalMap(map, flags);
    }

    /**
     * @param collection
     * @param <T>
     * @param <K>
     * @return
     */
    private <T, K extends Collection<T>, V extends Collection<T>> V distinctInternalList(K collection, Class<V> clazz) {
        Preconditions.checkArgument(collection != null);

        V ret = null;
        try {
            ret = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        for (T obj : collection) {
            ret.add(obj);
        }

        return ret;
    }

    /**
     * @param map
     * @param flags
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> LavaMap<K, V> distinctInternalMap(Map<K, V> map, LavaFlags flags) {
        Preconditions.checkArgument(map != null);

        LavaMap<K, V> ret = new LavaMap<K, V>();
        for (Map.Entry<K, V> pair : map.entrySet()) {
            if (flags == LavaFlags.KEY
                    ? !ret.containsKey(pair.getKey())
                    : !ret.containsValue(pair.getValue()))
                ret.put(pair.getKey(), pair.getValue());
        }

        return ret;
    }

    ///////////////
    // First
    ///////////////

    protected <T> T first(Collection<T> collection) {
        return collection.iterator().next();
    }

    protected <T> T first(Collection<T> collection, MatchOneCallback<T> callback) {
        Preconditions.checkArgument(collection != null);

        for (T t : collection) {
            if(callback.matches(t))
                return t;
        }

        throw new NoSuchElementException();
    }

    ///////////////
    // Order By
    ///////////////

    /**
     * Orders the given list using a default comparator
     *
     * @param list The list to order
     * @param <T>  The type of the object in the list
     * @return The sorted list
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderBy(List<T> list) {
        return orderByListInternal(list, null);
    }

    /**
     * Orders the given list using the given comparator
     *
     * @param list       The list to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the list
     * @return The sorted list
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderBy(List<T> list, Comparator<T> comparator) {
        return orderByListInternal(list, comparator);
    }

    /**
     * Orders the given set using a default comparator
     *
     * @param set The set to order
     * @param <T> The type of the object in the set
     * @return The sorted set
     */
    protected <T extends Comparable<? super T>> LavaSet<T> orderBy(Set<T> set) {
        return orderBySetInternal(set, null);
    }

    /**
     * Orders the given set using the given comparator
     *
     * @param set        The set to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the set
     * @return The ordered set
     */
    protected <T extends Comparable<? super T>> LavaSet<T> orderBy(Set<T> set, Comparator<T> comparator) {
        return orderBySetInternal(set, comparator);
    }

    /**
     * Internal function that orders the given list using the given comparator. The comparator can be null.
     *
     * @param list       The list to order
     * @param comparator The comparator to use (can be null)
     * @param <T>        The type of the object in the list
     * @return The ordered list
     */
    private <T extends Comparable<? super T>> LavaList<T> orderByListInternal(List<T> list, Comparator<T> comparator) {
        Preconditions.checkArgument(list != null);

        if (comparator != null)
            Collections.sort(list, comparator);
        else
            Collections.sort(list);

        if (!(list instanceof LavaList))
            return new LavaList<T>(list);
        else
            return (LavaList<T>) list;
    }

    /**
     * Internal function that orders the given set using the given comparator. The comparator can be null.
     *
     * @param set        The set to order
     * @param comparator The comparator to use (can be null)
     * @param <T>        The type of the object in the set
     * @return The ordered set
     */
    private <T extends Comparable<? super T>> LavaSet<T> orderBySetInternal(Set<T> set, Comparator<T> comparator) {
        Preconditions.checkArgument(set != null);

        SortedSet<T> sortedSet;

        if (comparator != null) {
            sortedSet = new TreeSet<T>(comparator);
            sortedSet.addAll(set);
        } else {
            sortedSet = new TreeSet<T>(set);
        }

        return new LavaSet<T>(sortedSet);
    }

    ///////////////
    // Select
    ///////////////

    /**
     * Transforms the contents of {@code list} using the {@code callback} function into a {@link LavaList} instance containing objects of type {@code E}
     *
     * @param list     The source list
     * @param callback The callback that transforms the objects
     * @param <T>      The type of the original objects
     * @param <E>      The type of the transformed objects
     * @return A collection of transformed objects
     */
    protected <T, E> LavaList<E> select(List<T> list, SelectOneCallback<T, E> callback) {
        return selectInternalList(list, callback, LavaList.class);
    }

    /**
     * Transforms the contents of {@code set} using the {@code callback} function into a {@link LavaSet} instance containing objects of type {@code E}
     *
     * @param set      The source set
     * @param callback The callback that transforms the objects
     * @param <T>      The type of the original objects
     * @param <E>      The type of the transformed objects
     * @return A collection of transformed objects
     */
    protected <T, E> LavaSet<E> select(Set<T> set, SelectOneCallback<T, E> callback) {
        return selectInternalList(set, callback, LavaSet.class);
    }

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
    protected <K, V, T> LavaMap<K, T> select(Map<K, V> map, SelectTwoCallback<K, V, T> callback) {
        return selectInternalMap(map, callback);
    }

    /**
     * Internal method used to transform the contents of one collection into another type and put those objects into a new collection
     *
     * @param collection The source collection
     * @param callback   The callback used to transform the objects in the source collection
     * @param clazz      The class of the resulting container
     * @param <T>        The type of the source object
     * @param <E>        The type of the transformed object
     * @param <K>        The type of the source collection
     * @param <V>        The type of the transformed collection
     * @return A new collection containing the transformed variations of the source objects
     */
    private <T, E, K extends Collection<T>, V extends Collection<E>> V selectInternalList(K collection, SelectOneCallback<T, E> callback, Class<V> clazz) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);
        Preconditions.checkArgument(clazz != null);

        V ret = null;
        try {
            ret = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        for (T obj : collection) {
            E transformed = callback.select(obj);
            ret.add(transformed);
        }

        return ret;
    }

    /**
     * Internal method used to transform the values of one map into another type and put those objects into a new map with the old keys
     *
     * @param map      The source map
     * @param callback The callback function used to transform the values in the map
     * @param <T>      The type of the source object
     * @param <K>      The type of the key in the map
     * @param <V>      The type of the transformed object
     * @return A new map containing the original keys with their newly transformed objects
     */
    private <K, V, T> LavaMap<K, T> selectInternalMap(Map<K, V> map, SelectTwoCallback<K, V, T> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        LavaMap<K, T> ret = new LavaMap<K, T>();

        for (Map.Entry<K, V> pair : map.entrySet()) {
            T transformed = callback.select(pair.getKey(), pair.getValue());
            ret.put(pair.getKey(), transformed);
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
     * @param callback   The callback used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function.
     * @throws NoSuchElementException         If no element in the collection returns true for the callback.
     * @throws MultipleElementsFoundException If there are multiple elements that match the callback.
     */
    protected <T> T single(Collection<T> collection, MatchOneCallback<T> callback) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);

        T ret = null;

        for (T t : collection) {
            if (callback.matches(t)) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = t;
            }
        }

        if (ret == null)
            throw new NoSuchElementException("Element not found");

        return ret;
    }

    /**
     * Searches for a single element that matches using the callback function. If there are multiple elements that match, an exception is throw.
     *
     * @param map      The map to search
     * @param callback The callback used to search the map
     * @param <K>      The type of the key
     * @param <V>      The type of the value
     * @return The single element that matches using the callback function.
     * @throws NoSuchElementException         If no element in the map returns true for the callback.
     * @throws MultipleElementsFoundException If there are multiple elements that match the callback.
     */
    protected <K, V> V single(Map<K, V> map, MatchTwoCallback<K, V> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        V ret = null;

        for (Map.Entry<K, V> pair : map.entrySet()) {
            if (callback.matches(pair.getKey(), pair.getValue())) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = pair.getValue();
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
     * @param callback   The callback used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function or null if none are found.
     * @throws MultipleElementsFoundException If there are multiple elements that match the callback.
     */
    protected <T> T singleOrDefault(Collection<T> collection, MatchOneCallback<T> callback) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);

        T ret = null;

        for (T t : collection) {
            if (callback.matches(t)) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = t;
            }
        }

        return ret;
    }

    /**
     * Searches for a single element that matches using the callback function.
     * If there are multiple elements that match, an exception is throw.
     * If there are no elements that match, null is returned.
     *
     * @param map      The map to search
     * @param callback The callback used to search the map
     * @param <K>      The type of the key
     * @param <V>      The type of the value
     * @return The single element that matches using the callback function or null if none are found.
     * @throws MultipleElementsFoundException If there are multiple elements that match the callback.
     */
    protected <K, V> V singleOrDefault(Map<K, V> map, MatchTwoCallback<K, V> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        V ret = null;

        for (Map.Entry<K, V> pair : map.entrySet()) {
            if (callback.matches(pair.getKey(), pair.getValue())) {
                if (ret != null)
                    throw new MultipleElementsFoundException();

                ret = pair.getValue();
            }
        }

        return ret;
    }

    ///////////////
    // Where
    ///////////////

    /**
     * Searches a {@link java.util.List list} using the given {@link org.icechamps.lava.callback.MatchOneCallback callback} function.
     *
     * @param list     The list to search through
     * @param callback The callback function to search with
     * @param <T>      The type of the object in the list
     * @return A subset of the list where all of the objects return a match in the callback function.
     */
    protected <T> LavaList<T> where(List<T> list, MatchOneCallback<T> callback) {
        return whereInternalList(list, callback, LavaList.class);
    }

    /**
     * Searches a {@link java.util.List list} using the given {@link org.icechamps.lava.callback.MatchOneCallback callback} function.
     *
     * @param set
     * @param callback
     * @param <T>
     * @return
     */
    protected <T> LavaSet<T> where(Set<T> set, MatchOneCallback<T> callback) {
        return whereInternalList(set, callback, LavaSet.class);
    }

    /**
     * @param map
     * @param callback
     * @param <K>
     * @param <V>
     * @return
     */
    protected <K, V> LavaMap<K, V> where(Map<K, V> map, MatchTwoCallback<K, V> callback) {
        return whereInternalMap(map, callback);
    }

    /**
     * The internal function that searches through the collection and returns a subset of the collection whose elements pass the callback's test.
     *
     * @param collection The collection to search through
     * @param callback   The callback function used to test each object
     * @param clazz      The class of the resulting collection
     * @param <T>        The type of object in the collection
     * @param <K>        The type of the collection
     * @param <V>        The type of the resulting collection
     * @return A subset of the collection whose elements pass the callback's test.
     */
    private <T, K extends Collection<T>, V extends Collection<T>> V whereInternalList(K collection, MatchOneCallback<T> callback, Class<V> clazz) {
        Preconditions.checkArgument(collection != null);
        Preconditions.checkArgument(callback != null);
        Preconditions.checkArgument(clazz != null);

        V ret = null;
        try {
            ret = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }

        for (T obj : collection) {
            if (callback.matches(obj))
                ret.add(obj);
        }

        return ret;
    }

    /**
     * @param map
     * @param callback
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> LavaMap<K, V> whereInternalMap(Map<K, V> map, MatchTwoCallback<K, V> callback) {
        Preconditions.checkArgument(map != null);
        Preconditions.checkArgument(callback != null);

        LavaMap<K, V> ret = new LavaMap<K, V>();

        for (Map.Entry<K, V> pair : map.entrySet()) {
            if (callback.matches(pair.getKey(), pair.getValue()))
                ret.put(pair.getKey(), pair.getValue());
        }

        return ret;
    }


    //TODO: Add the following methods: First, FirstOrDefault, Intersect, Join, Last, LastOrDefault, Max, Min, OrderByDescending, SelectMany, SequenceEqual, Skip, SkipWhile, Sum, Take, TakeWhile, ThenBy, ToMap, ToList, ToSet, Union, Zip

}
