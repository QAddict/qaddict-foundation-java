package org.qaddict;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class When {
    private When() {}

    public static <D> Applied<D> expectation(Expectation<D> expectation) {
        return data -> expectedResult -> assertEquals(expectedResult, expectation.test(data));
    }

    public interface Applied<D> {
        Expected appliedOn(D data);
    }

    public interface Expected {
        void shouldReturn(boolean expectedResult);
    }
}
