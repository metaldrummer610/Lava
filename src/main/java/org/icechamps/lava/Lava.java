package org.icechamps.lava;

import org.icechamps.lava.callback.Func;
import org.icechamps.lava.interfaces.LavaCollection;

import java.util.Collection;

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

	public static <T extends Object> LavaCollection<T> where(Collection<T> collection, Func<T, Boolean> searchCriteria) {
		return lavaBase.where(collection, searchCriteria);
	}
}
