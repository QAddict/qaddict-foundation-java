package org.qaddict;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.qaddict.Expectations.allOf;
import static org.qaddict.Expectations.anyOf;
import static org.qaddict.Expectations.contains;
import static org.qaddict.Expectations.endsWith;
import static org.qaddict.Expectations.has;
import static org.qaddict.Expectations.startsWith;
import static org.qaddict.When.expectation;

public class OperatorExpectationsTest {

    private static final String A = "A", B = "B", AB = "AB";

    @Test public void t001() { expectation(allOf(startsWith(A), endsWith(B))).appliedOn(AB).shouldReturn(true); }
    @Test public void t002() { expectation(allOf(startsWith(A), endsWith(B))).appliedOn("ACB").shouldReturn(true); }
    @Test public void t003() { expectation(allOf(startsWith(A), endsWith(B))).appliedOn("ABC").shouldReturn(false); }
    @Test public void t004() { expectation(allOf(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t005() { expectation(allOf(startsWith(A))).appliedOn(AB).shouldReturn(true); }
    @Test public void t006() { expectation(allOf(startsWith(A), contains("C"), endsWith(B))).appliedOn("ACB").shouldReturn(true); }
    @Test public void t007() { expectation(allOf()).appliedOn(null).shouldReturn(true); }

    @Test public void t008() { expectation(anyOf(startsWith(A), endsWith(B))).appliedOn(AB).shouldReturn(true); }
    @Test public void t009() { expectation(anyOf(startsWith(A), endsWith(B))).appliedOn("ACB").shouldReturn(true); }
    @Test public void t010() { expectation(anyOf(startsWith(A), endsWith(B))).appliedOn("ABC").shouldReturn(true); }
    @Test public void t011() { expectation(anyOf(startsWith(A), endsWith(B))).appliedOn(B).shouldReturn(true); }
    @Test public void t012() { expectation(anyOf(startsWith(A), endsWith(B))).appliedOn(null).shouldReturn(false); }
    @Test public void t013() { expectation(anyOf(startsWith(A))).appliedOn(AB).shouldReturn(true); }
    @Test public void t014() { expectation(anyOf(startsWith(A), contains("C"), endsWith(B))).appliedOn("ACB").shouldReturn(true); }
    @Test public void t015() { expectation(anyOf()).appliedOn(null).shouldReturn(false); }

    @Test public void t286() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(LocalTime.of(10, 30)).shouldReturn(true); }
    @Test public void t287() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(LocalTime.of(11, 30)).shouldReturn(false); }
    @Test public void t288() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(null).shouldReturn(false); }

    @Test public void t345() { expectation(has(Object::toString).equalTo(A).and(String::length).equalTo(1)).appliedOn(A).shouldReturn(true); }
    @Test public void t346() { expectation(has(Object::toString).equalTo(A).and(String::length).equalTo(2)).appliedOn(A).shouldReturn(false); }
    @Test public void t347() { expectation(has(Object::toString).equalTo(A).and(String::length).equalTo(1)).appliedOn(B).shouldReturn(false); }

}
