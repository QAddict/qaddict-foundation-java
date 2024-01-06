package org.qaddict.expectation.iterable;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.evaluation.ResultNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

public record InOrderOfDefinitionExpectation<D>(Collection<Expectation<? super D>> expectations, Mode mode) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        var iterator = data.iterator();
        Stream<Expectation<? super D>> stream = expectations.stream();
        if(switch (mode) {
            case EQUALS -> equals(iterator, stream);
            case CONTAINS -> contains(iterator, stream);
            case STARTS -> starts(iterator, stream);
        }) return new ResultNode(true);
        return new ResultNode(false);
    }

    private boolean equals(Iterator<D> iterator, Stream<Expectation<? super D>> expectations) {
        return starts(iterator, expectations) && !iterator.hasNext();
    }

    private boolean starts(Iterator<D> iterator, Stream<Expectation<? super D>> expectations) {
        return expectations.map(expectation -> iterator.hasNext() && expectation.evaluate(iterator.next()).result()).reduce((l, r) -> l && r).orElse(true);
    }

    private boolean contains(Iterator<D> iterator, Stream<Expectation<? super D>> expectations) {
        return expectations.allMatch(expectation -> match(expectation, iterator));
    }

    private boolean match(Expectation<? super D> expectation, Iterator<D> iterator) {
        while (iterator.hasNext())
            if(expectation.evaluate(iterator.next()).result())
                return true;
        return false;
    }
}
