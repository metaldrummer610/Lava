package org.icechamps.lava.interfaces;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 11:48 PM
 *
 * @param <T> The type of the object in the collection
 */
public interface Enumerable<T> extends Iterable<T> {
    /**
     * Aggregates the objects using the callback function
     *
     * @param func The callback that aggregates the objects
     * @param <V>  The type of the object to return
     * @return The aggregated object from the collection
     */
    public <V> V aggregate(Func2<T, V, V> func);

    /**
     * Iterates over the given collection and checks each element using the callback function.
     *
     * @param func The func function to use on the collection's elements
     * @return Returns true if all of the elements return true for the func. Returns false if a single element doesn't match.
     */
    public boolean all(Func<T, Boolean> func);

    /**
     * Checks if there are any elements in the collection.
     *
     * @return True if there are any elements in the collection, false if not.
     */
    public boolean any();

    /**
     * Returns the number of elements in the collection
     *
     * @return The number of elements in the collection
     */
    public int count();

    /**
     * Returns a Enumerable containing only distinct elements.
     *
     * @return A subset of the inital list containing no duplicates.
     */
    public Enumerable<T> distinct();

    /**
     * Returns the element in the collection at the specified index. Useful for collections that do not allow positional access.
     *
     * @param index The index of the element
     * @return The element in the collection at the specified index
     */
    public T elementAt(int index);

    /**
     * Returns the element in the collection at the specified index, or null if the index is out of bounds. Useful for collections that do not allow positional access.
     *
     * @param index The index of the element
     * @return The element in the collection at the specified index or null if the index is out of bounds
     */
    public T elementAtOrDefault(int index);

    /**
     * Creates an enumerable containing the difference between the two collections
     *
     * @param second The second collection
     * @return The enumerable containing the difference
     */
    public Enumerable<T> except(Collection<T> second);

    /**
     * Returns the first object in the collection.
     *
     * @return The first object in the collection
     */
    public T first();

    /**
     * Returns the first object in the collection that matches the callback function. Throws an exception if nothing is found.
     *
     * @param func The callback function to use
     * @return The first match the callback function finds
     */
    public T first(Func<T, Boolean> func);

    /**
     * Returns the first item in the collection, or null if there isn't one. Throws an exception if the collection is empty
     *
     * @return The first item in the collection, or null.
     */
    public T firstOrDefault();

    /**
     * Returns the first item in the collection using the callback function, or null if there isn't one.
     *
     * @param func The callback function to use
     * @return The first item in the collection, or null.
     */
    public T firstOrDefault(Func<T, Boolean> func);

    /**
     * Joins the two collections based on a common key and groups the results together for the result function.
     *
     * @param innerCollection The second collection to join
     * @param outerKeyFunc    The callback function that generates keys for the first collection
     * @param innerKeyFunc    The callback function that generates keys for the second collection
     * @param resultFunc      The callback function that generates the resulting object after the joins
     * @param <Inner>         The type in the second collection
     * @param <Key>           The type of the common key
     * @param <Result>        The type of the resulting object
     * @return An Enumerable instance containing the objects of the result callback
     */
    public <Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> groupJoin(Collection<Inner> innerCollection,
                                                                                                                                Func<T, Key> outerKeyFunc,
                                                                                                                                Func<Inner, Key> innerKeyFunc,
                                                                                                                                Func2<T, Collection<Inner>, Result> resultFunc);

    /**
     * Creates an intersection between the two collections. The resulting Enumerable implementation will be of the same type as the first collection.
     *
     * @param second The second collection
     * @return The intersection of the two collections
     */
    public Enumerable<T> intersect(Collection<T> second);

    /**
     * Obtains the last element in the collection
     *
     * @return The last element in the collection
     */
    public T last();

    /**
     * Obtains the last element in the collection that satisfies the given callback function
     *
     * @param func The callback function to use
     * @return The last element in the list that satisfies the callback
     * @throws java.util.NoSuchElementException
     *          If no elements are found
     */
    public T last(Func<T, Boolean> func);

    /**
     * Obtains the last element in the collection, or returns null.
     *
     * @return The last element or null
     */
    public T lastOrDefault();

    /**
     * Obtains the last element in the collection that satisfies the given callback function, or null
     *
     * @param func The callback function to use
     * @return The last element to satisfy the callback function, or null
     */
    public T lastOrDefault(Func<T, Boolean> func);

    /**
     * Returns the largest value from the collection, using the default comparison method
     *
     * @return The largest value in from the collection
     */
    public T max();

    /**
     * Returns the largest value from the collection using the callback function to determine the value to be compared.
     *
     * @param func The callback function used to generate the value
     * @param <E>  The type of the object returned from the callback function
     * @return The largest value from the collection
     */
    public <E extends Comparable<? super E>> E max(Func<T, E> func);

    /**
     * Returns the smallest value from the collection, using the default comparison method
     *
     * @return The smallest value in from the collection
     */
    public T min();

    /**
     * Returns the smallest value from the collection using the callback function to determine the value to be compared.
     *
     * @param func The callback function used to generate the value
     * @param <E>  The type of the object returned from the callback function
     * @return The smallest value from the collection
     */
    public <E extends Comparable<? super E>> E min(Func<T, E> func);

    /**
     * Orders the given collection using a default comparator
     *
     * @return The sorted collection
     */
    public Enumerable<T> orderBy();

    /**
     * Orders the given collection using the given comparator
     *
     * @param comparator The comparator to use
     * @return The sorted collection
     */
    public Enumerable<T> orderBy(Comparator<T> comparator);

    /**
     * Orders the given collection using a default comparator in reverse
     *
     * @return The sorted collection
     */
    public Enumerable<T> orderByDescending();

    /**
     * Orders the given collection using the given comparator in reverse
     *
     * @param comparator The comparator to use
     * @return The sorted collection
     */
    public Enumerable<T> orderByDescending(Comparator<T> comparator);

    /**
     * Randomizes the collection
     *
     * @return The randomized collection
     */
    public Enumerable<T> randomize();

    /**
     * Randomizes the collection using the given {@link Random}
     *
     * @param random The random to use
     * @return The randomized collection
     */
    public Enumerable<T> randomize(Random random);

    /**
     * Reverses the collection and wraps the return value in an Enumerable instance
     *
     * @return The reversed collection wrapped in an Enumerable
     */
    public Enumerable<T> reverse();

    /**
     * Transforms the contents of {@code list} using the {@code func} function into a {@link Enumerable}
     * instance containing objects of type {@code E}
     *
     * @param func The function that transforms the objects
     * @param <E>  The type of the transformed objects
     * @return A collection of transformed objects
     */
    public <E extends Comparable<? super E>> Enumerable<E> select(Func<T, E> func);

    /**
     * Performs a one to many projection from the source object to a resulting collection
     *
     * @param func     The function that returns a collection based on the source object
     * @param <Result> The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    public <Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func<T, Collection<Result>> func);

    /**
     * Performs a one to many projection from the source object to a resulting collection. Passes the index of each object to the callback function
     *
     * @param func     The function that returns a collection based on the source object
     * @param <Result> The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    public <Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func2<T, Integer, Collection<Result>> func);

    /**
     * Preforms a one to many projection from the source collection to a resulting collection
     *
     * @param collectionFunc The function that returns a collection based on the element passed in
     * @param resultFunc     The function that generates a new result based on the collection and source element
     * @param <TCollection>  The type of object in the projected collection
     * @param <Result>       The type of object that is to be returned from the projection
     * @return A collection of the resulting objects from the result function
     */
    public <TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func<T, Collection<TCollection>> collectionFunc,
                                                                                                  Func2<T, TCollection, Result> resultFunc);

    /**
     * Checks both collections to see if they are equal. "Equal" is defined as: having the same size, and having the objects in the same order.
     * If the two collections contain the same objects, but those objects are in different orders, then they are not considered "equal" by this method.
     *
     * @param second The second collection
     * @return True if they are equal, as defined above, and false if they are not.
     */
    public boolean sequenceEqual(Collection<T> second);

    /**
     * Searches for a single element that matches using the callback function. If there are multiple objects that match, an exception is thrown.
     *
     * @param func The function used to search the list
     * @return The single element that matches using the callback function.
     * @throws java.util.NoSuchElementException
     *          If no element in the collection returns true for the func.
     * @throws org.icechamps.lava.exception.MultipleElementsFoundException
     *          If there are multiple elements that match the func.
     */
    public T single(Func<T, Boolean> func);

    /**
     * Searches for a single element that matches using the callback function.
     * If there are multiple objects that match, an exception is thrown.
     * If there are no elements that match, null is returned.
     *
     * @param func The callback used to search the list
     * @return The single element that matches using the callback function or null if none are found.
     * @throws org.icechamps.lava.exception.MultipleElementsFoundException
     *          If there are multiple elements that match the callback.
     */
    public T singleOrDefault(Func<T, Boolean> func);

    /**
     * Skips {@code count} number of elements in the given collection, then returns the rest of the collection
     *
     * @param count The number of elements of the collection to skip
     * @return The elements in the collection minus the first {@code count} elements
     */
    public Enumerable<T> skip(int count);

    /**
     * Skips elements in the collection as long as the callback function returns true
     *
     * @param func The callback function used to test each element
     * @return A subset of the collection minus the elements the callback function filtered out
     */
    public Enumerable<T> skipWhile(Func<T, Boolean> func);

    /**
     * Takes the first {@code count} elements from the collection and returns them in a new collection.
     *
     * @param count The number of elements to take
     * @return The first {@code count} elements in the collection
     * @throws IndexOutOfBoundsException If count is larger than the number of elements in the collection
     */
    public Enumerable<T> take(int count);

    /**
     * Takes elements from the beginning of the collection, as long as the callback returns true, and puts them, in order, into a resulting collection.
     *
     * @param func The callback function to use to test the elements
     * @return The resulting elements from the collection
     */
    public Enumerable<T> takeWhile(Func<T, Boolean> func);

    /**
     * Creates a new {@link List} from the given collection
     *
     * @return The new List containing the elements in the collection
     */
    public List<T> toList();

    /**
     * Creates a new {@link Set} from the given collection
     *
     * @return A new Set containing the objects in the collection
     */
    public Set<T> toSet();

    /**
     * Searches a collection using the given {@link Func callback} function.
     *
     * @param func The callback function to search with
     * @return A subset of the collection where all of the objects return a match in the callback function.
     */
    public Enumerable<T> where(Func<T, Boolean> func);

    /**
     * Creates an enumerable containing the union of the two collections
     *
     * @param second The second collection
     * @return The enumerable containing the union
     */
    public Enumerable<T> union(Collection<T> second);

    /**
     * Creates an enumerable that contains the mapping of the two collections into a single collection.
     *
     * @param second   The second collection
     * @param func     The callback function used to create the mapping
     * @param <Second> The type of the second object
     * @param <Result> The type of the mapped object
     * @return The enumerable that contains the mapping
     */
    public <Second, Result extends Comparable<? super Result>> Enumerable<Result> zip(Collection<Second> second, Func2<T, Second, Result> func);
}
