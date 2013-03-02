package org.icechamps.lava.collection;

import org.icechamps.lava.LavaBase;
import org.icechamps.lava.LavaFlags;
import org.icechamps.lava.callback.MatchTwoCallback;
import org.icechamps.lava.callback.SelectTwoCallback;
import org.icechamps.lava.interfaces.ILavaMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Robert.Diaz
 * Date: 2/28/13
 * Time: 11:15 AM
 */
public class LavaMap<K, V> extends LavaBase implements Map<K, V>, ILavaMap<K, V> {
    private Map<K, V> map;

    /**
     * Default constructor. Uses a {@link HashMap} by default.
     */
    public LavaMap() {
        map = new HashMap<K, V>();
    }

    /**
     * Special constructor that allows one to either prepopulate the backing map, or change the default backing map.
     *
     * @param map The map to use as the new backing object.
     */
    public LavaMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public LavaMap<K, V> distinct() {
        return distinct(this);
    }

    @Override
    public LavaMap<K, V> distinct(LavaFlags flags) {
        return distinct(this, flags);
    }

    @Override
    public <T> LavaMap<K, T> select(SelectTwoCallback<K, V, T> callback) {
        return select(this, callback);
    }

    @Override
    public LavaMap<K, V> where(MatchTwoCallback<K, V> callback) {
        return where(this, callback);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public V get(Object o) {
        return map.get(o);
    }

    @Override
    public V put(K k, V v) {
        return map.put(k, v);
    }

    @Override
    public V remove(Object o) {
        return map.remove(o);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
