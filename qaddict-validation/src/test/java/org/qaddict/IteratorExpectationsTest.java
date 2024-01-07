package org.qaddict;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.qaddict.Expectations.collectionContains;
import static org.qaddict.Expectations.collectionContainsInAnyOrder;
import static org.qaddict.Expectations.collectionEquals;
import static org.qaddict.Expectations.collectionEqualsInAnyOrder;
import static org.qaddict.Expectations.collectionStartsInAnyOrderWith;
import static org.qaddict.Expectations.collectionStartsWith;
import static org.qaddict.Expectations.endsWith;
import static org.qaddict.Expectations.everyElement;
import static org.qaddict.Expectations.existsElement;
import static org.qaddict.Expectations.startsWith;
import static org.qaddict.When.expectation;

public class IteratorExpectationsTest {

    private static final String A = "A";
    private static final String B = "B";

    @Test public void t001() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t002() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(false); }
    @Test public void t003() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(false); }
    @Test public void t004() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t005() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t006() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t007() { expectation(collectionEquals(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(false); }

    @Test public void t011() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t012() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(true); }
    @Test public void t013() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(true); }
    @Test public void t014() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t015() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t016() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t017() { expectation(collectionContains(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(false); }

    @Test public void t021() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t022() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(false); }
    @Test public void t023() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(true); }
    @Test public void t024() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t025() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t026() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t027() { expectation(collectionStartsWith(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(false); }

    @Test public void t031() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t032() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(false); }
    @Test public void t033() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(false); }
    @Test public void t034() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t035() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t036() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t037() { expectation(collectionEqualsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(true); }

    @Test public void t041() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t042() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(true); }
    @Test public void t043() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(true); }
    @Test public void t044() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t045() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t046() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t047() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(true); }
    @Test public void t048() { expectation(collectionContainsInAnyOrder(startsWith(A), endsWith(B))).appliedOn(List.of("X", B, "Y", A)).shouldReturn(true); }

    @Test public void t051() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, B)).shouldReturn(true); }
    @Test public void t052() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, A, B)).shouldReturn(false); }
    @Test public void t053() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(A, B, B)).shouldReturn(true); }
    @Test public void t054() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(A)).shouldReturn(false); }
    @Test public void t055() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(B)).shouldReturn(false); }
    @Test public void t056() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t057() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(B, A)).shouldReturn(true); }
    @Test public void t058() { expectation(collectionStartsInAnyOrderWith(startsWith(A), endsWith(B))).appliedOn(List.of(B, A, A)).shouldReturn(true); }

    @Test public void t061() { expectation(everyElement(A)).appliedOn(List.of()).shouldReturn(true);}
    @Test public void t062() { expectation(everyElement(A)).appliedOn(List.of(A)).shouldReturn(true);}
    @Test public void t063() { expectation(everyElement(A)).appliedOn(List.of(A, A, A)).shouldReturn(true);}
    @Test public void t064() { expectation(everyElement(A)).appliedOn(List.of(A, B)).shouldReturn(false);}
    @Test public void t065() { expectation(everyElement(A)).appliedOn(List.of(B, A)).shouldReturn(false);}
    @Test public void t066() { expectation(everyElement(A)).appliedOn(List.of(B, B)).shouldReturn(false);}
    @Test public void t067() { expectation(everyElement(A)).appliedOn(null).shouldReturn(false);}

    @Test public void t071() { expectation(existsElement(A)).appliedOn(List.of()).shouldReturn(false);}
    @Test public void t072() { expectation(existsElement(A)).appliedOn(List.of(A)).shouldReturn(true);}
    @Test public void t073() { expectation(existsElement(A)).appliedOn(List.of(A, A, A)).shouldReturn(true);}
    @Test public void t074() { expectation(existsElement(A)).appliedOn(List.of(A, B)).shouldReturn(true);}
    @Test public void t075() { expectation(existsElement(A)).appliedOn(List.of(B, A)).shouldReturn(true);}
    @Test public void t076() { expectation(existsElement(A)).appliedOn(List.of(B, B)).shouldReturn(false);}
    @Test public void t077() { expectation(existsElement(A)).appliedOn(null).shouldReturn(false);}

}
