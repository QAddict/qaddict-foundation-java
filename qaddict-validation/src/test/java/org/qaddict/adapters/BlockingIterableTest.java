package org.qaddict.adapters;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockingIterableTest {

    @Test
    public void test() {
        BlockingDeque<Integer> deque = new LinkedBlockingDeque<>();
        BlockingIterable<Integer> integers = new BlockingIterable<>(deque, 800, MILLISECONDS);
        new Thread(() -> stream(new Retry<>(1, 4, Duration.ofMillis(500)).spliterator(), false).forEach(deque::add)).start();
        Iterator<Integer> iterator = integers.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());
        assertFalse(iterator.hasNext());
    }

}