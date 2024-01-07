package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.ComposedNode.Builder;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.evaluation.EvaluationNodes;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;
import static org.qaddict.evaluation.ComposedNode.builderFor;
import static org.qaddict.evaluation.EvaluationNodes.result;

public record EveryElementExpectation<D>(Expectation<? super D> expectation) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        if(data == null)
            return EvaluationNodes.expectation(this, result(false));
        Builder<? super D> builder = builderFor(expectation);
        Stream<D> stream = stream(data.spliterator(), false);
        return EvaluationNodes.expectation(this, builder.build(stream.allMatch(item -> builder.evaluate(item).result())));
    }

    @Override
    public Object description() {
        return "all elements in collection match " + expectation().description();
    }

}
