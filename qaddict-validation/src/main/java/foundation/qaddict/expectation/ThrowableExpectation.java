package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.ExpectationNode;
import foundation.qaddict.functions.Code;

public record ThrowableExpectation(Expectation<? super Throwable> expectation) implements Expectation<Code> {
    @Override
    public EvaluationNode evaluate(Code data) {
        EvaluationNode node = expectation.evaluate(run(data));
        return new ExpectationNode(node.result(), "Thrown throwable", node);
    }

    private Throwable run(Code code) {
        try {
            code.run();
            return null;
        } catch (Throwable throwable) {
            return throwable;
        }
    }

}
