package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.ComposedNode;
import org.qaddict.evaluation.ComposedNode.Builder;
import org.qaddict.evaluation.EvaluationNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Stream.iterate;
import static org.qaddict.evaluation.ComposedNode.builderFor;
import static org.qaddict.evaluation.EvaluationNodes.actualValue;
import static org.qaddict.evaluation.EvaluationNodes.expectation;
import static org.qaddict.evaluation.EvaluationNodes.result;

public record InOrderOfDefinitionExpectation<D>(Collection<Expectation<? super D>> expectations, Mode mode) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        if(data == null)
            return expectation("collection " + mode + " in order of definition " + expectations(), result(false));
        var iterator = data.iterator();
        Builder<Iterable<D>> builder = builderFor(this);
        Stream<? extends Builder<D>> stream = expectations.stream().map(ComposedNode::builderFor);
        return expectation("collection " + mode + " in order of definition " + expectations(), builder.build(switch (mode) {
            case EQUALS -> equals(iterator, stream, builder);
            case CONTAINS -> contains(iterator, stream, builder);
            case STARTS -> starts(iterator, stream, builder);
        }));
    }

    private boolean equals(Iterator<D> iterator, Stream<? extends Builder<D>> expectations, Builder<Iterable<D>> builder) {
        return starts(iterator, expectations, builder) && (!iterator.hasNext() || builder.add(expectation("No extra items", actualValue(extraItems(iterator), result(false)))).result());
    }

    private boolean starts(Iterator<D> iterator, Stream<? extends Builder<D>> builders, Builder<Iterable<D>> builder) {
        return builders.map(b -> builder.add(b, iterator.hasNext() && b.evaluate(iterator.next()).result())).reduce((l, r) -> l && r).orElse(true);
    }

    private boolean contains(Iterator<D> iterator, Stream<? extends Builder<D>> expectations, Builder<Iterable<D>> builder) {
        return expectations.allMatch(b -> builder.add(b, match(b, iterator)));
    }

    private boolean match(Builder<D> expectation, Iterator<D> iterator) {
        while (iterator.hasNext())
            if(expectation.evaluate(iterator.next()).result()) return true;
        return false;
    }

    private List<D> extraItems(Iterator<D> iterator) {
        return iterate(iterator, Iterator::hasNext, identity()).map(Iterator::next).toList();
    }

}
