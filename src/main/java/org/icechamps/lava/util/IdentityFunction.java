package org.icechamps.lava.util;

import org.icechamps.lava.callback.Func;

/**
 * User: Robert.Diaz
 * Date: 3/26/13
 * Time: 9:57 PM
 * <p/>
 * Convience function that returns the object passed into it.
 */
public class IdentityFunction<T> implements Func<T, T> {

    @Override
    public T callback(T t) {
        return t;
    }
}
