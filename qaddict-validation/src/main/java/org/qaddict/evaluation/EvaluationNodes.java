package org.qaddict.evaluation;

import org.qaddict.Described;

import java.util.List;

public final class EvaluationNodes {

    public static ActualValueNode actualValue(Object actualValue, EvaluationNode node) {
        return new ActualValueNode(node.result(), actualValue, node);
    }

    public static PathNode named(Object name, EvaluationNode node) {
        return new PathNode(node.result(), name, node);
    }

    public static PathNode named(boolean result, Object name, EvaluationNode node) {
        return new PathNode(result, name, node);
    }

    public static ExpectationNode expectation(Described expectation, EvaluationNode node) {
        return new ExpectationNode(node.result(), expectation, node);
    }

    public static ResultNode result(boolean result) {
        return new ResultNode(result);
    }

    public static ThrowableNode throwable(Throwable throwable) {
        return new ThrowableNode(throwable);
    }

    public static CandidateInversionNode invert(EvaluationNode node) {
        return new CandidateInversionNode(node.result(), node);
    }

    public static ComposedNode compose(boolean result, List<EvaluationNode> children) {
        return new ComposedNode(result, children);
    }

}
