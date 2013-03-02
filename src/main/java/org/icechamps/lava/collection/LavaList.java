package org.icechamps.lava.collection;

import com.google.common.base.Preconditions;
import org.icechamps.lava.LavaBase;
import org.icechamps.lava.interfaces.LavaCollection;

import java.util.*;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 5:02 PM
 * <p/>
 * <p>
 * A collection of objects of type {@code T} that implements the {@link org.icechamps.lava.interfaces.LavaCollection} interface.
 * This allows for method chaining.
 * </p>
 * <p>
 * The default implementation of this class uses an ArrayList under the covers. By passing
 * an instance of a different type into the constructor, one can change the backing object.
 * </p>
 */
public class LavaList<T> extends LavaBase implements List<T>, LavaCollection<T> {
    private List<T> sourceList;

    public LavaList() {
        sourceList = new ArrayList<T>();
    }

    public LavaList(Collection<T> collection) {
        Preconditions.checkArgument(collection != null);

        if (collection instanceof List)
            sourceList = (List<T>) collection;
        else {
            sourceList = new ArrayList<T>(collection.size());
            for (T t : collection) {
                sourceList.add(t);
            }
        }
    }

    @Override
    public LavaCollection<T> distinct() {
        return distinct(this);
    }

    @Override
    public <E> LavaList<E> select(SelectOneCallback<T, E> callback) {
        return select(this, callback);
    }

    @Override
    public LavaList<T> where(MatchOneCallback<T> callback) {
        return where(this, callback);
    }

    @Override
    public int size() {
        return sourceList.size();
    }

    @Override
    public boolean isEmpty() {
        return sourceList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return sourceList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return sourceList.iterator();
    }

    @Override
    public Object[] toArray() {
        return sourceList.toArray();
    }

    @Override
    public <T1 extends Object> T1[] toArray(T1[] t1s) {
        return sourceList.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return sourceList.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return sourceList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return sourceList.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        return sourceList.addAll(ts);
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        return sourceList.addAll(i, ts);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return sourceList.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return sourceList.retainAll(objects);
    }

    @Override
    public void clear() {
        sourceList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return sourceList.equals(o);
    }

    @Override
    public int hashCode() {
        return sourceList.hashCode();
    }

    @Override
    public T get(int i) {
        return sourceList.get(i);
    }

    @Override
    public T set(int i, T t) {
        return sourceList.set(i, t);
    }

    @Override
    public void add(int i, T t) {
        sourceList.add(i, t);
    }

    @Override
    public T remove(int i) {
        return sourceList.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return sourceList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return sourceList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return sourceList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int i) {
        return sourceList.listIterator(i);
    }

    @Override
    public List<T> subList(int i, int i2) {
        return sourceList.subList(i, i2);
    }
}
