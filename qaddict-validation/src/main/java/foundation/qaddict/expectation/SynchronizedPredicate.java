package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;

public record SynchronizedPredicate<D>(Expectation<D> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        synchronized (data) {
            return expectation.evaluate(data);
        }
    }

}
