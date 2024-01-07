package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.functions.Transformation;

import static org.qaddict.evaluation.EvaluationNodes.actualValue;
import static org.qaddict.evaluation.EvaluationNodes.throwable;

public record TransformedExpectation<D, V>(Transformation<? super D, ? extends V> transformation, Expectation<? super V> expectation) implements Expectation<D> {

    @Override
    public EvaluationNode evaluate(D data) {
        try {
            V value = transformation.transform(data);
            return actualValue(value, expectation.evaluate(value));
        } catch (Throwable throwable) {
            return throwable(throwable);
        }
    }

    @Override
    public Object description() {
        return expectation.description();
    }

}
