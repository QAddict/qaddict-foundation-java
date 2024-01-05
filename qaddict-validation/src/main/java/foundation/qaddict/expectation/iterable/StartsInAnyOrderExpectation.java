package foundation.qaddict.expectation.iterable;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.ComposedNode;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.ResultNode;
import foundation.qaddict.expectation.iterable.algo.MaximumMatching;

import java.util.Collection;
import java.util.List;

import static foundation.qaddict.evaluation.ComposedNode.buildersFor;
import static foundation.qaddict.expectation.iterable.EqualsInAnyOrderExpectation.stream;
import static foundation.qaddict.expectation.iterable.EqualsInAnyOrderExpectation.update;

public record StartsInAnyOrderExpectation<D>(Collection<Expectation<? super D>> expectations) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        var graph = new MaximumMatching<D, Expectation<? super D>>();
        List<ComposedNode.Builder<D>> evaluationBuilders = buildersFor(expectations);
        if(stream(data).anyMatch(item -> update(item, evaluationBuilders, graph).size() == expectations.size()) && graph.edges().size() == expectations.size())
            return new ResultNode(true);
        return new ResultNode(false);
    }

}
