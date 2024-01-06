package org.qaddict.adapters;

import java.time.Duration;
import java.util.Iterator;

import static java.util.stream.Stream.iterate;

public class Retry<D> implements Iterable<D> {

    private final D entry;
    private final long maxAttempts;
    private final Duration delay;

    public Retry(D entry, long maxAttempts, Duration delay) {
        this.entry = entry;
        this.maxAttempts = maxAttempts;
        this.delay = delay;
    }

    @Override
    public Iterator<D> iterator() {
        return iterate(entry, this::apply).limit(maxAttempts).iterator();
    }

    private D apply(D entry) {
        try {
            Thread.sleep(delay.toMillis());
            return entry;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
