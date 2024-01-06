package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;

public record ConditionalPredicate<D>(Expectation<? super D> condition, Expectation<? super D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        EvaluationNode conditionNode = condition.evaluate(data);
        return conditionNode.result() ? expectation.evaluate(data) : conditionNode;
    }

}
