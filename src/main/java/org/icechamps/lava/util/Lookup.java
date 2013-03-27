package org.icechamps.lava.util;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.Func;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * A helper class for the join operation. This provides a way to lookup groups of results based on a common key
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public class Lookup<K, V> {
    private ArrayList<Group<K, V>> groups;
    private Comparator<K> comparator;

    /**
     * Constructor that takes in a collection and a callback function. It then populates the internal group structure with the results of the callback
     *
     * @param collection    The source collection
     * @param func          The callback function used to generate the keys
     * @param keyComparator A comparator that is used to compare the keys. If it is null, a default "==" is used
     */
    public Lookup(Collection<V> collection, Func<V, K> func, Comparator<K> keyComparator) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        groups = new ArrayList<Group<K, V>>();
        comparator = keyComparator;

        for (V v : collection) {
            K key = func.callback(v);

            Group<K, V> group = getGroupForKey(key, true);
            group.add(v);
        }
    }

    /**
     * Looks up a group based on the given key. If createNew is true, we create a new Group using the given key.
     *
     * @param key       The key used in the lookup
     * @param createNew Should we create a new Group if it wasn't found?
     * @return Either the existing Group, a new Group, or null.
     */
    public Group<K, V> getGroupForKey(K key, boolean createNew) {
        for (Group<K, V> g : groups) {
            if (comparator != null && comparator.compare(g.getKey(), key) == 0)
                return g;

            if (g.getKey() == key) {
                return g;
            }
        }

        if (createNew) {
            Group<K, V> group = new Group<K, V>(key);
            groups.add(group);
            return group;
        }

        return null;
    }
}
