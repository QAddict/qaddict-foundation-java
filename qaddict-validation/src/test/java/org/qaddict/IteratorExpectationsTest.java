package org.qaddict;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.qaddict.Expectations.collectionEquals;
import static org.qaddict.Expectations.endsWith;
import static org.qaddict.Expectations.startsWith;
import static org.qaddict.When.expectation;

public class IteratorExpectationsTest {

    private static final String A = "A", B = "B", AB = "AB";

    @Test public void t001() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t002() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(false); }
    @Test public void t003() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(false); }
    @Test public void t004() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t005() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t006() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }

}
