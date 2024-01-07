package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.functions.Logic;

import static org.qaddict.evaluation.EvaluationNodes.result;
import static org.qaddict.evaluation.EvaluationNodes.throwable;

public record PredicateExpectation<D>(Logic<D> predicate) implements Expectation<D> {
    @Override
    public EvaluationNode evaluate(D data) {
        try {
            return result(predicate.test(data));
        } catch (Throwable throwable) {
            return throwable(throwable);
        }
    }

    @Override
    public Object description() {
        return "";
    }

}
