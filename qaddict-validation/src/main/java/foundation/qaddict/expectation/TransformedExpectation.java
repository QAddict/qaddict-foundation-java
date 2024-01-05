package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.functions.Transformation;

import static foundation.qaddict.evaluation.EvaluationNodes.actualValue;
import static foundation.qaddict.evaluation.EvaluationNodes.throwable;

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

}
