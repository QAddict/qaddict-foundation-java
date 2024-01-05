package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;

public record ConditionalPredicate<D>(Expectation<? super D> condition, Expectation<? super D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        EvaluationNode conditionNode = condition.evaluate(data);
        return conditionNode.result() ? expectation.evaluate(data) : conditionNode;
    }

}
