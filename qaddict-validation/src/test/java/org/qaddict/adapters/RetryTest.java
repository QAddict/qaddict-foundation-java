package org.qaddict.adapters;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RetryTest {

    @Test
    public void test() {
        Retry<Integer> retry = new Retry<>(1, 3, Duration.ofSeconds(1));
        Iterator<Integer> iterator = retry.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertFalse(iterator.hasNext());
    }

}
