package org.icechamps.lava.collection;

import org.icechamps.lava.LavaBase;
import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;
import org.icechamps.lava.interfaces.Enumerable;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 3/4/13
 * Time: 6:16 PM
 */
public abstract class LavaEnumerable<T extends Comparable<? super T>> extends LavaBase implements Enumerable<T>, Iterator<T> {
    protected Collection<T> collection;

    // Iterable method

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    // Iterator methods

    @Override
    public boolean hasNext() {
        return iterator().hasNext();
    }

    @Override
    public T next() {
        return iterator().next();
    }

    @Override
    public void remove() {
        iterator().remove();
    }

    // Lava methods

    @Override
    public <V> V aggregate(Func2<T, V, V> func) {
        return aggregate(collection, func);
    }

    @Override
    public boolean all(Func<T, Boolean> func) {
        return all(collection, func);
    }

    @Override
    public boolean any() {
        return any(collection);
    }

    @Override
    public int count() {
        return count(collection);
    }

    @Override
    public Enumerable<T> distinct() {
        return distinct(collection);
    }

    @Override
    public T elementAt(int index) {
        return elementAt(collection, index);
    }

    @Override
    public T elementAtOrDefault(int index) {
        return elementAtOrDefault(collection, index);
    }

    @Override
    public Enumerable<T> except(Collection<T> second) {
        return except(collection, second);
    }

    @Override
    public T first() {
        return first(collection);
    }

    @Override
    public T first(Func<T, Boolean> func) {
        return first(collection, func);
    }

    @Override
    public T firstOrDefault() {
        return firstOrDefault(collection);
    }

    @Override
    public T firstOrDefault(Func<T, Boolean> func) {
        return firstOrDefault(collection, func);
    }

    @Override
    public Enumerable<T> intersect(Collection<T> second) {
        return intersect(collection, second);
    }

    @Override
    public T last() {
        return last(collection);
    }

    @Override
    public T last(Func<T, Boolean> func) {
        return last(collection, func);
    }

    @Override
    public T lastOrDefault() {
        return lastOrDefault(collection);
    }

    @Override
    public T lastOrDefault(Func<T, Boolean> func) {
        return lastOrDefault(collection, func);
    }

    @Override
    public T max() {
        return max(collection);
    }

    @Override
    public <E extends Comparable<? super E>> E max(Func<T, E> func) {
        return max(collection, func);
    }

    @Override
    public T min() {
        return min(collection);
    }

    @Override
    public <E extends Comparable<? super E>> E min(Func<T, E> func) {
        return min(collection, func);
    }

    @Override
    public Enumerable<T> orderByDescending(Comparator<T> comparator) {
        return orderByDescending(collection, comparator);
    }

    @Override
    public Enumerable<T> orderByDescending() {
        return orderByDescending(collection);
    }

    @Override
    public Enumerable<T> orderBy(Comparator<T> comparator) {
        return orderBy(collection, comparator);
    }

    @Override
    public Enumerable<T> orderBy() {
        return orderBy(collection);
    }

    @Override
    public <E extends Comparable<? super E>> Enumerable<E> select(Func<T, E> func) {
        return select(collection, func);
    }

    @Override
    public <Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func<T, Collection<Result>> resultFunc) {
        return selectMany(collection, resultFunc);
    }

    @Override
    public <Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func2<T, Integer, Collection<Result>> resultFunc) {
        return selectMany(collection, resultFunc);
    }

    @Override
    public <TCollection, Result extends Comparable<? super Result>> Enumerable<Result> selectMany(Func<T, Collection<TCollection>> collectionFunc, Func2<T, TCollection, Result> resultFunc) {
        return selectMany(collection, collectionFunc, resultFunc);
    }

    @Override
    public boolean sequenceEqual(Collection<T> second) {
        return sequenceEqual(collection, second);
    }

    @Override
    public T single(Func<T, Boolean> func) {
        return single(collection, func);
    }

    @Override
    public T singleOrDefault(Func<T, Boolean> func) {
        return singleOrDefault(collection, func);
    }

    @Override
    public Enumerable<T> skip(int count) {
        return skip(collection, count);
    }

    @Override
    public Enumerable<T> skipWhile(Func<T, Boolean> func) {
        return skipWhile(collection, func);
    }

    @Override
    public Enumerable<T> take(int count) {
        return take(collection, count);
    }

    @Override
    public Enumerable<T> takeWhile(Func<T, Boolean> func) {
        return takeWhile(collection, func);
    }

    public List<T> toList() {
        return toList(collection);
    }

    @Override
    public Set<T> toSet() {
        return toSet(collection);
    }

    @Override
    public Enumerable<T> where(Func<T, Boolean> func) {
        return where(collection, func);
    }

    @Override
    public Enumerable<T> union(Collection<T> second) {
        return union(collection, second);
    }

    @Override
    public <Second, Result extends Comparable<? super Result>> Enumerable<Result> zip(Collection<Second> second, Func2<T, Second, Result> func) {
        return zip(collection, second, func);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Collection && collection.equals(o);
    }

    @Override
    public int hashCode() {
        return collection.hashCode();
    }
}
