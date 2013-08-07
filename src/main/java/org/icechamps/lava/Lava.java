package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.interfaces.Enumerable;
import org.icechamps.lava.util.Group;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 4:12 PM
 * <p/>
 * Main class in the Lava library.
 * This allows static access to methods that act on standard java collection types.
 * Each method returns a {@link org.icechamps.lava.collection.LavaEnumerable} type so that method calls can be chained.
 */
public class Lava {
    private static LavaBase lavaBase = new LavaBase();

    /**
     * Aggregates the objects using the callback function
     *
     * @param collection The collection to iterate
     * @param func       The callback that aggregates the objects
     * @param <T>        The type of the object in the collection
     * @param <V>        The type of the object to return
     * @return The aggregated object from the collection
     */
    public static <T, V> V aggregate(Collection<T> collection, Func2<T, V, V> func) {
        return lavaBase.aggregate(collection, func);
    }

    /**
     * Iterates over the given collection and checks each element using the callback function.
     *
     * @param collection The collection to search
     * @param func       The func function to use on the collection's elements
     * @param <T>        The type of element in the collection
     * @return Returns true if all of the elements return true for the func. Returns false if a single element doesn't match.
     */
    public static <T extends Comparable<? super T>> boolean all(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.all(collection, func);
    }

    /**
     * Checks if there are any elements in the collection.
     *
     * @param collection The collection to check
     * @param <T>        The type of object in the collection
     * @return True if there are any elements in the collection, false if not.
     */
    public static <T extends Comparable<? super T>> boolean any(Collection<T> collection) {
        return lavaBase.any(collection);
    }

    /**
     * Averages the collection and returns the results
     *
     * @param collection The collection to average
     * @return The average values of the collection
     */
    public static <T extends Number> Number average(Collection<T> collection) {
        return lavaBase.average(collection);
    }

    /**
     * Casts the untyped collection to a typed Enumerable containing the elements of the collection.
     *
     * @param collection The collection who's elements will be cast
     * @param <T>        The type of object to be cast to
     * @return An Enumerable instance containing all of the elements of the collection in a typed manner
     */
    @SuppressWarnings("RedundantTypeArguments")
    public static <T extends Comparable<? super T>> Enumerable<T> cast(Collection collection) {
        // Dev note: The <T> is REQUIRED because of some type casting thing. Not sure why, but if you remove it, javac complains to no end with cryptic error messages
        return lavaBase.<T>cast(collection);
    }

    /**
     * Concatenates the two collections together to create a new Enumerable.
     *
     * @param first  The first collection to use
     * @param second The second collection to use
     * @param <T>    The type of the objects in the collections
     * @return A new Enumerable that contains all of the elements from both collections
     */
    public static <T extends Comparable<? super T>> Enumerable<T> concat(Collection<T> first, Collection<T> second) {
        return lavaBase.concat(first, second);
    }

    /**
     * Returns the number of elements in the collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return The number of elements in the collection
     */
    public static <T extends Comparable<? super T>> int count(Collection<T> collection) {
        return lavaBase.count(collection);
    }

    /**
     * Returns a Enumerable containing only distinct elements.
     *
     * @param collection The collection to search
     * @param <T>        The type of the object
     * @return A subset of the inital list containing no duplicates.
     */
    public static <T extends Comparable<? super T>> Enumerable<T> distinct(Collection<T> collection) {
        return lavaBase.distinct(collection);
    }

    /**
     * Returns the element in the collection at the specified index. Useful for collections that do not allow positional access.
     *
     * @param collection The collection to use
     * @param index      The index of the element
     * @param <T>        The type of the element
     * @return The element in the collection at the specified index
     */
    public static <T extends Comparable<? super T>> T elementAt(Collection<T> collection, int index) {
        return lavaBase.elementAt(collection, index);
    }

    /**
     * Returns the element in the collection at the specified index, or null if the index is out of bounds. Useful for collections that do not allow positional access.
     *
     * @param collection The collection to use
     * @param index      The index of the element
     * @param <T>        The type of the element
     * @return The element in the collection at the specified index or null if the index is out of bounds
     */
    public static <T extends Comparable<? super T>> T elementAtOrDefault(Collection<T> collection, int index) {
        return lavaBase.elementAtOrDefault(collection, index);
    }

    /**
     * Creates an enumerable containing the difference between the two collections
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of the object in the collection
     * @return The enumerable containing the difference
     */
    public static <T extends Comparable<? super T>> Enumerable<T> except(Collection<T> first, Collection<T> second) {
        return lavaBase.except(first, second);
    }

    /**
     * Returns the first object in the collection.
     *
     * @param collection The collection to use
     * @param <T>        The type of the object in the collection
     * @return The first object in the collection
     */
    public static <T extends Comparable<? super T>> T first(Collection<T> collection) {
        return lavaBase.first(collection);
    }

    /**
     * Returns the first object in the collection that matches the callback function. Throws an exception if nothing is found.
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the object in the collection
     * @return The first match the callback function finds
     */
    public static <T extends Comparable<? super T>> T first(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.first(collection, func);
    }

    /**
     * Returns the first item in the collection, or null if there isn't one. Throws an exception if the collection is empty
     *
     * @param collection The collection to search
     * @param <T>        The type of the object in the collection
     * @return The first item in the collection, or null.
     */
    public static <T extends Comparable<? super T>> T firstOrDefault(Collection<T> collection) {
        return lavaBase.firstOrDefault(collection);
    }

    /**
     * Returns the first item in the collection using the callback function, or null if there isn't one.
     *
     * @param collection The collection to use
     * @param func       The callback function to use
     * @param <T>        The type of the object
     * @return The first item in the collection, or null.
     */
    public static <T extends Comparable<? super T>> T firstOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.firstOrDefault(collection, func);
    }

    /**
     * Groups the elements in the collection using the keys that are generated by the key function.
     *
     * @param collection The collection to group
     * @param keyFunc    The function used to generate keys
     * @param <T>        The type of the object in the collection
     * @param <K>        The type of the key
     * @return An Enumerable instance that contains the elements of collection grouped by the keys that were generated by the callback function
     */
    public static <T, K extends Comparable<? super K>> Enumerable<Group<K, T>> groupBy(Collection<T> collection, Func<T, K> keyFunc) {
        return lavaBase.groupBy(collection, keyFunc);
    }

    /**
     * Groups the elements in the collection using keys that are generated by the key function and the values the value function generates
     *
     * @param collection The source collection
     * @param keyFunc    The function used to generate the keys
     * @param valueFunc  The function used to generate the values
     * @param <T>        The type of object in the collection
     * @param <K>        The type of the key
     * @param <V>        The type of the value
     * @return An Enumerable instance containing the groupings of keys and values that were generated using the key and value functions and the source collection
     */
    public static <T, K extends Comparable<? super K>, V> Enumerable<Group<K, V>> groupBy(Collection<T> collection, Func<T, K> keyFunc, Func<T, V> valueFunc) {
        return lavaBase.groupBy(collection, keyFunc, valueFunc);
    }

    /**
     * Groups the elements in the collection using the keys that are generated by the key function, then transforms the groupings into a single result object
     *
     * @param collection The source collection
     * @param keyFunc    The function used to generate the keys
     * @param resultFunc The function that transforms each grouping into the result object
     * @param <T>        The type of object in the collection
     * @param <K>        The type of the key
     * @param <Result>   The type of the resulting object
     * @return An Enumerable instance containing the result objects that were generated from each grouping using the result function
     */
    public static <T, K extends Comparable<? super K>, Result extends Comparable<? super Result>> Enumerable<Result> groupBy(Collection<T> collection,
                                                                                                                             Func<T, K> keyFunc,
                                                                                                                             Func2<K, Collection<T>, Result> resultFunc) {
        return lavaBase.groupBy(collection, keyFunc, resultFunc);
    }

    /**
     * Groups the elements in the collection using keys that are generated by the key function and the values the value function generates, then transforms the groupings into a single result object
     *
     * @param collection The source collection
     * @param keyFunc    The function used to generate the keys
     * @param valueFunc  The function used to generate the values
     * @param resultFunc The function that transforms each grouping into the result object
     * @param <T>        The type of object in the collection
     * @param <K>        The type of the key
     * @param <V>        The type of the value
     * @param <Result>   The type of the resulting object
     * @return An Enumerable instance containing the result objects that were generated from each grouping using the result function
     */
    public static <T, K extends Comparable<? super K>, V, Result extends Comparable<? super Result>> Enumerable<Result> groupBy(Collection<T> collection,
                                                                                                                                Func<T, K> keyFunc,
                                                                                                                                Func<T, V> valueFunc,
                                                                                                                                Func2<K, Collection<V>, Result> resultFunc) {
        return lavaBase.groupBy(collection, keyFunc, valueFunc, resultFunc);
    }

    /**
     * Joins the two collections based on a common key and groups the results together for the result function.
     *
     * @param outerCollection The first collection to join
     * @param innerCollection The second collection to join
     * @param outerKeyFunc    The callback function that generates keys for the first collection
     * @param innerKeyFunc    The callback function that generates keys for the second collection
     * @param resultFunc      The callback function that generates the resulting object after the joins
     * @param <Outer>         The type in the first collection
     * @param <Inner>         The type in the second collection
     * @param <Key>           The type of the common key
     * @param <Result>        The type of the resulting object
     * @return An Enumerable instance containing the objects of the result callback
     */
    public static <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> groupJoin(Collection<Outer> outerCollection,
                                                                                                                                              Collection<Inner> innerCollection,
                                                                                                                                              Func<Outer, Key> outerKeyFunc,
                                                                                                                                              Func<Inner, Key> innerKeyFunc,
                                                                                                                                              Func2<Outer, Collection<Inner>, Result> resultFunc) {
        return lavaBase.groupJoin(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc);
    }

    /**
     * Creates an intersection between the two collections. The resulting Enumerable implementation will be of the same type as the first collection.
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of object in the collections
     * @return The intersection of the two collections
     */
    public static <T extends Comparable<? super T>> Enumerable<T> intersect(Collection<T> first, Collection<T> second) {
        return lavaBase.intersect(first, second);
    }

    /**
     * Joins the two collections on a set of common keys using the supplied callback functions.
     *
     * @param outerCollection The first collection to join on
     * @param innerCollection The second collection to join on
     * @param outerKeyFunc    The callback function used to generate a common key from the first collection
     * @param innerKeyFunc    The callback function used to generate a common key from the second collection
     * @param resultFunc      The callback function used to generate a result object based on the outputs of the other callback functions
     * @param <Outer>         The type of the object in the first collection
     * @param <Inner>         The type of the object in the second collection
     * @param <Key>           The type of the common key
     * @param <Result>        The type of the result object
     * @return An enumerable instance that contains the results of the join
     */
    public static <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                                                         Collection<Inner> innerCollection,
                                                                                                                                         Func<Outer, Key> outerKeyFunc,
                                                                                                                                         Func<Inner, Key> innerKeyFunc,
                                                                                                                                         Func2<Outer, Inner, Result> resultFunc) {
        return lavaBase.join(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc);
    }

    /**
     * Joins the two collections on a set of common keys using the supplied callback functions.
     *
     * @param outerCollection The first collection to join on
     * @param innerCollection The second collection to join on
     * @param outerKeyFunc    The callback function used to generate a common key from the first collection
     * @param innerKeyFunc    The callback function used to generate a common key from the second collection
     * @param resultFunc      The callback function used to generate a result object based on the outputs of the other callback functions
     * @param keyComparator   The comparator used to compare the keys
     * @param <Outer>         The type of the object in the first collection
     * @param <Inner>         The type of the object in the second collection
     * @param <Key>           The type of the common key
     * @param <Result>        The type of the result object
     * @return An enumerable instance that contains the results of the join
     */
    public static <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                                                         Collection<Inner> innerCollection,
                                                                                                                                         Func<Outer, Key> outerKeyFunc,
                                                                                                                                         Func<Inner, Key> innerKeyFunc,
                                                                                                                                         Func2<Outer, Inner, Result> resultFunc,
                                                                                                                                         Comparator<Key> keyComparator) {
        return lavaBase.join(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc, keyComparator);
    }

    /**
     * Obtains the last element in the collection
     *
     * @param collection The collection to search
     * @param <T>        The type of the object
     * @return The last element in the collection
     */
    public static <T extends Comparable<? super T>> T last(Collection<T> collection) {
        return lavaBase.last(collection);
    }

    /**
     * Obtains the last element in the collection that satisfies the given callback function
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the elements in the collection
     * @return The last element in the list that satisfies the callback
     * @throws java.util.NoSuchElementException
     *          If no elements are found
     */
    public static <T extends Comparable<? super T>> T last(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.last(collection, func);
    }

    /**
     * Obtains the last element in the collection, or returns null.
     *
     * @param collection The collection to search
     * @param <T>        The type of the element
     * @return The last element or null
     */
    public static <T extends Comparable<? super T>> T lastOrDefault(Collection<T> collection) {
        return lavaBase.lastOrDefault(collection);
    }

    /**
     * Obtains the last element in the collection that satisfies the given callback function, or null
     *
     * @param collection The collection to search
     * @param func       The callback function to use
     * @param <T>        The type of the element
     * @return The last element to satisfy the callback function, or null
     */
    public static <T extends Comparable<? super T>> T lastOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.lastOrDefault(collection, func);
    }

    /**
     * Returns the largest value from the collection, using the default comparison method
     *
     * @param collection The collection to search
     * @param <T>        The type of the collection
     * @return The largest value in from the collection
     */
    public static <T extends Comparable<? super T>> T max(Collection<T> collection) {
        return lavaBase.max(collection);
    }

    /**
     * Returns the largest value from the collection using the callback function to determine the value to be compared.
     *
     * @param collection The collection to search
     * @param func       The callback function used to generate the value
     * @param <T>        The type of the object in the collection
     * @param <E>        The type of the object returned from the callback function
     * @return The largest value from the collection
     */
    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> E max(Collection<T> collection, Func<T, E> func) {
        return lavaBase.max(collection, func);
    }

    /**
     * Returns the smallest value from the collection, using the default comparison method
     *
     * @param collection The collection to search
     * @param <T>        The type of the collection
     * @return The smallest value in from the collection
     */
    public static <T extends Comparable<? super T>> T min(Collection<T> collection) {
        return lavaBase.min(collection);
    }

    /**
     * Returns the smallest value from the collection using the callback function to determine the value to be compared.
     *
     * @param collection The collection to search
     * @param func       The callback function used to generate the value
     * @param <T>        The type of the object in the collection
     * @param <E>        The type of the object returned from the callback function
     * @return The smallest value from the collection
     */
    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> E min(Collection<T> collection, Func<T, E> func) {
        return lavaBase.min(collection, func);
    }

    /**
     * Filters the untyped collection and returns only the elements that conform to the given class
     *
     * @param collection The untyped collection to filter
     * @param clazz      The class to filter with
     * @param <T>        The type of the object that is returned
     * @return The filtered collection that contains only the objects of type clazz
     */
    public static <T extends Comparable<? super T>> Enumerable<T> ofType(Collection collection, Class<T> clazz) {
        return lavaBase.ofType(collection, clazz);
    }

    /**
     * Orders the given collection using a default comparator
     *
     * @param collection The collection to order
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection) {
        return lavaBase.orderBy(collection);
    }

    /**
     * Orders the given collection using the given comparator
     *
     * @param collection The collection to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection, Comparator<T> comparator) {
        return lavaBase.orderBy(collection, comparator);
    }

    /**
     * Orders the given collection using a default comparator in reverse
     *
     * @param collection The collection to order
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection) {
        return lavaBase.orderByDescending(collection);
    }

    /**
     * Orders the given collection using the given comparator in reverse
     *
     * @param collection The collection to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection, Comparator<T> comparator) {
        return lavaBase.orderByDescending(collection, comparator);
    }

    /**
     * Randomizes the given collection
     *
     * @param collection The collection to randomize
     * @param <T>        The type of object in the collection
     * @return The randomized collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> randomize(Collection<T> collection) {
        return lavaBase.randomize(collection);
    }

    /**
     * Randomizes the given collection using the given {@link Random} instance to
     *
     * @param collection The collection to randomize
     * @param random     The {@link Random} instance to use as a seed
     * @param <T>        The type of object in the collection
     * @return The randomized collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> randomize(Collection<T> collection, Random random) {
        return lavaBase.randomize(collection, random);
    }

    /**
     * Generates a sequence of numbers that starts at {@code start} and ends at {@code start + count}
     *
     * @param start The integer to start the counting
     * @param count The number of integers to count
     * @return The sequence of numbers between {@code start} and {@code start + count}
     */
    public static Enumerable<Integer> range(int start, int count) {
        return lavaBase.range(start, count);
    }

    /**
     * Creates an enumerable that contains the {@code src} object {@code count} times
     *
     * @param src   The object that will be repeated in the enumerable
     * @param count The number of times to repeat the object
     * @param <T>   The type of the object in the enumerable
     * @return An enumerable containing {@code count src} objects
     */
    public static <T extends Comparable<? super T>> Enumerable<T> repeat(T src, int count) {
        return lavaBase.repeat(src, count);
    }

    /**
     * Reverses the collection and wraps the return value in an Enumerable instance
     *
     * @param collection The collection to reverse
     * @param <T>        The type of object in the collection
     * @return The reversed collection wrapped in an Enumerable
     */
    public static <T extends Comparable<? super T>> Enumerable<T> reverse(Collection<T> collection) {
        return lavaBase.reverse(collection);
    }

    /**
     * Transforms the contents of {@code list} using the {@code func} function into a {@link Enumerable} instance containing objects of type {@code E}
     *
     * @param collection The source collection
     * @param func       The function that transforms the objects
     * @param <T>        The type of the original objects
     * @param <E>        The type of the transformed objects
     * @return A collection of transformed objects
     */
    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> Enumerable<E> select(Collection<T> collection, Func<T, E> func) {
        return lavaBase.select(collection, func);
    }

    /**
     * Performs a one to many projection from the source object to a resulting collection
     *
     * @param sourceCollection The source collection
     * @param resultFunc       The function that returns a collection based on the source object
     * @param <Source>         The type of the source object
     * @param <Result>         The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    public static <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                    Func<Source, Collection<Result>> resultFunc) {
        return lavaBase.selectMany(sourceCollection, resultFunc);
    }

    /**
     * Performs a one to many projection from the source object to a resulting collection. Passes the index of each object to the callback function
     *
     * @param sourceCollection The source collection
     * @param resultFunc       The function that returns a collection based on the source object
     * @param <Source>         The type of the source object
     * @param <Result>         The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    public static <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                    Func2<Source, Integer, Collection<Result>> resultFunc) {
        return lavaBase.selectMany(sourceCollection, resultFunc);
    }

    /**
     * Preforms a one to many projection from the source collection to a resulting collection
     *
     * @param sourceCollection The source collection
     * @param collectionFunc   The function that returns a collection based on the element passed in
     * @param resultFunc       The function that generates a new result based on the collection and source element
     * @param <Source>         The type of object in the source collection
     * @param <TCollection>    The type of object in the projected collection
     * @param <Result>         The type of object that is to be returned from the projection
     * @return A collection of the resulting objects from the result function
     */
    public static <Source, TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                                 Func<Source, Collection<TCollection>> collectionFunc,
                                                                                                                 Func2<Source, TCollection, Result> resultFunc) {
        return lavaBase.selectMany(sourceCollection, collectionFunc, resultFunc);
    }

    /**
     * Checks both collections to see if they are equal. "Equal" is defined as: having the same size, and having the objects in the same order.
     * If the two collections contain the same objects, but those objects are in different orders, then they are not considered "equal" by this method.
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of object in both collections
     * @return True if they are equal, as defined above, and false if they are not.
     */
    public static <T extends Comparable<? super T>> boolean sequenceEqual(Collection<T> first, Collection<T> second) {
        return lavaBase.sequenceEqual(first, second);
    }

    /**
     * Searches for a single element that matches using the callback function. If there are multiple objects that match, an exception is thrown.
     *
     * @param collection The collection to search
     * @param func       The function used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function.
     * @throws java.util.NoSuchElementException
     *          If no element in the collection returns true for the func.
     * @throws org.icechamps.lava.exception.MultipleElementsFoundException
     *          If there are multiple elements that match the func.
     */
    public static <T extends Comparable<? super T>> T single(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.single(collection, func);
    }

    /**
     * Searches for a single element that matches using the callback function.
     * If there are multiple objects that match, an exception is thrown.
     * If there are no elements that match, null is returned.
     *
     * @param collection The collection to search
     * @param func       The callback used to search the list
     * @param <T>        The type of the object in the list
     * @return The single element that matches using the callback function or null if none are found.
     * @throws org.icechamps.lava.exception.MultipleElementsFoundException
     *          If there are multiple elements that match the callback.
     */
    public static <T extends Comparable<? super T>> T singleOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.singleOrDefault(collection, func);
    }

    /**
     * Skips {@code count} number of elements in the given collection, then returns the rest of the collection
     *
     * @param collection The collection to use
     * @param count      The number of elements of the collection to skip
     * @param <T>        The type of the elements in the collection
     * @return The elements in the collection minus the first {@code count} elements
     */
    public static <T extends Comparable<? super T>> Enumerable<T> skip(Collection<T> collection, int count) {
        return lavaBase.skip(collection, count);
    }

    /**
     * Skips elements in the collection as long as the callback function returns true
     *
     * @param collection The collection to use
     * @param func       The callback function used to test each element
     * @param <T>        The type of object in the collection
     * @return A subset of the collection minus the elements the callback function filtered out
     */
    public static <T extends Comparable<? super T>> Enumerable<T> skipWhile(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.skipWhile(collection, func);
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    public static <T extends Number> Number sum(Collection<T> collection) {
        return lavaBase.sum(collection);
    }

    /**
     * Takes the first {@code count} elements from the collection and returns them in a new collection.
     *
     * @param collection The collection to use
     * @param count      The number of elements to take
     * @param <T>        The type of the elements
     * @return The first {@code count} elements in the collection
     * @throws IndexOutOfBoundsException If count is larger than the number of elements in the collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> take(Collection<T> collection, int count) {
        return lavaBase.take(collection, count);
    }

    /**
     * Takes elements from the beginning of the collection, as long as the callback returns true, and puts them, in order, into a resulting collection.
     *
     * @param collection The collection to search
     * @param func       The callback function to use to test the elements
     * @param <T>        The type of the elements in the list
     * @return The resulting elements from the collection
     */
    public static <T extends Comparable<? super T>> Enumerable<T> takeWhile(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.takeWhile(collection, func);
    }

    /**
     * Creates a new {@link List} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return The new List containing the elements in the collection
     */
    public static <T extends Comparable<? super T>> List<T> toList(Collection<T> collection) {
        return lavaBase.toList(collection);
    }

    /**
     * Creates a new {@link Set} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return A new Set containing the objects in the collection
     */
    public static <T extends Comparable<? super T>> Set<T> toSet(Collection<T> collection) {
        return lavaBase.toSet(collection);
    }

    /**
     * Searches a collection using the given {@link Func callback} function.
     *
     * @param collection     The collection to search through
     * @param searchCriteria The callback function to search with
     * @param <T>            The type of the object in the list
     * @return A subset of the collection where all of the objects return a match in the callback function.
     */
    public static <T extends Comparable<? super T>> Enumerable<T> where(Collection<T> collection, Func<T, Boolean> searchCriteria) {
        return lavaBase.where(collection, searchCriteria);
    }

    /**
     * Creates an enumerable containing the union of the two collections
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of the object in the collection
     * @return The enumerable containing the union
     */
    public static <T extends Comparable<? super T>> Enumerable<T> union(Collection<T> first, Collection<T> second) {
        return lavaBase.union(first, second);
    }

    /**
     * Creates an enumerable that contains the mapping of the two collections into a single collection.
     *
     * @param first    The first collection
     * @param second   The second collection
     * @param func     The callback function used to create the mapping
     * @param <First>  The type of the first object
     * @param <Second> The type of the second object
     * @param <Result> The type of the mapped object
     * @return The enumerable that contains the mapping
     */
    public static <First, Second, Result extends Comparable<? super Result>> Enumerable<Result> zip(Collection<First> first,
                                                                                                    Collection<Second> second,
                                                                                                    Func2<First, Second, Result> func) {
        return lavaBase.zip(first, second, func);
    }
}
