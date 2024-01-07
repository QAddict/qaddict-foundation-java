package org.qaddict.algo;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Stream.generate;
import static java.util.stream.Stream.iterate;

public record MaximumMatching<I, O>(Map<I, Collection<O>> edges, Map<O, I> pairing, Set<I> free) {

    public MaximumMatching() {
        this(new HashMap<>(), new HashMap<>(), new HashSet<>());
    }

    public Map<O, I> update(I start, Collection<O> ends) {
        edges.put(start, ends);
        free.add(start);
        generate(this::findImprovingPath).takeWhile(Objects::nonNull).flatMap(path -> iterate(path, Objects::nonNull, e -> e.prev.prev)).forEach(e -> {
            pairing.put(e.node, e.prev.node);
            free.remove(e.prev.node);
        });
        return pairing;
    }

    public Edge<O, I> findImprovingPath() {
        var queue = free.stream().map(i -> new Edge<I, O>(null, i)).collect(toCollection(LinkedList::new));
        var visited = new HashSet<I>();
        while (!queue.isEmpty()) {
            var a = queue.poll();
            if(visited.add(a.node)) {
                for(var b : edges.get(a.node)) {
                    if(!pairing.containsKey(b))
                        return new Edge<>(a, b);
                    queue.add(new Edge<>(new Edge<>(a, b), pairing.get(b)));
                }
            }
        }
        return null;
    }

    public interface Node<I> {
        I getValue();
    }

    public static <I> Node<I> node(I i) {
        return () -> i;
    }

    public record Edge<I, O>(Edge<O, I> prev, I node) {}

}
