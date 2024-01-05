package foundation.qaddict.expectation.iterable;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.ComposedNode.Builder;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.expectation.iterable.algo.MaximumMatching;

import java.util.Collection;
import java.util.List;

import static foundation.qaddict.evaluation.ComposedNode.buildersFor;
import static foundation.qaddict.evaluation.EvaluationNodes.compose;
import static foundation.qaddict.expectation.iterable.EqualsInAnyOrderExpectation.stream;
import static foundation.qaddict.expectation.iterable.EqualsInAnyOrderExpectation.update;
import static java.util.stream.Collectors.toList;

public record ContainsInAnyOrderExpectation<D>(Collection<Expectation<? super D>> expectations) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        var graph = new MaximumMatching<D, Expectation<? super D>>();
        List<Builder<D>> evaluationBuilders = buildersFor(expectations);
        if(stream(data).anyMatch(item -> update(item, evaluationBuilders, graph).size() == expectations.size()))
            return compose(true, byPairing(evaluationBuilders, graph));
        return compose(false, byPairing(evaluationBuilders, graph));
    }

    public static <D> List<EvaluationNode> byPairing(List<Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        return evaluationBuilders.stream().map(builder -> builder.build(graph.pairing().containsKey(builder.expectation()))).collect(toList());
    }

}
