package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.algo.MaximumMatching;
import org.qaddict.algo.MaximumMatching.Node;
import org.qaddict.evaluation.ComposedNode;
import org.qaddict.evaluation.ComposedNode.Builder;
import org.qaddict.evaluation.EvaluationNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.qaddict.evaluation.ComposedNode.buildersFor;
import static org.qaddict.evaluation.EvaluationNodes.actualValue;
import static org.qaddict.evaluation.EvaluationNodes.compose;
import static org.qaddict.evaluation.EvaluationNodes.expectation;
import static org.qaddict.evaluation.EvaluationNodes.result;

public record InAnyOrderExpectation<D>(Collection<Expectation<? super D>> expectations, Mode mode) implements Expectation<Iterable<D>> {

    @Override
    public EvaluationNode evaluate(Iterable<D> data) {
        if(data == null)
            return expectation("collection " + mode + " in order of definition " + expectations(), result(false));
        var graph = new MaximumMatching<Node<D>, Builder<D>>();
        List<ComposedNode.Builder<D>> evaluationBuilders = buildersFor(expectations);
        Stream<D> stream = StreamSupport.stream(data.spliterator(), false);
        return expectation("collection " + mode + " in any order " + expectations(), compose(switch (mode) {
            case EQUALS -> equals(stream, evaluationBuilders, graph);
            case CONTAINS -> contains(stream, evaluationBuilders, graph);
            case STARTS -> starts(stream, evaluationBuilders, graph);
        }, byPairing2(evaluationBuilders, graph)));
    }

    private boolean equals(Stream<D> data, List<Builder<D>> evaluationBuilders, MaximumMatching<Node<D>, Builder<D>> graph) {
        data.forEach(item -> update(item, evaluationBuilders, graph));
        return graph.pairing().size() == expectations.size() && graph.edges().size() == expectations.size();
    }

    private boolean contains(Stream<D> data, List<Builder<D>> evaluationBuilders, MaximumMatching<Node<D>, Builder<D>> graph) {
        return data.anyMatch(item -> update(item, evaluationBuilders, graph).size() == expectations.size());
    }

    private boolean starts(Stream<D> data, List<Builder<D>> evaluationBuilders, MaximumMatching<Node<D>, Builder<D>> graph) {
        return contains(data, evaluationBuilders, graph) && graph.edges().size() == expectations.size();
    }

    public static <D> Map<Builder<D>, Node<D>> update(D item, Collection<Builder<D>> expectations, MaximumMatching<Node<D>, Builder<D>> graph) {
        return graph.update(MaximumMatching.node(item), expectations.stream().filter(e -> e.evaluate(item).result()).collect(toList()));
    }

    private static <D> List<EvaluationNode> byPairing2(List<Builder<D>> evaluationBuilders, MaximumMatching<Node<D>, Builder<D>> graph) {
        return graph.free().isEmpty() ? byPairing(evaluationBuilders, graph) : concat(
                evaluationBuilders.stream().map(builder -> builder.build(graph.pairing().containsKey(builder))),
                Stream.of(actualValue(graph.free().size() + " items", expectation("No extra items", result(false))))
        ).collect(toList());
    }

    public static <D> List<EvaluationNode> byPairing(List<Builder<D>> evaluationBuilders, MaximumMatching<Node<D>, Builder<D>> graph) {
        return evaluationBuilders.stream().map(builder -> builder.build(graph.pairing().containsKey(builder))).collect(toList());
    }

}
