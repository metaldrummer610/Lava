package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.interfaces.Enumerable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

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

    public static <T, V> V aggregate(Collection<T> collection, Func2<T, V, V> func) {
        return lavaBase.aggregate(collection, func);
    }

    public static <T extends Comparable<? super T>> boolean all(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.all(collection, func);
    }

    public static <T extends Comparable<? super T>> boolean any(Collection<T> collection) {
        return lavaBase.any(collection);
    }

    public static Byte average(Collection<Byte> collection) {
        return lavaBase.average(collection);
    }

    public static Double average(Collection<Double> collection) {
        return lavaBase.average(collection);
    }

    public static Float average(Collection<Float> collection) {
        return lavaBase.average(collection);
    }

    public static Integer average(Collection<Integer> collection) {
        return lavaBase.average(collection);
    }

    public static Long average(Collection<Long> collection) {
        return lavaBase.average(collection);
    }

    public static Short average(Collection<Short> collection) {
        return lavaBase.average(collection);
    }

    @SuppressWarnings("RedundantTypeArguments")
    public static <T extends Comparable<? super T>> Enumerable<T> cast(Collection collection) {
        // Dev note: The <T> is REQUIRED because of some type casting thing. Not sure why, but if you remove it, javac complains to no end with cryptic error messages
        return lavaBase.<T>cast(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> concat(Collection<T> first, Collection<T> second) {
        return lavaBase.concat(first, second);
    }

    public static <T extends Comparable<? super T>> int count(Collection<T> collection) {
        return lavaBase.count(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> distinct(Collection<T> collection) {
        return lavaBase.distinct(collection);
    }

    public static  <T extends Comparable<? super T>> T elementAt(Collection<T> collection, int index) {
        return lavaBase.elementAt(collection, index);
    }

    public static  <T extends Comparable<? super T>> T elementAtOrDefault(Collection<T> collection, int index) {
        return lavaBase.elementAtOrDefault(collection, index);
    }

    public static <T extends Comparable<? super T>> T first(Collection<T> collection) {
        return lavaBase.first(collection);
    }

    public static <T extends Comparable<? super T>> T first(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.first(collection, func);
    }

    public static <T extends Comparable<? super T>> T firstOrDefault(Collection<T> collection) {
        return lavaBase.firstOrDefault(collection);
    }

    public static <T extends Comparable<? super T>> T firstOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.firstOrDefault(collection, func);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> intersect(Collection<T> first, Collection<T> second) {
        return lavaBase.intersect(first, second);
    }

    public static <Outer, Inner, Key, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                         Collection<Inner> innerCollection,
                                                                                                         Func<Outer, Key> outerKeyFunc,
                                                                                                         Func<Inner, Key> innerKeyFunc,
                                                                                                         Func2<Outer, Inner, Result> resultFunc) {
        return lavaBase.join(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc);
    }

    public static <Outer, Inner, Key, Result extends Comparable<? super Result>> Enumerable<Result> join(Collection<Outer> outerCollection,
                                                                                                         Collection<Inner> innerCollection,
                                                                                                         Func<Outer, Key> outerKeyFunc,
                                                                                                         Func<Inner, Key> innerKeyFunc,
                                                                                                         Func2<Outer, Inner, Result> resultFunc,
                                                                                                         Comparator<Key> keyComparator) {
        return lavaBase.join(outerCollection, innerCollection, outerKeyFunc, innerKeyFunc, resultFunc, keyComparator);
    }

    public static <T extends Comparable<? super T>> T last(Collection<T> collection) {
        return lavaBase.last(collection);
    }

    public static <T extends Comparable<? super T>> T last(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.last(collection, func);
    }

    public static <T extends Comparable<? super T>> T lastOrDefault(Collection<T> collection) {
        return lavaBase.lastOrDefault(collection);
    }

    public static <T extends Comparable<? super T>> T lastOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.lastOrDefault(collection, func);
    }

    public static <T extends Comparable<? super T>> T max(Collection<T> collection) {
        return lavaBase.max(collection);
    }

    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> E max(Collection<T> collection, Func<T, E> func) {
        return lavaBase.max(collection, func);
    }

    public static <T extends Comparable<? super T>> T min(Collection<T> collection) {
        return lavaBase.min(collection);
    }

    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> E min(Collection<T> collection, Func<T, E> func) {
        return lavaBase.min(collection, func);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection) {
        return lavaBase.orderBy(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> orderBy(Collection<T> collection, Comparator<T> comparator) {
        return lavaBase.orderBy(collection, comparator);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection) {
        return lavaBase.orderByDescending(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> orderByDescending(Collection<T> collection, Comparator<T> comparator) {
        return lavaBase.orderByDescending(collection, comparator);
    }

    public static <T extends Comparable<? super T>, E extends Comparable<? super E>> Enumerable<E> select(Collection<T> collection, Func<T, E> func) {
        return lavaBase.select(collection, func);
    }

    public static <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                    Func<Source, Collection<Result>> resultFunc) {
        return lavaBase.selectMany(sourceCollection, resultFunc);
    }

    public static <Source, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                    Func2<Source, Integer, Collection<Result>> resultFunc) {
        return lavaBase.selectMany(sourceCollection, resultFunc);
    }

    public static <Source, TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Collection<Source> sourceCollection,
                                                                                                                 Func<Source, Collection<TCollection>> collectionFunc,
                                                                                                                 Func2<Source, TCollection, Result> resultFunc) {
        return lavaBase.selectMany(sourceCollection, collectionFunc, resultFunc);
    }

    public static <T extends Comparable<? super T>> boolean sequenceEqual(Collection<T> first, Collection<T> second) {
        return lavaBase.sequenceEqual(first, second);
    }

    public static <T extends Comparable<? super T>> T single(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.single(collection, func);
    }

    public static <T extends Comparable<? super T>> T singleOrDefault(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.singleOrDefault(collection, func);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> skip(Collection<T> collection, int count) {
        return lavaBase.skip(collection, count);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> skipWhile(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.skipWhile(collection, func);
    }

    public static Byte sum(Collection<Byte> collection) {
        return lavaBase.sum(collection);
    }

    public static Double sum(Collection<Double> collection) {
        return lavaBase.sum(collection);
    }

    public static Float sum(Collection<Float> collection) {
        return lavaBase.sum(collection);
    }

    public static Integer sum(Collection<Integer> collection) {
        return lavaBase.sum(collection);
    }

    public static Long sum(Collection<Long> collection) {
        return lavaBase.sum(collection);
    }

    public static Short sum(Collection<Short> collection) {
        return lavaBase.sum(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> take(Collection<T> collection, int count) {
        return lavaBase.take(collection, count);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> takeWhile(Collection<T> collection, Func<T, Boolean> func) {
        return lavaBase.takeWhile(collection, func);
    }

    public static <T extends Comparable<? super T>> List<T> toList(Collection<T> collection) {
        return lavaBase.toList(collection);
    }

    public static <T extends Comparable<? super T>> Set<T> toSet(Collection<T> collection) {
        return lavaBase.toSet(collection);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> where(Collection<T> collection, Func<T, Boolean> searchCriteria) {
        return lavaBase.where(collection, searchCriteria);
    }

    public static <T extends Comparable<? super T>> Enumerable<T> union(Collection<T> first, Collection<T> second) {
        return lavaBase.union(first, second);
    }

    public static <First, Second, Result extends Comparable<? super Result>> Enumerable<Result> zip(Collection<First> first,
                                                                                                    Collection<Second> second,
                                                                                                    Func2<First, Second, Result> func) {
        return lavaBase.zip(first, second, func);
    }
}
