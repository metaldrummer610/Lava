package org.icechamps.lava.util;

import com.google.common.base.Preconditions;
import org.icechamps.lava.callback.Func;
import org.icechamps.lava.callback.Func2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * A helper class for the join operation. This provides a way to lookup groups of results based on a common key
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 */
public class Lookup<K extends Comparable<? super K>, V> {
    private ArrayList<Group<K, V>> groups;
    private Comparator<K> comparator;

    public static <T, K extends Comparable<? super K>, V> Lookup<K, V> create(Collection<T> source, Func<T, K> keyFunc, Func<T, V> valueFunc, Comparator<K> keyComparator) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(keyFunc);
        Preconditions.checkNotNull(valueFunc);
        Preconditions.checkArgument(!source.isEmpty());

        Lookup<K, V> lookup = new Lookup<K, V>(keyComparator);

        for (T t : source) {
            lookup.getGroupForKey(keyFunc.callback(t), true).add(valueFunc.callback(t));
        }

        return lookup;
    }

    /**
     * Constructor method that takes in a collection and a callback function. It then populates the internal group structure with the results of the callback
     *
     * @param collection    The source collection
     * @param func          The callback function used to generate the keys
     * @param keyComparator A comparator that is used to compare the keys. If it is null, a default "==" is used
     */
    public static <K extends Comparable<? super K>, V> Lookup<K, V> createForJoin(Collection<V> collection, Func<V, K> func, Comparator<K> keyComparator) {
        Preconditions.checkNotNull(collection);
        Preconditions.checkNotNull(func);

        Lookup<K, V> lookup = new Lookup<K, V>(keyComparator);

        for (V v : collection) {
            K key = func.callback(v);

            Group<K, V> group = lookup.getGroupForKey(key, true);
            group.add(v);
        }

        return lookup;
    }

    private Lookup(Comparator<K> comparator) {
        this.comparator = comparator;
        groups = new ArrayList<Group<K, V>>();
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

    /**
     * Applys a transformation function on the groups in this lookup.
     *
     * @param resultFunc The function that transforms the groups in this lookup into a single object
     * @param <Result>   The type of object each group is transformed into
     * @return An Enumerable instance containing the transformed results of each group
     */
    public <Result> Collection<Result> applyResultFunction(Func2<K, Collection<V>, Result> resultFunc) {
        ArrayList<Result> ret = new ArrayList<Result>();

        for (Group<K, V> group : groups) {
            ret.add(resultFunc.callback(group.getKey(), group.getValues()));
        }

        return ret;
    }

    public ArrayList<Group<K, V>> getGroups() {
        return groups;
    }
}
