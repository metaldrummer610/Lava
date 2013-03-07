package org.icechamps.lava.collection;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 5:02 PM
 * <p/>
 * <p>
 * A collection of objects of type {@code T} that implements the {@link org.icechamps.lava.interfaces.Enumerable} interface.
 * This allows for method chaining.
 * </p>
 * <p>
 * The default implementation of this class uses an ArrayList under the covers. By passing
 * an instance of a different type into the constructor, one can change the backing object.
 * </p>
 */
public class LavaList<T extends Comparable<? super T>> extends LavaEnumerable<T> implements List<T> {
    public LavaList() {
        collection = new ArrayList<T>();
    }

    public LavaList(Collection<T> col) {
        Preconditions.checkArgument(col != null);

        collection = new ArrayList<T>(col.size());
        for (T t : col) {
            collection.add(t);
        }
    }

    private List<T> asList() {
        return (List<T>) collection;
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1 extends Object> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return collection.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return collection.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return collection.addAll(c);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        return asList().addAll(i, ts);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return collection.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return collection.retainAll(c);
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public T get(int i) {
        return asList().get(i);
    }

    @Override
    public T set(int i, T t) {
        return asList().set(i, t);
    }

    @Override
    public void add(int i, T t) {
        asList().add(i, t);
    }

    @Override
    public T remove(int i) {
        return asList().remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return asList().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return asList().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return asList().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return asList().listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i2) {
        return asList().subList(i, i2);
    }
}