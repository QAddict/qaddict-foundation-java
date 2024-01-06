package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;

import static org.qaddict.evaluation.EvaluationNodes.invert;
import static org.qaddict.evaluation.EvaluationNodes.named;

public record Negation<D>(Expectation<D> positiveExpectation) implements Expectation<D> {
    @Override
    public EvaluationNode evaluate(D data) {
        EvaluationNode node = positiveExpectation.evaluate(data);
        return named(!node.result(), "not", invert(node));
    }

}
