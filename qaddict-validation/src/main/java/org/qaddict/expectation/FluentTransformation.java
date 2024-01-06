package org.qaddict.expectation;

import org.qaddict.Expectations;
import org.qaddict.Expectation;

@FunctionalInterface
public interface FluentTransformation<V, R> {

    R matching(Expectation<? super V> expectation);

    default R equalTo(V expectedValue) {
        return matching(Expectations.equalTo(expectedValue));
    }

}
