package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;

import static foundation.qaddict.evaluation.EvaluationNodes.invert;
import static foundation.qaddict.evaluation.EvaluationNodes.named;

public record Negation<D>(Expectation<D> positiveExpectation) implements Expectation<D> {
    @Override
    public EvaluationNode evaluate(D data) {
        EvaluationNode node = positiveExpectation.evaluate(data);
        return named(!node.result(), "not", invert(node));
    }

}
