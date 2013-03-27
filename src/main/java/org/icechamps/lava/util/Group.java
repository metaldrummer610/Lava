package org.icechamps.lava.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a grouping of values for a single key
 *
 * @param <K> The type of the key
 * @param <V> The type of the values being held
 */
public class Group<K, V> implements Iterable<V> {
    private K key;
    private ArrayList<V> values;

    /**
     * Constructor that creates a new group using the given key
     *
     * @param k The key that represents this group
     */
    public Group(K k) {
        key = k;
        values = new ArrayList<V>();
    }

    /**
     * Adds a new value to the internal collection
     *
     * @param value The value to add
     */
    public void add(V value) {
        values.add(value);
    }

    /**
     * Provides a convenient method for iterating over the collection
     *
     * @return The iterator for the internal collection
     */
    @Override
    public Iterator<V> iterator() {
        return values.iterator();
    }

    public K getKey() {
        return key;
    }

    public ArrayList<V> getValues() {
        return values;
    }
}
