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
    // Max
    ///////////////

    /**
     * Returns the largest value from the collection, using the default comparison method
     *
     * @param collection The collection to search
     * @param <T>        The type of the collection
     * @return The largest value in from the collection
     */
    protected <T extends Comparable<? super T>> T max(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        T ret = null;
        for (T t : collection) {
            if (ret == null) {
                ret = t;
                continue;
            }

            if (ret.compareTo(t) > 0) {
                ret = t;
            }
        }

        return ret;
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
    protected <T, E extends Comparable<? super E>> E max(Collection<T> collection, Func<T, E> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        E ret = null;

        for (T t : collection) {
            E e = func.callback(t);
            if (ret == null) {
                ret = e;
                continue;
            }

            if (ret.compareTo(e) > 0) {
                ret = e;
            }
        }

        return ret;
    }

    ///////////////
    // Min
    ///////////////

    /**
     * Returns the smallest value from the collection, using the default comparison method
     *
     * @param collection The collection to search
     * @param <T>        The type of the collection
     * @return The smallest value in from the collection
     */
    protected <T extends Comparable<? super T>> T min(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        T ret = null;
        for (T t : collection) {
            if (ret == null) {
                ret = t;
                continue;
            }

            if (ret.compareTo(t) < 0) {
                ret = t;
            }
        }

        return ret;
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
    protected <T, E extends Comparable<? super E>> E min(Collection<T> collection, Func<T, E> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        E ret = null;

        for (T t : collection) {
            E e = func.callback(t);
            if (ret == null) {
                ret = e;
                continue;
            }

            if (ret.compareTo(e) < 0) {
                ret = e;
            }
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
    // Order By Descending
    ///////////////

    /**
     * Orders the given collection using a default comparator in reverse
     *
     * @param collection The collection to order
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderByDescending(Collection<T> collection) {
        LavaList<T> ret = orderByListInternal(collection, null);
        Collections.reverse(ret);
        return ret;
    }

    /**
     * Orders the given collection using the given comparator in reverse
     *
     * @param collection The collection to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    protected <T extends Comparable<? super T>> LavaList<T> orderByDescending(Collection<T> collection, Comparator<T> comparator) {
        LavaList<T> ret = orderByListInternal(collection, comparator);
        Collections.reverse(ret);
        return ret;
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
    // Select Many
    ///////////////

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
    protected <Source, TCollection, Result> LavaCollection<Result> selectMany(Collection<Source> sourceCollection,
                                                                              Func<Source, Collection<TCollection>> collectionFunc,
                                                                              Func2<Source, TCollection, Result> resultFunc) {
        Preconditions.checkNotNull(sourceCollection);
        Preconditions.checkNotNull(collectionFunc);
        Preconditions.checkNotNull(resultFunc);

        LavaCollection<Result> results = buildLavaCollectionFromCollection(sourceCollection);
        for (Source source : sourceCollection) {
            for (TCollection collection : collectionFunc.callback(source)) {
                results.add(resultFunc.callback(source, collection));
            }
        }

        return results;
    }

    ///////////////
    // Sequence Equal
    ///////////////

    /**
     * Checks both collections to see if they are equal. "Equal" is defined as: having the same size, and having the objects in the same order.
     * If the two collections contain the same objects, but those objects are in different orders, then they are not considered "equal" by this method.
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of object in both collections
     * @return True if they are equal, as defined above, and false if they are not.
     */
    protected <T> boolean sequenceEqual(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        if (first.size() != second.size())
            return false;

        int size = first.size();
        Iterator<T> firstIter = first.iterator();
        Iterator<T> secondIter = second.iterator();

        while (firstIter.hasNext() && secondIter.hasNext()) {
            if (!firstIter.next().equals(secondIter.next()))
                return false;
        }

        return true;
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
    // Skip
    ///////////////

    /**
     * Skips {@code count} number of elements in the given collection, then returns the rest of the collection
     *
     * @param collection The collection to use
     * @param count      The number of elements of the collection to skip
     * @param <T>        The type of the elements in the collection
     * @return The elements in the collection minus the first {@code count} elements
     */
    protected <T> LavaCollection<T> skip(Collection<T> collection, int count) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument(count > collection.size());

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);

        if (count == 0) {
            ret.addAll(collection);
            return ret;
        }

        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < count; i++) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            ret.add(iterator.next());
        }

        return ret;
    }

    ///////////////
    // Skip While
    ///////////////

    /**
     * Skips elements in the collection as long as the callback function returns true
     *
     * @param collection The collection to use
     * @param func       The callback function used to test each element
     * @param <T>        The type of object in the collection
     * @return A subset of the collection minus the elements the callback function filtered out
     */
    protected <T> LavaCollection<T> skipWhile(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        Iterator<T> iterator = collection.iterator();
        while (func.callback(iterator.next())) ;

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);
        while (iterator.hasNext()) {
            ret.add(iterator.next());
        }

        return ret;
    }

    ///////////////
    // Sum
    ///////////////

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Byte sum(Collection<Byte> collection) {
        Preconditions.checkNotNull(collection);

        Byte ret = new Byte((byte) 0);

        for (Byte b : collection) {
            ret += b;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Double sum(Collection<Double> collection) {
        Preconditions.checkNotNull(collection);

        Double ret = new Double(0);

        for (Double d : collection) {
            ret += d;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Float sum(Collection<Float> collection) {
        Preconditions.checkNotNull(collection);

        Float ret = new Float(0);

        for (Float f : collection) {
            ret += f;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Integer sum(Collection<Integer> collection) {
        Preconditions.checkNotNull(collection);

        Integer ret = new Integer(0);

        for (Integer i : collection) {
            ret += i;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Long sum(Collection<Long> collection) {
        Preconditions.checkNotNull(collection);

        Long ret = new Long(0);

        for (Long l : collection) {
            ret += l;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Short sum(Collection<Short> collection) {
        Preconditions.checkNotNull(collection);

        Short ret = new Short((short) 0);

        for (Short s : collection) {
            ret += s;
        }

        return ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @param func       The callback function that does the addition. The first argument is the result of all the sums so far. The second is the current item in the iteration. The result is the addition of the two. On first run, the first argument will be null.
     * @param <T>        The type of object in the collection
     * @return The added values of the collection
     */
    protected <T> T sum(Collection<T> collection, Func2<T, T, T> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        if (collection.isEmpty()) {
            return null;
        }

        Iterator<T> iterator = collection.iterator();
        T ret = func.callback(null, iterator.next());

        while (iterator.hasNext()) {
            ret = func.callback(ret, iterator.next());
        }

        return ret;
    }

    ///////////////
    // Take
    ///////////////

    /**
     * Takes the first {@code count} elements from the collection and returns them in a new collection.
     *
     * @param collection The collection to use
     * @param count      The number of elements to take
     * @param <T>        The type of the elements
     * @return The first {@code count} elements in the collection
     * @throws IndexOutOfBoundsException If count is larger than the number of elements in the collection
     */
    protected <T> LavaCollection<T> take(Collection<T> collection, int count) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(count >= 0);

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);

        if (count == 0 || collection.isEmpty())
            return ret;

        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < count; i++) {
            if (iterator.hasNext())
                ret.add(iterator.next());
            else
                throw new IndexOutOfBoundsException();
        }

        return ret;
    }

    ///////////////
    // Take While
    ///////////////

    /**
     * Takes elements from the beginning of the collection, as long as the callback returns true, and puts them, in order, into a resulting collection.
     *
     * @param collection The collection to search
     * @param func       The callback function to use to test the elements
     * @param <T>        The type of the elements in the list
     * @return The resulting elements from the collection
     */
    protected <T> LavaCollection<T> takeWhile(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        LavaCollection<T> ret = buildLavaCollectionFromCollection(collection);

        if (collection.isEmpty())
            return ret;

        Iterator<T> iterator = collection.iterator();

        while (iterator.hasNext()) {
            T next = iterator.next();
            if (func.callback(next))
                ret.add(next);
        }

        return ret;
    }

    ///////////////
    // To Map
    ///////////////

    /**
     * Projects the given collection to a {@link Map} type using the callback function. The callback will generate a key based on the given value. The resulting key will be paired with the value and added to the map.
     *
     * @param collection The collection to project
     * @param func       The function to generate keys for the values
     * @param <Key>      The type of the key
     * @param <Value>    The type of the value
     * @return The new Map instance containing the keys and values from the collection
     */
    protected <Key, Value> Map<Key, Value> toMap(Collection<Value> collection, Func<Value, Key> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        if (collection.isEmpty()) {
            return new HashMap<Key, Value>();
        }

        HashMap<Key, Value> ret = new HashMap<Key, Value>(collection.size());

        for (Value value : collection) {
            Key key = func.callback(value);
            ret.put(key, value);
        }

        return ret;
    }

    ///////////////
    // To List
    ///////////////

    /**
     * Creates a new {@link LavaList} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return The new LavaList containing the elements in the collection
     */
    protected <T> LavaList<T> toList(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        LavaList<T> ret = new LavaList<T>();
        ret.addAll(collection);

        return ret;
    }

    ///////////////
    // To Set
    ///////////////

    /**
     * Creates a new {@link LavaSet} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return A new LavaSet containing the objects in the collection
     */
    protected <T> LavaSet<T> toSet(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        LavaSet<T> set = new LavaSet<T>();
        set.addAll(collection);

        return set;
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

    //TODO: Add the following methods: Union, Zip
    //TODO: Phase 2: Average, Cast, Concat, ElementAt?, ElementAtOrDefault?, Except, GroupBy, GroupJoin, Join, OfType, Range, Repeat, Reverse

}
