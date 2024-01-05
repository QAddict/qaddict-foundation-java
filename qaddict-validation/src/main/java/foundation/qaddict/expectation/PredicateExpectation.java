package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.functions.Logic;

import static foundation.qaddict.evaluation.EvaluationNodes.result;
import static foundation.qaddict.evaluation.EvaluationNodes.throwable;

public record PredicateExpectation<D>(Logic<D> predicate) implements Expectation<D> {
    @Override
    public EvaluationNode evaluate(D data) {
        try {
            return result(predicate.test(data));
        } catch (Throwable throwable) {
            return throwable(throwable);
        }
    }
}
