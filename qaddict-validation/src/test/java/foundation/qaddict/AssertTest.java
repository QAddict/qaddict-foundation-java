package foundation.qaddict;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static foundation.qaddict.Expectations.collectionEqualsInAnyOrder;
import static foundation.qaddict.Expectations.equalTo;
import static foundation.qaddict.Expectations.has;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertTest {

    @Test
    public void assertThatShouldReturnTestedValueIfValidationPass() {
        assertEquals(Assert.that("A", equalTo("A")), "A");
    }

    @Test
    public void assertThatShouldThrowAssertionErrorWhenValidationFails() {
        assertThrows(AssertionError.class, () -> Assert.that("A", equalTo("B")));
    }

    @Test
    public void assertMessage() {
        Assert.that(
                List.of(LocalDate.of(2024, 1, 14), LocalDate.of(2024, 2, 8)),
                collectionEqualsInAnyOrder(
                        has(LocalDate::getDayOfWeek).equalTo(FRIDAY).and(LocalDate::getMonth).equalTo(JANUARY),
                        has(LocalDate::getDayOfWeek).equalTo(FRIDAY).and(LocalDate::getMonth).equalTo(JANUARY)
                )
        );
    }
}
