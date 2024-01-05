package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.EvaluationNodes;

public record ExpectationDescription<D>(Object description, Expectation<D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        return EvaluationNodes.expectation(description, expectation.evaluate(data));
    }

}
