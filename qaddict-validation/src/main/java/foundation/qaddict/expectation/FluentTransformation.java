package foundation.qaddict.expectation;

import foundation.qaddict.Expectations;
import foundation.qaddict.Expectation;

@FunctionalInterface
public interface FluentTransformation<V, R> {

    R matching(Expectation<? super V> expectation);

    default R equalTo(V expectedValue) {
        return matching(Expectations.equalTo(expectedValue));
    }

}
