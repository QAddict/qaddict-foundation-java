package org.qaddict.adapters;

import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Stream.generate;

public class BlockingIterable<D> implements Iterable<D> {

    private final BlockingQueue<D> queue;
    private final long timeout;
    private final TimeUnit timeUnit;

    public BlockingIterable(BlockingQueue<D> queue, long timeout, TimeUnit timeUnit) {
        this.queue = queue;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public Iterator<D> iterator() {
        return generate(this::poll).takeWhile(Objects::nonNull).iterator();
    }

    private D poll() {
        try {
            return queue.poll(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
