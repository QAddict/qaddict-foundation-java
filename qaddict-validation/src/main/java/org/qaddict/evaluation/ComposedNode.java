package org.qaddict.evaluation;

import org.qaddict.Expectation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.qaddict.evaluation.EvaluationNodes.actualValue;
import static org.qaddict.evaluation.EvaluationNodes.compose;

public record ComposedNode(boolean result, List<EvaluationNode> children) implements EvaluationNode {

    public static <D> List<Builder<D>> buildersFor(Collection<Expectation<? super D>> expectations) {
        return expectations.stream().<Builder<D>>map(ComposedNode::builderFor).toList();
    }

    public static <D> Builder<D> builderFor(Expectation<? super D> expectation) {
        return new BuilderImpl<>(expectation, new ArrayList<>());
    }

    public interface Builder<D> {
        Expectation<? super D> expectation();

        EvaluationNode evaluate(D item);

        EvaluationNode build(boolean result);
    }

    private record BuilderImpl<D>(Expectation<? super D> expectation, List<EvaluationNode> nodes) implements Builder<D> {

        @Override
        public EvaluationNode evaluate(D item) {
            EvaluationNode evaluated = expectation.evaluate(item);
            nodes.add(actualValue(item, evaluated));
            return evaluated;
        }

        @Override
        public EvaluationNode build(boolean result) {
            return EvaluationNodes.expectation(expectation().toString(), compose(result, nodes));
        }
    }
}
