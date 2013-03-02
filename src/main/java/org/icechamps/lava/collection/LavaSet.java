package org.icechamps.lava.collection;

import org.icechamps.lava.LavaBase;
import org.icechamps.lava.callback.MatchOneCallback;
import org.icechamps.lava.callback.SelectOneCallback;
import org.icechamps.lava.interfaces.ILavaCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 7:17 PM
 */
public class LavaSet<T> extends LavaBase implements Set<T>, ILavaCollection<T, LavaSet> {
    private Set<T> set;

    public LavaSet() {
        this.set = new HashSet<T>();
    }

    public LavaSet(Set<T> set) {
        this.set = set;
    }

    @Override
    public LavaSet<T> distinct() {
        return distinct(this);
    }

    @Override
    public <E> LavaSet<E> select(SelectOneCallback<T, E> callback) {
        return select(this, callback);
    }

    @Override
    public LavaSet<T> where(MatchOneCallback<T> callback) {
        return where(this, callback);
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1 extends Object> T1[] toArray(T1[] t1s) {
        return set.toArray(t1s);
    }

    @Override
    public boolean add(T t) {
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return set.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        return set.addAll(ts);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return set.retainAll(objects);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return set.removeAll(objects);
    }

    @Override
    public void clear() {
        set.clear();
    }
}
