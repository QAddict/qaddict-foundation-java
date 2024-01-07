package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.evaluation.EvaluationNodes;

public record ExpectationDescription<D>(Object description, Expectation<D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        return EvaluationNodes.expectation(this, expectation.evaluate(data));
    }

}
