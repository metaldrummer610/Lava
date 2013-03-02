package org.icechamps.lava;

import org.icechamps.lava.collection.LavaList;
import org.icechamps.lava.collection.LavaSet;
import org.icechamps.lava.callback.MatchOneCallback;

import java.util.List;
import java.util.Set;

/**
 * User: Robert.Diaz
 * Date: 2/27/13
 * Time: 4:12 PM
 * <p/>
 * Main class in the Lava library.
 * This allows static access to methods that act on standard java collection types.
 * Each method returns a Lava* collection type so that method calls can be chained.
 */
public class Lava {
	private static LavaBase lavaBase = new LavaBase();

	public static <T extends Object> LavaList<T> where(List<T> list, MatchOneCallback<T> searchCriteria) {
		return lavaBase.where(list, searchCriteria);
	}

	public static <T extends Object> LavaSet<T> where(Set<T> set, MatchOneCallback<T> searchCriteria) {
		return lavaBase.where(set, searchCriteria);
	}
}
