package foundation.qaddict.expectation.iterable;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.ComposedNode;
import foundation.qaddict.evaluation.ComposedNode.Builder;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.expectation.iterable.algo.MaximumMatching;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static foundation.qaddict.evaluation.ComposedNode.buildersFor;
import static foundation.qaddict.evaluation.EvaluationNodes.actualValue;
import static foundation.qaddict.evaluation.EvaluationNodes.compose;
import static foundation.qaddict.evaluation.EvaluationNodes.expectation;
import static foundation.qaddict.evaluation.EvaluationNodes.result;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public record InAnyOrderExpectation<D>(Collection<Expectation<? super D>> expectations, Mode mode) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        var graph = new MaximumMatching<D, Expectation<? super D>>();
        List<ComposedNode.Builder<D>> evaluationBuilders = buildersFor(expectations);
        Stream<D> stream = StreamSupport.stream(data.spliterator(), false);
        if(switch (mode) {
            case EQUALS -> equals(stream, evaluationBuilders, graph);
            case CONTAINS -> contains(stream, evaluationBuilders, graph);
            case STARTS -> starts(stream, evaluationBuilders, graph);
        }) return expectation("collection " + mode + " in any order " + expectations(), compose(true, byPairing(evaluationBuilders, graph)));
        return expectation("collection " + mode + " in any order " + expectations(), compose(false, byPairing2(evaluationBuilders, graph)));
    }

    private boolean equals(Stream<D> data, List<ComposedNode.Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        data.forEach(item -> update(item, evaluationBuilders, graph));
        return graph.pairing().size() == expectations.size() && graph.edges().size() == expectations.size();
    }

    private boolean contains(Stream<D> data, List<ComposedNode.Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        return data.anyMatch(item -> update(item, evaluationBuilders, graph).size() == expectations.size());
    }

    private boolean starts(Stream<D> data, List<ComposedNode.Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        return contains(data, evaluationBuilders, graph) && graph.edges().size() == expectations.size();
    }

    public static <D> Map<Expectation<? super D>, D> update(D item, Collection<Builder<D>> expectations, MaximumMatching<D, Expectation<? super D>> graph) {
        return graph.update(item, expectations.stream().filter(e -> e.evaluate(item).result()).map(Builder::expectation).collect(toList()));
    }

    private static <D> List<EvaluationNode> byPairing2(List<Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        return graph.free().isEmpty() ? byPairing(evaluationBuilders, graph) : concat(
                evaluationBuilders.stream().map(builder -> builder.build(graph.pairing().containsKey(builder.expectation()))),
                Stream.of(actualValue(graph.free().size() + " items", expectation("No extra items", result(false))))
        ).collect(toList());
    }

    public static <D> List<EvaluationNode> byPairing(List<Builder<D>> evaluationBuilders, MaximumMatching<D, Expectation<? super D>> graph) {
        return evaluationBuilders.stream().map(builder -> builder.build(graph.pairing().containsKey(builder.expectation()))).collect(toList());
    }

}
