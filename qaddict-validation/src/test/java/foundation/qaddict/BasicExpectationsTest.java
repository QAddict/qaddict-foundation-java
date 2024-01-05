package foundation.qaddict;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static foundation.qaddict.Expectations.anything;
import static foundation.qaddict.Expectations.betweenExclusive;
import static foundation.qaddict.Expectations.betweenInclusive;
import static foundation.qaddict.Expectations.blankString;
import static foundation.qaddict.Expectations.closeTo;
import static foundation.qaddict.Expectations.contains;
import static foundation.qaddict.Expectations.emptyString;
import static foundation.qaddict.Expectations.endsWith;
import static foundation.qaddict.Expectations.equalTo;
import static foundation.qaddict.Expectations.equalToIgnoreCase;
import static foundation.qaddict.Expectations.findPattern;
import static foundation.qaddict.Expectations.has;
import static foundation.qaddict.Expectations.isNull;
import static foundation.qaddict.Expectations.lessOrEqualTo;
import static foundation.qaddict.Expectations.lessThan;
import static foundation.qaddict.Expectations.matchesPattern;
import static foundation.qaddict.Expectations.moreOrEqualTo;
import static foundation.qaddict.Expectations.moreThan;
import static foundation.qaddict.Expectations.nonNull;
import static foundation.qaddict.Expectations.not;
import static foundation.qaddict.Expectations.nullOrBlankString;
import static foundation.qaddict.Expectations.nullOrEmptyString;
import static foundation.qaddict.Expectations.sameInstanceAs;
import static foundation.qaddict.Expectations.startsWith;
import static foundation.qaddict.When.expectation;

public class BasicExpectationsTest {

    private static final String A = "A", B = "B", AB = "AB";

    @Test public void t001() { expectation(anything()).appliedOn(null).shouldReturn(true); }
    @Test public void t002() { expectation(anything()).appliedOn("A").shouldReturn(true); }

    @Test public void t003() { expectation(isNull()).appliedOn(null).shouldReturn(true); }
    @Test public void t004() { expectation(isNull()).appliedOn("").shouldReturn(false); }
    @Test public void t005() { expectation(isNull()).appliedOn(1).shouldReturn(false); }

    @Test public void t006() { expectation(nonNull()).appliedOn(null).shouldReturn(false); }
    @Test public void t007() { expectation(nonNull()).appliedOn("").shouldReturn(true); }
    @Test public void t008() { expectation(nonNull()).appliedOn(1).shouldReturn(true); }

    @Test public void t009() { expectation(sameInstanceAs(A)).appliedOn(null).shouldReturn(false); }
    @Test public void t010() { expectation(sameInstanceAs(A)).appliedOn(new String(A)).shouldReturn(false); }
    @Test public void t011() { expectation(sameInstanceAs(A)).appliedOn(A).shouldReturn(true); }

    @Test public void t012() { expectation(equalTo(A)).appliedOn(null).shouldReturn(false); }
    @Test public void t013() { expectation(equalTo(A)).appliedOn(new String(A)).shouldReturn(true); }
    @Test public void t014() { expectation(equalTo(A)).appliedOn(A).shouldReturn(true); }
    @Test public void t015() { expectation(equalTo(A)).appliedOn(B).shouldReturn(false); }
    @Test public void t016() { expectation(equalTo(A)).appliedOn(AB).shouldReturn(false); }
    @Test public void t017() { expectation(equalTo(AB)).appliedOn(A).shouldReturn(false); }
    @Test public void t018() { expectation(equalTo(10)).appliedOn(10).shouldReturn(true); }
    @Test public void t019() { expectation(equalTo(10)).appliedOn(11).shouldReturn(false); }

    @Test public void t020() { expectation(equalTo(10.0)).appliedOn(10.0).shouldReturn(true); }
    @Test public void t021() { expectation(equalTo(10.0)).appliedOn(10.000000005).shouldReturn(true); }
    @Test public void t022() { expectation(equalTo(10.0)).appliedOn(10.00000001).shouldReturn(true); }
    @Test public void t023() { expectation(equalTo(10.0)).appliedOn(10.000000015).shouldReturn(false); }
    @Test public void t024() { expectation(equalTo(10.0)).appliedOn(9.999999995).shouldReturn(true); }
    @Test public void t025() { expectation(equalTo(10.0)).appliedOn(9.99999999).shouldReturn(true); }
    @Test public void t026() { expectation(equalTo(10.0)).appliedOn(9.999999985).shouldReturn(false); }
    @Test public void t027() { expectation(equalTo(10.0)).appliedOn(null).shouldReturn(false); }

    @Test public void t028() { expectation(equalTo(10.0f)).appliedOn(10.0f).shouldReturn(true); }
    @Test public void t029() { expectation(equalTo(10.0f)).appliedOn(10.00005f).shouldReturn(true); }
    @Test public void t030() { expectation(equalTo(10.0f)).appliedOn(10.0001f).shouldReturn(true); }
    @Test public void t031() { expectation(equalTo(10.0f)).appliedOn(10.00015f).shouldReturn(false); }
    @Test public void t032() { expectation(equalTo(10.0f)).appliedOn(9.99995f).shouldReturn(true); }
    @Test public void t033() { expectation(equalTo(10.0f)).appliedOn(9.9999f).shouldReturn(true); }
    @Test public void t034() { expectation(equalTo(10.0f)).appliedOn(9.99985f).shouldReturn(false); }
    @Test public void t035() { expectation(equalTo(10.0f)).appliedOn(null).shouldReturn(false); }

    @Test public void t036() { expectation(not(A)).appliedOn(null).shouldReturn(true); }
    @Test public void t037() { expectation(not(A)).appliedOn(new String(A)).shouldReturn(false); }
    @Test public void t038() { expectation(not(A)).appliedOn(A).shouldReturn(false); }
    @Test public void t039() { expectation(not(A)).appliedOn(B).shouldReturn(true); }
    @Test public void t040() { expectation(not(A)).appliedOn(AB).shouldReturn(true); }
    @Test public void t041() { expectation(not(AB)).appliedOn(A).shouldReturn(true); }

    @Test public void t042() { expectation(moreThan(3)).appliedOn(4).shouldReturn(true); }
    @Test public void t043() { expectation(moreThan(3)).appliedOn(3).shouldReturn(false); }
    @Test public void t044() { expectation(moreThan(3)).appliedOn(2).shouldReturn(false); }
    @Test public void t045() { expectation(moreThan(3)).appliedOn(null).shouldReturn(false); }

    @Test public void t046() { expectation(moreOrEqualTo(3)).appliedOn(4).shouldReturn(true); }
    @Test public void t047() { expectation(moreOrEqualTo(3)).appliedOn(3).shouldReturn(true); }
    @Test public void t048() { expectation(moreOrEqualTo(3)).appliedOn(2).shouldReturn(false); }
    @Test public void t049() { expectation(moreOrEqualTo(3)).appliedOn(null).shouldReturn(false); }

    @Test public void t050() { expectation(lessThan(3)).appliedOn(4).shouldReturn(false); }
    @Test public void t051() { expectation(lessThan(3)).appliedOn(3).shouldReturn(false); }
    @Test public void t052() { expectation(lessThan(3)).appliedOn(2).shouldReturn(true); }
    @Test public void t053() { expectation(lessThan(3)).appliedOn(null).shouldReturn(false); }

    @Test public void t054() { expectation(lessOrEqualTo(3)).appliedOn(4).shouldReturn(false); }
    @Test public void t055() { expectation(lessOrEqualTo(3)).appliedOn(3).shouldReturn(true); }
    @Test public void t056() { expectation(lessOrEqualTo(3)).appliedOn(2).shouldReturn(true); }
    @Test public void t057() { expectation(lessOrEqualTo(3)).appliedOn(null).shouldReturn(false); }

    @Test public void t058() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(4.0).shouldReturn(true); }
    @Test public void t059() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(3.0).shouldReturn(true); }
    @Test public void t060() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(2.9).shouldReturn(false); }
    @Test public void t061() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(6.0).shouldReturn(true); }
    @Test public void t062() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(6.1).shouldReturn(false); }
    @Test public void t063() { expectation(betweenInclusive(3.0, 6.0)).appliedOn(null).shouldReturn(false); }

    @Test public void t064() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(4.0).shouldReturn(true); }
    @Test public void t065() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(3.0).shouldReturn(false); }
    @Test public void t066() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(2.9).shouldReturn(false); }
    @Test public void t067() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(6.0).shouldReturn(false); }
    @Test public void t068() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(6.1).shouldReturn(false); }
    @Test public void t069() { expectation(betweenExclusive(3.0, 6.0)).appliedOn(null).shouldReturn(false); }

    @Test public void t070() { expectation(closeTo(10.0, 0.1)).appliedOn(10.0).shouldReturn(true); }
    @Test public void t071() { expectation(closeTo(10.0, 0.1)).appliedOn(10.05).shouldReturn(true); }
    @Test public void t072() { expectation(closeTo(10.0, 0.1)).appliedOn(10.1).shouldReturn(true); }
    @Test public void t073() { expectation(closeTo(10.0, 0.1)).appliedOn(10.15).shouldReturn(false); }
    @Test public void t074() { expectation(closeTo(10.0, 0.1)).appliedOn(9.95).shouldReturn(true); }
    @Test public void t075() { expectation(closeTo(10.0, 0.1)).appliedOn(9.9).shouldReturn(true); }
    @Test public void t076() { expectation(closeTo(10.0, 0.1)).appliedOn(9.85).shouldReturn(false); }
    @Test public void t077() { expectation(closeTo(10.0, 0.1)).appliedOn(null).shouldReturn(false); }

    @Test public void t078() { expectation(closeTo(10.0f, 0.1f)).appliedOn(10.0f).shouldReturn(true); }
    @Test public void t079() { expectation(closeTo(10.0f, 0.1f)).appliedOn(10.05f).shouldReturn(true); }
    @Test public void t080() { expectation(closeTo(10.0f, 0.1f)).appliedOn(10.1f).shouldReturn(true); }
    @Test public void t081() { expectation(closeTo(10.0f, 0.1f)).appliedOn(10.15f).shouldReturn(false); }
    @Test public void t082() { expectation(closeTo(10.0f, 0.1f)).appliedOn(9.95f).shouldReturn(true); }
    @Test public void t083() { expectation(closeTo(10.0f, 0.1f)).appliedOn(9.9f).shouldReturn(true); }
    @Test public void t084() { expectation(closeTo(10.0f, 0.1f)).appliedOn(9.85f).shouldReturn(false); }
    @Test public void t085() { expectation(closeTo(10.0f, 0.1f)).appliedOn(null).shouldReturn(false); }

    @Test public void t086() { expectation(emptyString()).appliedOn(null).shouldReturn(false); }
    @Test public void t087() { expectation(emptyString()).appliedOn("").shouldReturn(true); }
    @Test public void t088() { expectation(emptyString()).appliedOn(" ").shouldReturn(false); }
    @Test public void t089() { expectation(emptyString()).appliedOn("h").shouldReturn(false); }

    @Test public void t090() { expectation(blankString()).appliedOn(null).shouldReturn(false); }
    @Test public void t091() { expectation(blankString()).appliedOn("").shouldReturn(true); }
    @Test public void t092() { expectation(blankString()).appliedOn(" ").shouldReturn(true); }
    @Test public void t093() { expectation(blankString()).appliedOn("\t").shouldReturn(true); }
    @Test public void t094() { expectation(blankString()).appliedOn("\n").shouldReturn(true); }
    @Test public void t095() { expectation(blankString()).appliedOn("\r").shouldReturn(true); }
    @Test public void t096() { expectation(blankString()).appliedOn("h").shouldReturn(false); }

    @Test public void t097() { expectation(nullOrEmptyString()).appliedOn(null).shouldReturn(true); }
    @Test public void t098() { expectation(nullOrEmptyString()).appliedOn("").shouldReturn(true); }
    @Test public void t099() { expectation(nullOrEmptyString()).appliedOn(" ").shouldReturn(false); }
    @Test public void t100() { expectation(nullOrEmptyString()).appliedOn("h").shouldReturn(false); }

    @Test public void t101() { expectation(nullOrBlankString()).appliedOn(null).shouldReturn(true); }
    @Test public void t102() { expectation(nullOrBlankString()).appliedOn("").shouldReturn(true); }
    @Test public void t103() { expectation(nullOrBlankString()).appliedOn(" ").shouldReturn(true); }
    @Test public void t104() { expectation(nullOrBlankString()).appliedOn("\t").shouldReturn(true); }
    @Test public void t105() { expectation(nullOrBlankString()).appliedOn("\n").shouldReturn(true); }
    @Test public void t106() { expectation(nullOrBlankString()).appliedOn("\r").shouldReturn(true); }
    @Test public void t107() { expectation(nullOrBlankString()).appliedOn("h").shouldReturn(false); }

    @Test public void t108() { expectation(equalToIgnoreCase("ab")).appliedOn("ab").shouldReturn(true); }
    @Test public void t109() { expectation(equalToIgnoreCase("ab")).appliedOn("aB").shouldReturn(true); }
    @Test public void t110() { expectation(equalToIgnoreCase("ab")).appliedOn("AB").shouldReturn(true); }
    @Test public void t111() { expectation(equalToIgnoreCase("ab")).appliedOn("abc").shouldReturn(false); }
    @Test public void t112() { expectation(equalToIgnoreCase("ab")).appliedOn("aa").shouldReturn(false); }
    @Test public void t113() { expectation(equalToIgnoreCase("ab")).appliedOn(null).shouldReturn(false); }

    @Test public void t114() { expectation(startsWith("ab")).appliedOn("ab").shouldReturn(true); }
    @Test public void t115() { expectation(startsWith("ab")).appliedOn("abc").shouldReturn(true); }
    @Test public void t116() { expectation(startsWith("ab")).appliedOn("cabc").shouldReturn(false); }
    @Test public void t117() { expectation(startsWith("ab")).appliedOn("a").shouldReturn(false); }
    @Test public void t118() { expectation(startsWith("ab")).appliedOn(null).shouldReturn(false); }

    @Test public void t119() { expectation(endsWith("ab")).appliedOn("ab").shouldReturn(true); }
    @Test public void t120() { expectation(endsWith("ab")).appliedOn("cab").shouldReturn(true); }
    @Test public void t121() { expectation(endsWith("ab")).appliedOn("cabc").shouldReturn(false); }
    @Test public void t122() { expectation(endsWith("ab")).appliedOn("b").shouldReturn(false); }
    @Test public void t123() { expectation(endsWith("ab")).appliedOn(null).shouldReturn(false); }

    @Test public void t124() { expectation(contains("ab")).appliedOn("ab").shouldReturn(true); }
    @Test public void t125() { expectation(contains("ab")).appliedOn("cab").shouldReturn(true); }
    @Test public void t126() { expectation(contains("ab")).appliedOn("abc").shouldReturn(true); }
    @Test public void t127() { expectation(contains("ab")).appliedOn("cabc").shouldReturn(true); }
    @Test public void t128() { expectation(contains("ab")).appliedOn("b").shouldReturn(false); }
    @Test public void t129() { expectation(contains("ab")).appliedOn("a").shouldReturn(false); }
    @Test public void t130() { expectation(contains("ab")).appliedOn("baabh").shouldReturn(true); }
    @Test public void t131() { expectation(contains("ab")).appliedOn(null).shouldReturn(false); }

    @Test public void t132() { expectation(matchesPattern("a.b")).appliedOn("afb").shouldReturn(true); }
    @Test public void t133() { expectation(matchesPattern("a.b")).appliedOn("xafb").shouldReturn(false); }
    @Test public void t134() { expectation(matchesPattern("a.b")).appliedOn("xafa").shouldReturn(false); }
    @Test public void t135() { expectation(matchesPattern("a.b")).appliedOn(null).shouldReturn(false); }

    @Test public void t136() { expectation(findPattern("a.b")).appliedOn("afb").shouldReturn(true); }
    @Test public void t137() { expectation(findPattern("a.b")).appliedOn("xafb").shouldReturn(true); }
    @Test public void t138() { expectation(findPattern("a.b")).appliedOn("xafa").shouldReturn(false); }
    @Test public void t139() { expectation(findPattern("a.b")).appliedOn(null).shouldReturn(false); }

    @Test public void t286() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(LocalTime.of(10, 30)).shouldReturn(true); }
    @Test public void t287() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(LocalTime.of(11, 30)).shouldReturn(false); }
    @Test public void t288() { expectation(has(LocalTime::getHour).equalTo(10)).appliedOn(null).shouldReturn(false); }

}
