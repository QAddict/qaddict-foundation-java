package org.qaddict.expectation;

import org.qaddict.Expectation;
import org.qaddict.evaluation.EvaluationNode;
import org.qaddict.evaluation.ExpectationNode;
import org.qaddict.functions.Executable;

public record ThrowableExpectation(Expectation<? super Throwable> expectation) implements Expectation<Executable> {
    @Override
    public EvaluationNode evaluate(Executable data) {
        EvaluationNode node = expectation.evaluate(run(data));
        return new ExpectationNode(node.result(), this, node);
    }

    @Override
    public String description() {
        return "throw throwable " + expectation.description();
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
