package org.icechamps.lava;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.collection.LavaEnumerable;
import org.icechamps.lava.exception.MultipleElementsFoundException;
import org.icechamps.lava.interfaces.Enumerable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    // Count
    ///////////////

    /**
     * Returns the number of elements in the collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return The number of elements in the collection
     */
    protected <T> int count(Collection<T> collection) {
        Preconditions.checkNotNull(collection);
        return collection.size();
    }

    ///////////////
    // Distinct
    ///////////////

    /**
     * Returns a Enumerable containing only distinct elements.
     *
     * @param collection The collection to search
     * @param <T>        The type of the object
     * @return A subset of the inital list containing no duplicates.
     */
    protected <T extends Comparable<? super T>> Enumerable<T> distinct(Collection<T> collection) {
        Preconditions.checkNotNull(collection);
        return new DistinctEnumerable<T>(collection);
    }

    /**
     * A container for holding the returned values of the distinct call
     *
     * @param <T> The type of the object in the enumerable
     */
    class DistinctEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        protected DistinctEnumerable(Collection<T> col) {
            super();
            collection = new HashSet<T>(col);
            iterator = collection.iterator();
        }
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
     * Creates an intersection between the two collections. The resulting Enumerable implementation will be of the same type as the first collection.
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of object in the collections
     * @return The intersection of the two collections
     */
    protected <T extends Comparable<? super T>> Enumerable<T> intersect(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        Enumerable<T> firstDistinct = distinct(first);
        Enumerable<T> secondDistinct = distinct(second);

        return new IntersectEnumerable<T>(firstDistinct, secondDistinct);
    }

    /**
     * Enumerable that implements the intersect functionality
     *
     * @param <T> The type of the object in the enumerable
     */
    class IntersectEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        private Enumerable<T> first;
        private Enumerable<T> second;

        IntersectEnumerable(Enumerable<T> first, Enumerable<T> second) {
            this.first = first;
            this.iterator = this.first.iterator();
            this.second = second;
        }

        public T next() {
            T next = iterator.next();

            for (T s : second)
                if (next.equals(s))
                    return s;

            return null;
        }
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
    protected <Outer, Inner, Key, Result> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                  Collection<Inner> innerCollection,
                                                                  Func<Outer, Key> outerKeyFunc,
                                                                  Func<Inner, Key> innerKeyFunc,
                                                                  Func2<Outer, Inner, Result> resultFunc) {
//        Preconditions.checkArgument(outerCollection != null);
//        Preconditions.checkArgument(innerCollection != null);
//        Preconditions.checkArgument(outerKeyFunc != null);
//        Preconditions.checkArgument(innerKeyFunc != null);
//        Preconditions.checkArgument(resultFunc != null);
//
//        Enumerable<Result> results = buildLavaCollectionFromCollection(outerCollection);
//        ArrayList<JoinedKey> outerKeys = new ArrayList<JoinedKey>();
//        ArrayList<JoinedKey> innerKeys = new ArrayList<JoinedKey>();
//
//        // Collect the keys from each collection
//        for (Outer outer : outerCollection) {
//            Key key = outerKeyFunc.callback(outer);
//            outerKeys.add(new JoinedKey(key, outer));
//        }
//
//        for (Inner inner : innerCollection) {
//            Key key = innerKeyFunc.callback(inner);
//            innerKeys.add(new JoinedKey(key, inner));
//        }
//
//        Iterator<JoinedKey> outerIterator = outerKeys.iterator();
//        Iterator<JoinedKey> innerIterator = innerKeys.iterator();
//
////        while (outerIterator.hasNext() && innerIterator.hasNext()) {
////            results.add(resultFunc.callback(outerIterator.next(), innerIterator.next()));
////        }
//
//        return results;
        throw new NotImplementedException();
    }

    private class JoinedKey<K, V> {
        protected K key;
        protected V value;

        protected JoinedKey(K k, V v) {
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
    protected <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection) {
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
    protected <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection, Comparator<T> comparator) {
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
    private <T extends Comparable<? super T>> Enumerable<T> orderByListInternal(Collection<T> collection, Comparator<T> comparator) {
        Preconditions.checkNotNull(collection);
        return new OrderByEnumerable<T>(collection, comparator);
    }

    class OrderByEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {

        OrderByEnumerable(Collection<T> col, Comparator<T> comparator) {
            List list = new ArrayList<T>(col);

            if (comparator != null)
                Collections.sort(list, comparator);
            else
                Collections.sort(list);

            collection = list;
            iterator = collection.iterator();
        }
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
    protected <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection) {
        return orderByDescendingListInternal(collection, null);
    }

    /**
     * Orders the given collection using the given comparator in reverse
     *
     * @param collection The collection to order
     * @param comparator The comparator to use
     * @param <T>        The type of the object in the collection
     * @return The sorted collection
     */
    protected <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection, Comparator<T> comparator) {
        return orderByDescendingListInternal(collection, comparator);
    }

    /**
     * Internal function that orders the given collection using the given comparator in reverse. The comparator can be null.
     *
     * @param collection The collection to order
     * @param comparator The comparator to use (can be null)
     * @param <T>        The type of the object in the collection
     * @return The ordered collection
     */
    private <T extends Comparable<? super T>> Enumerable<T> orderByDescendingListInternal(Collection<T> collection, Comparator<T> comparator) {
        Preconditions.checkNotNull(collection);
        return new OrderByDescendingEnumerable<T>(collection, comparator);
    }

    /**
     * Orders the collection in reverse.
     *
     * @param <T> The type of the object in the enumerable
     */
    class OrderByDescendingEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {

        OrderByDescendingEnumerable(Collection<T> col, Comparator<T> comparator) {
            List list = new ArrayList<T>(col);

            if (comparator != null)
                Collections.sort(list, comparator);
            else
                Collections.sort(list);

            Collections.reverse(list);

            collection = list;
            iterator = collection.iterator();
        }
    }

    ///////////////
    // Select
    ///////////////

    /**
     * Transforms the contents of {@code list} using the {@code func} function into a {@link Enumerable} instance containing objects of type {@code E}
     *
     * @param collection The source collection
     * @param func       The function that transforms the objects
     * @param <T>        The type of the original objects
     * @param <E>        The type of the transformed objects
     * @return A collection of transformed objects
     */
    protected <T, E extends Comparable<? super E>> Enumerable<E> select(Collection<T> collection, Func<T, E> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        return new SelectEnumerable<T, E>(collection, func);
    }

    class SelectEnumerable<T, E extends Comparable<? super E>> extends LavaEnumerable<E> {
        SelectEnumerable(Collection<T> col, Func<T, E> func) {
            collection = new ArrayList<E>();

            for (T obj : col) {
                E transformed = func.callback(obj);
                collection.add(transformed);
            }

            iterator = collection.iterator();
        }
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
    protected <Source, TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                             Func<Source, Collection<TCollection>> collectionFunc,
                                                                                                             Func2<Source, TCollection, Result> resultFunc) {
        Preconditions.checkNotNull(sourceCollection);
        Preconditions.checkNotNull(collectionFunc);
        Preconditions.checkNotNull(resultFunc);

        return new SelectManyEnumerable<Source, TCollection, Result>(sourceCollection, collectionFunc, resultFunc);
    }

    class SelectManyEnumerable<Source, TCollection, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        SelectManyEnumerable(Collection<Source> sourceCollection,
                             Func<Source, Collection<TCollection>> collectionFunc,
                             Func2<Source, TCollection, Result> resultFunc) {
            collection = new ArrayList<Result>();

            for (Source source : sourceCollection) {
                for (TCollection collection : collectionFunc.callback(source)) {
                    this.collection.add(resultFunc.callback(source, collection));
                }
            }

            iterator = collection.iterator();
        }
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
    protected <T extends Comparable<? super T>> Enumerable<T> skip(Collection<T> collection, int count) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(count >= 0);
        Preconditions.checkArgument(count > collection.size());

        return new SkipEnumerable<T>(collection, count);
    }

    class SkipEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        SkipEnumerable(Collection<T> col, int count) {
            collection = new ArrayList<T>();

            if (count == 0) {
                collection.addAll(col);
                return;
            }

            Iterator<T> iter = col.iterator();
            for (int i = 0; i < count; i++) {
                iter.next();
            }

            while (iter.hasNext()) {
                collection.add(iter.next());
            }

            iterator = collection.iterator();
        }
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
    protected <T extends Comparable<? super T>> Enumerable<T> skipWhile(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        return new SkipWhileEnumerable<T>(collection, func);
    }

    class SkipWhileEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        SkipWhileEnumerable(Collection<T> col, Func<T, Boolean> func) {
            collection = new ArrayList<T>();

            Iterator<T> iter = col.iterator();
            while (func.callback(iter.next())) ;

            while (iter.hasNext()) {
                collection.add(iter.next());
            }

            iterator = collection.iterator();
        }
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

        int ret = 0;

        for (Byte b : collection) {
            ret += b;
        }

        return (byte) ret;
    }

    /**
     * Sums up the collection and returns the results
     *
     * @param collection The collection to sum
     * @return The added values of the collection
     */
    protected Double sum(Collection<Double> collection) {
        Preconditions.checkNotNull(collection);

        Double ret = (double) 0;

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

        Float ret = (float) 0;

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

        Integer ret = 0;

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

        Long ret = (long) 0;

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

        int ret = 0;

        for (Short s : collection) {
            ret = ret + s;
        }

        return (short) ret;
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
    protected <T extends Comparable<? super T>> Enumerable<T> take(Collection<T> collection, int count) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(count >= 0);

        return new TakeEnumerable<T>(collection, count);
    }

    class TakeEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        TakeEnumerable(Collection<T> col, int count) {
            if (count == 0 || col.isEmpty())
                return;

            collection = new ArrayList<T>();

            Iterator<T> iter = col.iterator();
            for (int i = 0; i < count; i++) {
                if (iter.hasNext())
                    collection.add(iter.next());
                else
                    throw new IndexOutOfBoundsException();
            }

            iterator = collection.iterator();
        }
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
    protected <T extends Comparable<? super T>> Enumerable<T> takeWhile(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        return new TakeWhileEnumerable<T>(collection, func);
    }

    class TakeWhileEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        TakeWhileEnumerable(Collection<T> col, Func<T, Boolean> func) {
            if (col.isEmpty())
                return;

            collection = new ArrayList<T>();

            for (T next : col) {
                if (func.callback(next))
                    collection.add(next);
            }

            iterator = collection.iterator();
        }
    }

    ///////////////
    // To Map
    ///////////////

//    /**
//     * Projects the given collection to a {@link Map} type using the callback function. The callback will generate a key based on the given value. The resulting key will be paired with the value and added to the map.
//     *
//     * @param collection The collection to project
//     * @param func       The function to generate keys for the values
//     * @param <Key>      The type of the key
//     * @param <Value>    The type of the value
//     * @return The new Map instance containing the keys and values from the collection
//     */
//    protected <Key, Value> Enumerable<Map.Entry<Key, Value>> toMap(Collection<Value> collection, Func<Value, Key> func) {
//        Preconditions.checkNotNull(collection);
//        Preconditions.checkNotNull(func);
//
//        if (collection.isEmpty()) {
//            return new LavaMap<Key, Value>();
//        }
//
//        HashMap<Key, Value> ret = new HashMap<Key, Value>(collection.size());
//
//        for (Value value : collection) {
//            Key key = func.callback(value);
//            ret.put(key, value);
//        }
//
//        return ret;
//    }

    ///////////////
    // To List
    ///////////////

    /**
     * Creates a new {@link List} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return The new List containing the elements in the collection
     */
    protected <T> List<T> toList(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        return new ArrayList<T>(collection);
    }

    ///////////////
    // To Set
    ///////////////

    /**
     * Creates a new {@link Set} from the given collection
     *
     * @param collection The collection to use
     * @param <T>        The type of object in the collection
     * @return A new Set containing the objects in the collection
     */
    protected <T> Set<T> toSet(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        return new HashSet<T>(collection);
    }

    ///////////////
    // Where
    ///////////////

    /**
     * Searches a collection using the given {@link Func callback} function.
     *
     * @param collection The collection to search through
     * @param func       The callback function to search with
     * @param <T>        The type of the object in the list
     * @return A subset of the collection where all of the objects return a match in the callback function.
     */
    protected <T extends Comparable<? super T>> Enumerable<T> where(Collection<T> collection, Func<T, Boolean> func) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        return new WhereEnumerable<T>(collection, func);
    }

    class WhereEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        WhereEnumerable(Collection<T> col, Func<T, Boolean> func) {
            collection = new ArrayList<T>();

            for (T obj : col) {
                if (func.callback(obj))
                    collection.add(obj);
            }

            iterator = collection.iterator();
        }
    }

    //TODO: Add the following methods: Union, Zip
    //TODO: Phase 2: Average, Cast, Concat, ElementAt?, ElementAtOrDefault?, Except, GroupBy, GroupJoin, Join, OfType, Range, Repeat, Reverse

}
