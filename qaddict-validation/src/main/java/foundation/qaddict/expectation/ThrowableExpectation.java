package foundation.qaddict.expectation;

import foundation.qaddict.Expectation;
import foundation.qaddict.evaluation.EvaluationNode;
import foundation.qaddict.evaluation.ExpectationNode;
import foundation.qaddict.functions.Executable;

public record ThrowableExpectation(Expectation<? super Throwable> expectation) implements Expectation<Executable> {
    @Override
    public EvaluationNode evaluate(Executable data) {
        EvaluationNode node = expectation.evaluate(run(data));
        return new ExpectationNode(node.result(), "Thrown throwable", node);
    }

    private Throwable run(Executable executable) {
        try {
            executable.run();
            return null;
        } catch (Throwable throwable) {
            return throwable;
        }
    }

}
