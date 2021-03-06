package org.icechamps.lava;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.collection.LavaEnumerable;
import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.exception.MultipleElementsFoundException;
import org.icechamps.lava.interfaces.Enumerable;
import org.icechamps.lava.util.Group;
import org.icechamps.lava.util.IdentityFunction;
import org.icechamps.lava.util.Lookup;
import org.icechamps.lava.util.NumberEnum;

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
    // Average
    ///////////////

    /**
     * Averages the collection and returns the results
     *
     * @param collection The collection to average
     * @return The average values of the collection
     */
    protected <T extends Number> Number average(Collection<T> collection) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(!collection.isEmpty());

        Number first = first(collection);
        NumberEnum type = getType(first);

        Number container;

        // We do this switch because it is a tad more efficient than chained ifs
        switch (type) {
            case SHORT:
                container = (short) 0;
                break;
            case BYTE:
                container = (byte) 0;
                break;
            case DOUBLE:
                container = (double) 0;
                break;
            case FLOAT:
                container = (float) 0;
                break;
            case INTEGER:
                container = 0;
                break;
            case LONG:
                container = (long) 0;
                break;
            default:
                throw new UnsupportedOperationException("Invalid NumberEnum type");
        }

        for (Number num : collection) {
            switch (type) {
                case SHORT:
                    container = container.shortValue() + num.shortValue();
                    break;
                case BYTE:
                    container = container.byteValue() + num.byteValue();
                    break;
                case DOUBLE:
                    container = container.doubleValue() + num.doubleValue();
                    break;
                case FLOAT:
                    container = container.floatValue() + num.floatValue();
                    break;
                case INTEGER:
                    container = container.intValue() + num.intValue();
                    break;
                case LONG:
                    container = container.longValue() + num.longValue();
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid NumberEnum type");
            }
        }

        switch (type) {
            case SHORT:
                return container.shortValue() / collection.size();
            case BYTE:
                return container.byteValue() / collection.size();
            case DOUBLE:
                return container.doubleValue() / collection.size();
            case FLOAT:
                return container.floatValue() / collection.size();
            case INTEGER:
                return container.intValue() / collection.size();
            case LONG:
                return container.longValue() / collection.size();
            default:
                throw new UnsupportedOperationException("Invalid NumberEnum type");
        }
    }

    ///////////////
    // Cast
    ///////////////

    /**
     * Casts the untyped collection to a typed Enumerable containing the elements of the collection.
     *
     * @param collection The collection who's elements will be cast
     * @param <T>        The type of object to be cast to
     * @return An Enumerable instance containing all of the elements of the collection in a typed manner
     */
    protected <T extends Comparable<? super T>> Enumerable<T> cast(Collection collection) {
        Preconditions.checkNotNull(collection);
        return new CastEnumerable<T>(collection);
    }

    /**
     * Implements the logic to cast the elements of the collection to type T. We suppress the warnings because of Java's type erasure.
     *
     * @param <T> The type of the object in the collection
     */
    @SuppressWarnings("unchecked")
    class CastEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        public CastEnumerable(Collection source) {
            collection = new ArrayList<T>();

            for (Object obj : source) {
                collection.add((T) obj);
            }
        }
    }

    ///////////////
    // Concat
    ///////////////

    /**
     * Concatenates the two collections together to create a new Enumerable.
     *
     * @param first  The first collection to use
     * @param second The second collection to use
     * @param <T>    The type of the objects in the collections
     * @return A new Enumerable that contains all of the elements from both collections
     */
    protected <T extends Comparable<? super T>> Enumerable<T> concat(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        LavaList<T> ret = new LavaList<T>(first);
        ret.addAll(second);

        return ret;
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
            collection = new HashSet<T>(col);
        }
    }

    ///////////////
    // Element At
    ///////////////

    /**
     * Returns the element in the collection at the specified index. Useful for collections that do not allow positional access.
     *
     * @param collection The collection to use
     * @param index      The index of the element
     * @param <T>        The type of the element
     * @return The element in the collection at the specified index
     */
    @SuppressWarnings("unchecked")
    protected <T extends Comparable<? super T>> T elementAt(Collection<T> collection, int index) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkPositionIndex(index, collection.size());

        if (collection instanceof List) {
            return ((List<T>) collection).get(index);
        }

        return (T) collection.toArray()[index];
    }

    ///////////////
    // Element At Or Default
    ///////////////

    /**
     * Returns the element in the collection at the specified index, or null if the index is out of bounds. Useful for collections that do not allow positional access.
     *
     * @param collection The collection to use
     * @param index      The index of the element
     * @param <T>        The type of the element
     * @return The element in the collection at the specified index or null if the index is out of bounds
     */
    @SuppressWarnings("unchecked")
    protected <T extends Comparable<? super T>> T elementAtOrDefault(Collection<T> collection, int index) {
        Preconditions.checkNotNull(collection);

        if (index >= collection.size())
            return null;

        if (collection instanceof List) {
            return ((List<T>) collection).get(index);
        }

        return (T) collection.toArray()[index];
    }

    ///////////////
    // Except
    ///////////////

    /**
     * Creates an enumerable containing the difference between the two collections
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of the object in the collection
     * @return The enumerable containing the difference
     */
    protected <T extends Comparable<? super T>> Enumerable<T> except(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        return new ExceptEnumerable<T>(first, second);
    }

    /**
     * Enumerable that provides the logic to produce the difference
     *
     * @param <T> The type of object in the collection
     */
    class ExceptEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        public ExceptEnumerable(Collection<T> first, Collection<T> second) {
            collection = new ArrayList<T>();
            ArrayList<T> temp = new ArrayList<T>();

            temp.addAll(second);

            for (T f : first)
                if (!temp.contains(f))
                    collection.add(f);
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
    // Group By
    ///////////////

    /**
     * Groups the elements in the collection using the keys that are generated by the key function.
     *
     * @param collection The collection to group
     * @param keyFunc    The function used to generate keys
     * @param <T>        The type of the object in the collection
     * @param <K>        The type of the key
     * @return An Enumerable instance that contains the elements of collection grouped by the keys that were generated by the callback function
     */
    protected <T, K extends Comparable<? super K>> Enumerable<Group<K, T>> groupBy(Collection<T> collection, Func<T, K> keyFunc) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(keyFunc);

        return new GroupByEnumerator<T, K, T>(collection, keyFunc, new IdentityFunction<T>());
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
    protected <T, K extends Comparable<? super K>, V> Enumerable<Group<K, V>> groupBy(Collection<T> collection, Func<T, K> keyFunc, Func<T, V> valueFunc) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(keyFunc);
        Preconditions.checkNotNull(valueFunc);

        return new GroupByEnumerator<T, K, V>(collection, keyFunc, valueFunc);
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
    protected <T, K extends Comparable<? super K>, Result extends Comparable<? super Result>> Enumerable<Result> groupBy(Collection<T> collection,
                                                                                                                         Func<T, K> keyFunc,
                                                                                                                         Func2<K, Collection<T>, Result> resultFunc) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(keyFunc);

        return new GroupByResultEnumerator<T, K, T, Result>(collection, keyFunc, new IdentityFunction<T>(), resultFunc);
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
    protected <T, K extends Comparable<? super K>, V, Result extends Comparable<? super Result>> Enumerable<Result> groupBy(Collection<T> collection,
                                                                                                                            Func<T, K> keyFunc,
                                                                                                                            Func<T, V> valueFunc,
                                                                                                                            Func2<K, Collection<V>, Result> resultFunc) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(keyFunc);
        Preconditions.checkNotNull(valueFunc);

        return new GroupByResultEnumerator<T, K, V, Result>(collection, keyFunc, valueFunc, resultFunc);
    }

    /**
     * Enumerable that implements the logic for the Group By implementation
     *
     * @param <T> The type of the source object
     * @param <K> The type of the key
     * @param <V> The type of the value
     */
    class GroupByEnumerator<T, K extends Comparable<? super K>, V> extends LavaEnumerable<Group<K, V>> {
        GroupByEnumerator(Collection<T> items, Func<T, K> keyFunc, Func<T, V> valueFunc) {
            collection = new ArrayList<Group<K, V>>();

            Lookup<K, V> lookup = Lookup.create(items, keyFunc, valueFunc, null);
            collection.addAll(lookup.getGroups());
        }
    }

    /**
     * Enumerable that implements the logic for the Group By implementation
     *
     * @param <T> The type of the source object
     * @param <K> The type of the key
     * @param <V> The type of the value
     */
    class GroupByResultEnumerator<T, K extends Comparable<? super K>, V, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        GroupByResultEnumerator(Collection<T> items, Func<T, K> keyFunc, Func<T, V> valueFunc, Func2<K, Collection<V>, Result> resultFunc) {
            collection = new ArrayList<Result>();

            Lookup<K, V> lookup = Lookup.create(items, keyFunc, valueFunc, null);
            collection.addAll(lookup.applyResultFunction(resultFunc));
        }
    }

    ///////////////
    // Group Join
    ///////////////

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
    protected <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> groupJoin(Collection<Outer> outerCollection,
                                                                                                                                          Collection<Inner> innerCollection,
                                                                                                                                          Func<Outer, Key> outerKeyFunc,
                                                                                                                                          Func<Inner, Key> innerKeyFunc,
                                                                                                                                          Func2<Outer, Collection<Inner>, Result> resultFunc) {
        Preconditions.checkNotNull(outerCollection);
        Preconditions.checkNotNull(innerCollection);
        Preconditions.checkNotNull(outerKeyFunc);
        Preconditions.checkNotNull(innerKeyFunc);
        Preconditions.checkNotNull(resultFunc);

        return new GroupJoinEnumerator<Outer, Inner, Key, Result>(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc);
    }

    /**
     * The enumerator instance that provides the logic for the group join
     *
     * @param <Outer>  The type in the first collection
     * @param <Inner>  The type in the second collection
     * @param <Key>    The type of the common key
     * @param <Result> The type of the resulting object
     */
    class GroupJoinEnumerator<Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        GroupJoinEnumerator(Collection<Outer> outerCollection,
                            Collection<Inner> innerCollection,
                            Func<Outer, Key> outerKeyFunc,
                            Func<Inner, Key> innerKeyFunc,
                            Func2<Outer, Collection<Inner>, Result> resultFunc) {
            collection = new ArrayList<Result>();
            Lookup<Key, Inner> lookup = Lookup.createForJoin(innerCollection, innerKeyFunc, null);

            for (Outer outer : outerCollection) {
                collection.add(resultFunc.callback(outer, lookup.getGroupForKey(outerKeyFunc.callback(outer), false).getValues()));
            }
        }
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

        List<T> firstDistinct = distinct(first).toList();
        List<T> secondDistinct = distinct(second).toList();

        return new IntersectEnumerable<T>(firstDistinct, secondDistinct);
    }

    /**
     * Enumerable that implements the intersect functionality
     *
     * @param <T> The type of the object in the enumerable
     */
    class IntersectEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        IntersectEnumerable(List<T> first, List<T> second) {
            collection = new ArrayList<T>();

            for (T f : first)
                for (T s : second)
                    if (f.equals(s))
                        collection.add(f);
        }
    }

    ///////////////
    // Join
    ///////////////

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
    protected <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                                                     Collection<Inner> innerCollection,
                                                                                                                                     Func<Outer, Key> outerKeyFunc,
                                                                                                                                     Func<Inner, Key> innerKeyFunc,
                                                                                                                                     Func2<Outer, Inner, Result> resultFunc) {
        Preconditions.checkArgument(outerCollection != null);
        Preconditions.checkArgument(innerCollection != null);
        Preconditions.checkArgument(outerKeyFunc != null);
        Preconditions.checkArgument(innerKeyFunc != null);
        Preconditions.checkArgument(resultFunc != null);

        return new JoinEnumerable<Outer, Inner, Key, Result>(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc, null);
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
    protected <Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                                                     Collection<Inner> innerCollection,
                                                                                                                                     Func<Outer, Key> outerKeyFunc,
                                                                                                                                     Func<Inner, Key> innerKeyFunc,
                                                                                                                                     Func2<Outer, Inner, Result> resultFunc,
                                                                                                                                     Comparator<Key> keyComparator) {
        Preconditions.checkArgument(outerCollection != null);
        Preconditions.checkArgument(innerCollection != null);
        Preconditions.checkArgument(outerKeyFunc != null);
        Preconditions.checkArgument(innerKeyFunc != null);
        Preconditions.checkArgument(resultFunc != null);
        Preconditions.checkNotNull(keyComparator);

        return new JoinEnumerable<Outer, Inner, Key, Result>(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc, keyComparator);
    }

    /**
     * Enumerable that implements the join functionality
     *
     * @param <Outer>  The type of the outer key
     * @param <Inner>  The type of the inner key
     * @param <Key>    The type of the common join key
     * @param <Result> The type of the resulting object
     */
    class JoinEnumerable<Outer, Inner, Key extends Comparable<? super Key>, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        JoinEnumerable(Collection<Outer> outerCollection,
                       Collection<Inner> innerCollection,
                       Func<Outer, Key> outerKeyFunc,
                       Func<Inner, Key> innerKeyFunc,
                       Func2<Outer, Inner, Result> resultFunc,
                       Comparator<Key> keyComparator) {
            collection = new ArrayList<Result>();

            Lookup<Key, Inner> lookup = Lookup.createForJoin(innerCollection, innerKeyFunc, keyComparator);

            for (Outer outer : outerCollection) {
                Key outerKey = outerKeyFunc.callback(outer);
                Group<Key, Inner> group = lookup.getGroupForKey(outerKey, false);

                if (group != null) {
                    for (Inner inner : group) {
                        collection.add(resultFunc.callback(outer, inner));
                    }
                }
            }
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

        return toList(collection).get(collection.size() - 1);
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

            if (ret.compareTo(t) < 0) {
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

            if (ret.compareTo(e) < 0) {
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

            if (ret.compareTo(t) > 0) {
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

            if (ret.compareTo(e) > 0) {
                ret = e;
            }
        }

        return ret;
    }

    ///////////////
    // Of Type
    ///////////////

    /**
     * Filters the untyped collection and returns only the elements that conform to the given class
     *
     * @param collection The untyped collection to filter
     * @param clazz      The class to filter with
     * @param <T>        The type of the object that is returned
     * @return The filtered collection that contains only the objects of type clazz
     */
    protected <T extends Comparable<? super T>> Enumerable<T> ofType(Collection collection, Class<T> clazz) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(clazz);

        return new OfTypeEnumerator<T>(collection, clazz);
    }

    /**
     * Enumerator instance that implements the logic for the ofType method
     *
     * @param <T> The type of object contained in this Enumerable
     */
    class OfTypeEnumerator<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        @SuppressWarnings("unchecked")
        OfTypeEnumerator(Collection source, Class<T> clazz) {
            collection = new ArrayList<T>();

            for (Object obj : source) {
                if (clazz.isAssignableFrom(obj.getClass())) {
                    collection.add((T) obj);
                }
            }
        }
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
            List<T> list = new ArrayList<T>(col);

            if (comparator != null)
                Collections.sort(list, comparator);
            else
                Collections.sort(list);

            collection = list;
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
            List<T> list = new ArrayList<T>(col);

            if (comparator != null)
                Collections.sort(list, comparator);
            else
                Collections.sort(list);

            Collections.reverse(list);

            collection = list;
        }
    }

    ///////////////
    // Randomize
    ///////////////

    /**
     * Randomizes the given collection
     *
     * @param collection The collection to randomize
     * @param <T>        The type of object in the collection
     * @return The randomized collection
     */
    protected <T extends Comparable<? super T>> Enumerable<T> randomize(Collection<T> collection) {
        return randomize(collection, null);
    }

    /**
     * Randomizes the given collection using the given {@link Random} instance to
     *
     * @param collection The collection to randomize
     * @param random     The {@link Random} instance to use as a seed
     * @param <T>        The type of object in the collection
     * @return The randomized collection
     */
    protected <T extends Comparable<? super T>> Enumerable<T> randomize(Collection<T> collection, Random random) {
        Preconditions.checkNotNull(collection);

        LavaList<T> list = new LavaList<T>(collection);

        if (random != null)
            Collections.shuffle(list, random);
        else
            Collections.shuffle(list);

        return list;
    }

    ///////////////
    // Range
    ///////////////

    /**
     * Generates a sequence of numbers that starts at {@code start} and ends at {@code start + count}
     *
     * @param start The integer to start the counting
     * @param count The number of integers to count
     * @return The sequence of numbers between {@code start} and {@code start + count}
     */
    protected Enumerable<Integer> range(int start, int count) {
        Preconditions.checkArgument(start >= 0);

        LavaList<Integer> ret = new LavaList<Integer>();

        for (int i = start; i < count; i++) {
            ret.add(i);
        }

        return ret;
    }

    ///////////////
    // Repeat
    ///////////////

    /**
     * Creates an enumerable that contains the {@code src} object {@code count} times
     *
     * @param src   The object that will be repeated in the enumerable
     * @param count The number of times to repeat the object
     * @param <T>   The type of the object in the enumerable
     * @return An enumerable containing {@code count src} objects
     */
    protected <T extends Comparable<? super T>> Enumerable<T> repeat(T src, int count) {
        Preconditions.checkNotNull(src);
        Preconditions.checkArgument(count >= 0);

        LavaList<T> ret = new LavaList<T>();

        for (int i = 0; i < count; i++) {
            ret.add(src);
        }

        return ret;
    }

    ///////////////
    // Reverse
    ///////////////

    /**
     * Reverses the collection and wraps the return value in an Enumerable instance
     *
     * @param collection The collection to reverse
     * @param <T>        The type of object in the collection
     * @return The reversed collection wrapped in an Enumerable
     */
    protected <T extends Comparable<? super T>> Enumerable<T> reverse(Collection<T> collection) {
        Preconditions.checkNotNull(collection);

        LavaList<T> ret = new LavaList<T>(collection);
        Collections.reverse(ret);
        return ret;
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
        }
    }

    ///////////////
    // Select Many
    ///////////////

    /**
     * Performs a one to many projection from the source object to a resulting collection
     *
     * @param sourceCollection The source collection
     * @param func             The function that returns a collection based on the source object
     * @param <Source>         The type of the source object
     * @param <Result>         The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    protected <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                Func<Source, Collection<Result>> func) {
        Preconditions.checkNotNull(sourceCollection);
        Preconditions.checkNotNull(func);

        return new SelectManyEnumerable1<Source, Result>(sourceCollection, func);
    }

    class SelectManyEnumerable1<Source, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {

        public SelectManyEnumerable1(Collection<Source> sourceCollection, Func<Source, Collection<Result>> func) {
            collection = new ArrayList<Result>();

            for (Source source : sourceCollection) {
                Collection<Result> results = func.callback(source);

                if (results != null)
                    collection.addAll(results);
            }
        }
    }

    /**
     * Performs a one to many projection from the source object to a resulting collection. Passes the index of each object to the callback function
     *
     * @param sourceCollection The source collection
     * @param func             The function that returns a collection based on the source object
     * @param <Source>         The type of the source object
     * @param <Result>         The type of the result object
     * @return A collection of the resulting objects from the callback function
     */
    protected <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                Func2<Source, Integer, Collection<Result>> func) {
        Preconditions.checkNotNull(sourceCollection);
        Preconditions.checkNotNull(func);

        return new SelectManyEnumerable2<Source, Result>(sourceCollection, func);
    }

    class SelectManyEnumerable2<Source, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {

        public SelectManyEnumerable2(Collection<Source> sourceCollection, Func2<Source, Integer, Collection<Result>> func) {
            collection = new ArrayList<Result>();

            int index = 0;
            for (Iterator<Source> sourceIterator = sourceCollection.iterator(); sourceIterator.hasNext(); index++) {
                Source source = sourceIterator.next();
                Collection<Result> results = func.callback(source, index);

                if (results != null)
                    collection.addAll(results);
            }
        }
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
    protected <Source, TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                             Func<Source, Collection<TCollection>> collectionFunc,
                                                                                                             Func2<Source, TCollection, Result> resultFunc) {
        Preconditions.checkNotNull(sourceCollection);
        Preconditions.checkNotNull(collectionFunc);
        Preconditions.checkNotNull(resultFunc);

        return new SelectManyEnumerable3<Source, TCollection, Result>(sourceCollection, collectionFunc, resultFunc);
    }

    class SelectManyEnumerable3<Source, TCollection, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        SelectManyEnumerable3(Collection<Source> sourceCollection,
                              Func<Source, Collection<TCollection>> collectionFunc,
                              Func2<Source, TCollection, Result> resultFunc) {
            collection = new ArrayList<Result>();

            for (Source source : sourceCollection) {
                for (TCollection tCollection : collectionFunc.callback(source)) {
                    Result result = resultFunc.callback(source, tCollection);

                    if (result != null)
                        collection.add(result);
                }
            }
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
        Preconditions.checkArgument(count < collection.size());

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
        @SuppressWarnings("StatementWithEmptyBody")
        SkipWhileEnumerable(Collection<T> col, Func<T, Boolean> func) {
            collection = new ArrayList<T>();

            Iterator<T> iter = col.iterator();
            // Purposely empty while loop
            while (func.callback(iter.next())) {
            }

            while (iter.hasNext()) {
                collection.add(iter.next());
            }
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
    protected <T extends Number> Number sum(Collection<T> collection) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkArgument(!collection.isEmpty());

        Number first = first(collection);
        NumberEnum type = getType(first);

        Number container;

        // We do this switch because it is a tad more efficient than chained ifs
        switch (type) {
            case SHORT:
                container = (short) 0;
                break;
            case BYTE:
                container = (byte) 0;
                break;
            case DOUBLE:
                container = (double) 0;
                break;
            case FLOAT:
                container = (float) 0;
                break;
            case INTEGER:
                container = 0;
                break;
            case LONG:
                container = (long) 0;
                break;
            default:
                throw new UnsupportedOperationException("Invalid NumberEnum type");
        }

        for (Number num : collection) {
            switch (type) {
                case SHORT:
                    container = container.shortValue() + num.shortValue();
                    break;
                case BYTE:
                    container = container.byteValue() + num.byteValue();
                    break;
                case DOUBLE:
                    container = container.doubleValue() + num.doubleValue();
                    break;
                case FLOAT:
                    container = container.floatValue() + num.floatValue();
                    break;
                case INTEGER:
                    container = container.intValue() + num.intValue();
                    break;
                case LONG:
                    container = container.longValue() + num.longValue();
                    break;
                default:
                    throw new UnsupportedOperationException("Invalid NumberEnum type");
            }
        }

        switch (type) {
            case SHORT:
                return container.shortValue();
            case BYTE:
                return container.byteValue();
            case DOUBLE:
                return container.doubleValue();
            case FLOAT:
                return container.floatValue();
            case INTEGER:
                return container.intValue();
            case LONG:
                return container.longValue();
            default:
                throw new UnsupportedOperationException("Invalid NumberEnum type");
        }
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
                else
                    break;
            }
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
        }
    }

    ///////////////
    // Union
    ///////////////

    /**
     * Creates an enumerable containing the union of the two collections
     *
     * @param first  The first collection
     * @param second The second collection
     * @param <T>    The type of the object in the collection
     * @return The enumerable containing the union
     */
    protected <T extends Comparable<? super T>> Enumerable<T> union(Collection<T> first, Collection<T> second) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);

        return new UnionEnumerable<T>(first, second);
    }

    /**
     * Enumerable that provides the logic to produce the union
     *
     * @param <T> The type of object in the collection
     */
    class UnionEnumerable<T extends Comparable<? super T>> extends LavaEnumerable<T> {
        public UnionEnumerable(Collection<T> first, Collection<T> second) {
            collection = new HashSet<T>();

            for (T f : first)
                if (!collection.contains(f))
                    collection.add(f);

            for (T s : second)
                if (!collection.contains(s))
                    collection.add(s);
        }
    }

    ///////////////
    // Zip
    ///////////////

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
    protected <First, Second, Result extends Comparable<? super Result>> Enumerable<Result> zip(Collection<First> first,
                                                                                                Collection<Second> second,
                                                                                                Func2<First, Second, Result> func) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(func);

        return new ZipEnumerable<First, Second, Result>(first, second, func);
    }

    /**
     * Enumerable that provides the logic to produce the mapping
     *
     * @param <First>  The type of the first object
     * @param <Second> The type of the second object
     * @param <Result> The type of the mapped object
     */
    class ZipEnumerable<First, Second, Result extends Comparable<? super Result>> extends LavaEnumerable<Result> {
        public ZipEnumerable(Collection<First> first, Collection<Second> second, Func2<First, Second, Result> func) {
            collection = new ArrayList<Result>();
            Iterator<First> firstIterator = first.iterator();
            Iterator<Second> secondIterator = second.iterator();

            while (firstIterator.hasNext() || secondIterator.hasNext()) {
                Result result = func.callback(firstIterator.next(), secondIterator.next());
                if (result != null)
                    collection.add(result);
            }
        }
    }

    /**
     * Determines the type of Number that is passed in.
     *
     * @param number The number to check
     * @return The kind of number was passed in
     * @throws UnsupportedOperationException If the Number instance is not a standard type
     */
    private NumberEnum getType(Number number) {
        if (number.getClass() == Byte.class) return NumberEnum.BYTE;
        if (number.getClass() == Double.class) return NumberEnum.DOUBLE;
        if (number.getClass() == Float.class) return NumberEnum.FLOAT;
        if (number.getClass() == Integer.class) return NumberEnum.INTEGER;
        if (number.getClass() == Long.class) return NumberEnum.LONG;
        if (number.getClass() == Short.class) return NumberEnum.SHORT;

        throw new UnsupportedOperationException("Unsupported Number class");
    }
}
