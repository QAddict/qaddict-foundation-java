package org.qaddict.expectation.algo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qaddict.Assert;
import org.qaddict.algo.MaximumMatching;

import java.util.List;
import java.util.Map;

import static org.qaddict.Expectations.equalTo;

public class MaximumMatchingTest {

    private enum A {a,b,c}
    private enum Y {x,y,z}

    @Test
    public void test() {
        var matching = new MaximumMatching<A, Y>();
        matching.update(A.a, List.of(Y.x, Y.y));
        matching.update(A.b, List.of(Y.x));
        Assert.that(matching.pairing().size(), equalTo(2));
    }


    @Test
    public void test2() {
        var matching = new MaximumMatching<A, Y>();
        matching.update(A.a, List.of(Y.x, Y.y, Y.z));
        matching.update(A.b, List.of(Y.x));
        matching.update(A.c, List.of(Y.x));
        Assertions.assertEquals(matching.pairing().size(), 2);
    }

    @Test
    public void test3() {
        var matching = new MaximumMatching<A, Y>();
        matching.update(A.a, List.of(Y.x));
        Map<Y, A> update = matching.update(A.b, List.of(Y.x));
        Assertions.assertEquals(1, update.size());
    }

}
