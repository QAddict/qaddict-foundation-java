package org.qaddict.evaluation;

public record ThrowableNode(Throwable throwable) implements EvaluationNode {
    @Override
    public boolean result() {
        return false;
    }
}
