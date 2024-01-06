package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;

public record SynchronizedPredicate<D>(Expectation<D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        synchronized (data) {
            return expectation.evaluate(data);
        }
    }

}
